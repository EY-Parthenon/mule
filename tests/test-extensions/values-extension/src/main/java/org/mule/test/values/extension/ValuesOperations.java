/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.test.values.extension;

import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.metadata.TypeResolver;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.sdk.api.annotation.binding.Binding;
import org.mule.test.values.extension.metadata.JsonTypeResolver;
import org.mule.test.values.extension.resolver.MultiLevelValueProvider;
import org.mule.test.values.extension.resolver.SimpleValueProvider;
import org.mule.test.values.extension.resolver.WithArrayParameterValueProvider;
import org.mule.test.values.extension.resolver.WithComplexActingParameter;
import org.mule.test.values.extension.resolver.WithConnectionValueProvider;
import org.mule.test.values.extension.resolver.WithConfigValueProvider;
import org.mule.test.values.extension.resolver.WithEnumParameterValueProvider;
import org.mule.test.values.extension.resolver.WithErrorValueProvider;
import org.mule.test.values.extension.resolver.WithFourActingParametersValueProvider;
import org.mule.test.values.extension.resolver.WithMapParameterValueProvider;
import org.mule.test.values.extension.resolver.WithOptionalParametersValueProvider;
import org.mule.test.values.extension.resolver.WithOptionalParametersWithDefaultValueProvider;
import org.mule.test.values.extension.resolver.WithPojoParameterValueProvider;
import org.mule.test.values.extension.resolver.WithRequiredAndOptionalParametersValueProvider;
import org.mule.test.values.extension.resolver.WithRequiredParameterFromGroupValueProvider;
import org.mule.test.values.extension.resolver.WithRequiredParameterValueProvider;
import org.mule.test.values.extension.resolver.WithRequiredParametersValueProvider;
import org.mule.test.values.extension.resolver.WithMuleContextValueProvider;
import org.mule.test.values.extension.resolver.WithTwoActingParametersValueProvider;

import java.io.InputStream;
import java.util.List;

public class ValuesOperations {

  public void singleValuesEnabledParameter(@OfValues(SimpleValueProvider.class) String channels) {

  }

  public void singleValuesEnabledParameterWithConnection(@OfValues(WithConnectionValueProvider.class) String channels,
                                                         @Connection ValuesConnection connection) {}

  public void singleValuesEnabledParameterWithConfiguration(@OfValues(WithConfigValueProvider.class) String channels,
                                                            @Connection ValuesConnection connection) {}

  public void singleValuesEnabledParameterWithRequiredParameters(@OfValues(WithRequiredParametersValueProvider.class) String channels,
                                                                 String requiredString,
                                                                 boolean requiredBoolean,
                                                                 int requiredInteger,
                                                                 List<String> strings) {}

  public void singleValuesEnabledParameterInsideParameterGroup(@ParameterGroup(
      name = "ValuesGroup") GroupWithValuesParameter optionsParameter) {}

  public void singleValuesEnabledParameterRequiresValuesOfParameterGroup(@OfValues(WithRequiredParameterFromGroupValueProvider.class) String values,
                                                                         @ParameterGroup(
                                                                             name = "ValuesGroup") GroupWithValuesParameter optionsParameter) {}

  public void multiLevelValue(@OfValues(MultiLevelValueProvider.class) @ParameterGroup(
      name = "values") GroupAsMultiLevelValue optionsParameter) {

  }

  public void singleValuesWithRequiredParameterWithAlias(@ParameterGroup(
      name = "someGroup") WithRequiredParameterWithAliasGroup group) {}

  public void resolverGetsMuleContextInjection(@OfValues(WithMuleContextValueProvider.class) String channel) {

  }

  public void valuesInsideShowInDslGroup(@OfValues(WithRequiredParameterFromGroupValueProvider.class) String values,
                                         @ParameterGroup(name = "ValuesGroup",
                                             showInDsl = true) GroupWithValuesParameter optionsParameter) {

  }

  public void withErrorValueProvider(@OfValues(WithErrorValueProvider.class) String values, String errorCode) {

  }

  public void withComplexActingParameter(@Optional @OfValues(WithComplexActingParameter.class) String providedParameter,
                                         ComplexActingParameter complexActingParameter) {}

  public void withRequiredParameter(@OfValues(WithRequiredParameterValueProvider.class) String providedParameters,
                                    String requiredValue) {}

  public void withRequiredParameterAndOptionalParameterAsRequired(@OfValues(WithRequiredAndOptionalParametersValueProvider.class) String providedParameters,
                                                                  String requiredValue, String optionalValue) {}

  public void withRequiredAndOptionalParameters(@OfValues(WithRequiredAndOptionalParametersValueProvider.class) String providedParameters,
                                                String requiredValue, @Optional String optionalValue) {}

