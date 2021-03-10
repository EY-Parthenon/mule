/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.extension.internal.loader.java.property;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.mule.runtime.api.util.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.parameter.ValueProviderModel;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.module.extension.internal.runtime.resolver.ParameterValueResolver;
import org.mule.runtime.module.extension.internal.util.ReflectionCache;
import org.mule.runtime.module.extension.internal.value.ValueProviderFactory;

/**
 * ADD JDOC
 *
 * @since 4.4
 */
public final class FieldsValueProviderFactoryModelProperty implements ModelProperty {

  private final Map<String, ValueProviderFactoryModelProperty> fieldsValueProviderFactories;

  /**
   * ADD JDOC
   */
  public FieldsValueProviderFactoryModelProperty(Map<String, ValueProviderFactoryModelProperty> fieldsValueProviderFactories) {
    requireNonNull(fieldsValueProviderFactories, "Map of value provider factories cannot be null");
    this.fieldsValueProviderFactories = fieldsValueProviderFactories;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "FieldsValueProviderFactory";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPublic() {
    return false;
  }

  public Map<String, ValueProviderFactoryModelProperty> getFieldsValueProviderFactories() {
    return fieldsValueProviderFactories;
  }

}
