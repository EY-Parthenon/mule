/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.extension.internal.value;

import static java.lang.String.format;
import static org.mule.runtime.core.api.lifecycle.LifecycleUtils.initialiseIfNeeded;
import static org.mule.runtime.core.api.util.ClassUtils.instantiateClass;
import static org.mule.runtime.extension.api.values.ValueResolvingException.MISSING_REQUIRED_PARAMETERS;
import static org.mule.runtime.extension.api.values.ValueResolvingException.UNKNOWN;
import static org.mule.runtime.module.extension.internal.util.IntrospectionUtils.setValueIntoField;
import static org.mule.sdk.api.data.sample.SampleDataException.CONNECTION_FAILURE;

import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.module.extension.internal.loader.java.property.InjectableParameterInfo;
import org.mule.runtime.module.extension.internal.loader.java.property.ValueProviderFactoryModelProperty;
import org.mule.runtime.module.extension.internal.runtime.resolver.ParameterValueResolver;
import org.mule.runtime.module.extension.internal.util.ReflectionCache;
import org.mule.sdk.api.values.ValueProvider;
import org.mule.sdk.api.values.ValueResolvingException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides instances of the {@link ValueProvider}
 *
 * @since 4.0
 */
public class ValueProviderFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(ValueProviderFactory.class);

  private final ValueProviderFactoryModelProperty factoryModelProperty;
  private final ParameterValueResolver parameterValueResolver;
  private final Supplier<Object> connectionSupplier;
  private final Supplier<Object> configurationSupplier;
  private final Field connectionField;
  private final Field configField;
  private final ReflectionCache reflectionCache;
  private final MuleContext muleContext;

  public ValueProviderFactory(ValueProviderFactoryModelProperty factoryModelProperty,
                              ParameterValueResolver parameterValueResolver, Supplier<Object> connectionSupplier,
                              Supplier<Object> configurationSupplier, Field connectionField, Field configField,
                              ReflectionCache reflectionCache, MuleContext muleContext) {
    this.factoryModelProperty = factoryModelProperty;
    this.parameterValueResolver = parameterValueResolver;
    this.connectionSupplier = connectionSupplier;
    this.configurationSupplier = configurationSupplier;
    this.connectionField = connectionField;
    this.configField = configField;
    this.reflectionCache = reflectionCache;
    this.muleContext = muleContext;
  }

  ValueProvider createValueProvider() throws ValueResolvingException {
    Class<?> resolverClass = factoryModelProperty.getValueProvider();

    try {
      Object resolver = instantiateClass(resolverClass);
      initialiseIfNeeded(resolver, true, muleContext);

      injectValueProviderFields(resolver);

      if (factoryModelProperty.usesConnection()) {
        Object connection;
        try {
          connection = connectionSupplier.get();
        } catch (Exception e) {
          throw new ValueResolvingException("Failed to establish connection: " + e.getMessage(), CONNECTION_FAILURE, e);
        }

        if (connection == null) {
          throw new ValueResolvingException("The value provider requires a connection and none was provided",
                                            MISSING_REQUIRED_PARAMETERS);
        }
        setValueIntoField(resolver, connectionSupplier.get(), connectionField);
      }

      if (factoryModelProperty.usesConfig()) {
        Object config = configurationSupplier.get();
        if (config == null) {
          throw new ValueResolvingException("The value provider requires a configuration and none was provided",
                                            MISSING_REQUIRED_PARAMETERS);
        }
        setValueIntoField(resolver, configurationSupplier.get(), configField);
      }
      return adaptResolver(resolver);
    } catch (ValueResolvingException e) {
      throw e;
    } catch (Exception e) {
      throw new ValueResolvingException("An error occurred trying to create a ValueProvider", UNKNOWN, e);
    }
  }

  private void injectValueProviderFields(Object resolver) throws ValueResolvingException {
    List<String> missingParameters = new ArrayList<>();
    for (InjectableParameterInfo injectableParam : factoryModelProperty.getInjectableParameters()) {
      Object parameterValue = null;
      String parameterName = injectableParam.getParameterName();
      try {
        parameterValue = parameterValueResolver.getParameterValue(parameterName);
      } catch (org.mule.runtime.module.extension.internal.runtime.ValueResolvingException ignored) {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("An error occurred while resolving parameter " + parameterName, ignored);
        }
      }

      if (parameterValue != null) {
        setValueIntoField(resolver, parameterValue, parameterName, reflectionCache);
      } else if (injectableParam.isRequired()) {
        missingParameters.add(parameterName);
      }
    }

    if (!missingParameters.isEmpty()) {
      throw new ValueResolvingException("Unable to retrieve values. There are missing required parameters for the resolution: "
          + missingParameters, MISSING_REQUIRED_PARAMETERS);
    }
  }

  private ValueProvider adaptResolver(Object resolverObject) throws ValueResolvingException {
    if (resolverObject instanceof ValueProvider) {
      return (ValueProvider) resolverObject;
    } else if (resolverObject instanceof org.mule.runtime.extension.api.values.ValueProvider) {
      return new SdkValueProviderAdapter((org.mule.runtime.extension.api.values.ValueProvider) resolverObject);
    } else {
      throw new ValueResolvingException(format("An error occurred trying to create a ValueProvider: %s should implement %s or %s",
                                               resolverObject.getClass().getName(),
                                               ValueProvider.class.getName(),
                                               org.mule.sdk.api.values.ValueProvider.class.getName()),
                                        UNKNOWN);
    }
  }

}