  public void withRequiredAndOptionalWithDefaultParameters(@OfValues(WithRequiredAndOptionalParametersValueProvider.class) String providedParameters,
                                                           String requiredValue, @Optional(
                                                               defaultValue = "OPERATION_DEFAULT_VALUE") String optionalValue) {}

  public void withOptionalParameterAsRequired(@OfValues(WithOptionalParametersValueProvider.class) String providedParameters,
                                              String optionalValue) {}

  public void withOptionalParameter(@OfValues(WithOptionalParametersValueProvider.class) String providedParameters,
                                    @Optional String optionalValue) {}

  public void withOptionalParameterWithDefault(@OfValues(WithOptionalParametersValueProvider.class) String providedParameters,
                                               @Optional(defaultValue = "OPERATION_DEFAULT_VALUE") String optionalValue) {}

  public void withVPOptionalParameterWithDefaultValue(@OfValues(WithOptionalParametersWithDefaultValueProvider.class) String providedParameters,
                                                      @Optional(defaultValue = "OPERATION_DEFAULT_VALUE") String optionalValue) {}

  public void withBoundActingParameter(@OfValues(
      value = WithRequiredParameterValueProvider.class,
      bindings = {@Binding(actingParameter = "requiredValue", path = "actingParameter")}) String parameterWithValues,
                                       String actingParameter) {}

  public void withBoundActingParameterField(@OfValues(
      value = WithRequiredParameterValueProvider.class,
      bindings = {@Binding(actingParameter = "requiredValue", path = "actingParameter.field")}) String parameterWithValues,
                                            @TypeResolver(JsonTypeResolver.class) InputStream actingParameter) {}

  public void withTwoActingParameters(@OfValues(
      value = WithTwoActingParametersValueProvider.class,
      bindings = {@Binding(actingParameter = "requiredValue", path = "actingParameter.field")}) String parameterWithValues,
                                      String scalarActingParameter,
                                      InputStream actingParameter) {}

  public void withTwoBoundActingParameters(@OfValues(
      value = WithTwoActingParametersValueProvider.class,
      bindings = {@Binding(actingParameter = "requiredValue", path = "actingParameter.field"),
          @Binding(actingParameter = "scalarActingParameter", path = "anotherParameter")}) String parameterWithValues,
                                           String anotherParameter,
                                           InputStream actingParameter) {}


  public void withBoundActingParameterToXmlTagContent(@OfValues(
      value = WithRequiredParameterValueProvider.class,
      bindings = {
          @Binding(actingParameter = "requiredValue", path = "actingParameter.nested.xmlTag")}) String parameterWithValues,
                                                      InputStream actingParameter) {}

  public void withBoundActingParameterToXmlTagAtttribute(@OfValues(
      value = WithRequiredParameterValueProvider.class,
      bindings = {@Binding(actingParameter = "requiredValue",
          path = "actingParameter.nested.xmlTag.@attribute")}) String parameterWithValues,
                                                         InputStream actingParameter) {}

  public void withFourBoundActingParametes(@OfValues(
      value = WithFourActingParametersValueProvider.class,
      bindings = {@Binding(actingParameter = "requiredValue", path = "actingParameter.field1"),
          @Binding(actingParameter = "anotherValue", path = "actingParameter.nested.field2"),
          @Binding(actingParameter = "someValue", path = "actingParameter.nested.field.3"),
          @Binding(actingParameter = "optionalValue", path = "actingParameter.anotherNested.field4")}) String parameterWithValues,
                                           InputStream actingParameter) {}

  public void withBoundActingParameterArray(@OfValues(
      value = WithArrayParameterValueProvider.class,
      bindings = {@Binding(actingParameter = "requiredValue", path = "actingParameter.jsonArray")}) String parameterWithValues,
                                            InputStream actingParameter) {}

  public void withPojoBoundActingParameter(@OfValues(
      value = WithPojoParameterValueProvider.class,
      bindings = {@Binding(actingParameter = "requiredValue", path = "actingParameter.pojoField")}) String parameterWithValues,
                                           InputStream actingParameter) {}

  public void withMapBoundActingParameter(@OfValues(
      value = WithMapParameterValueProvider.class,
      bindings = {@Binding(actingParameter = "requiredValue", path = "actingParameter.mapField")}) String parameterWithValues,
                                          InputStream actingParameter) {}

  // Test both defining pojo as an expression and in the dsl.
  public void withPojoFieldBoundActingParameterField(@OfValues(
      value = WithRequiredParameterValueProvider.class,
      bindings = {@Binding(actingParameter = "requiredValue", path = "actingParameter.pojoId")}) String parameterWithValues,
                                                     MyPojo actingParameter) {}

  public void withBoundActingParameterEnum(@OfValues(
      value = WithEnumParameterValueProvider.class,
      bindings = {@Binding(actingParameter = "requiredValue", path = "actingParameter.enumField")}) String parameterWithValues,
                                           InputStream actingParameter) {}

}
