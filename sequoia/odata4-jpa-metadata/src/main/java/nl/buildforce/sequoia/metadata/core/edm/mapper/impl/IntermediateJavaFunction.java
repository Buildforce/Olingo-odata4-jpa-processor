package nl.buildforce.sequoia.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction.ReturnType;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunctionType;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAJavaFunction;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAOperationResultParameter;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAParameter;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlParameter;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlReturnType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

class IntermediateJavaFunction extends IntermediateFunction implements JPAJavaFunction {
  private final Method javaFunction;
  private final Constructor<?> javaConstructor;
  private List<JPAParameter> parameterList;

  IntermediateJavaFunction(final JPAEdmNameBuilder nameBuilder, final EdmFunction jpaFunction,
                           final Method javaFunction, final IntermediateSchema schema) throws ODataJPAModelException {

    super(nameBuilder, jpaFunction, schema,
            JPANameBuilder.buildFunctionName(jpaFunction).isEmpty() ? javaFunction.getName() : JPANameBuilder.buildFunctionName(jpaFunction));

    this.setExternalName(jpaFunction.name().isEmpty()
        ? nameBuilder.buildOperationName(internalName)
        : jpaFunction.name());
    this.javaFunction = javaFunction;
    this.javaConstructor = IntermediateOperationHelper.determineConstructor(javaFunction);
  }

  @Override
  public Constructor<?> getConstructor() {
    return javaConstructor;
  }

  @Override
  public EdmFunctionType getFunctionType() {
    return EdmFunctionType.JavaClass;
  }

  @Override
  public Method getMethod() {
    return javaFunction;
  }

  @Override
  public List<JPAParameter> getParameter() throws ODataJPAModelException {
    if (parameterList == null) {
      parameterList = new ArrayList<>();
      Class<?>[] types = javaFunction.getParameterTypes();
      Parameter[] declaredParameters = javaFunction.getParameters();
      for (int i = 0; i < declaredParameters.length; i++) {
        Parameter declaredParameter = declaredParameters[i];
        EdmParameter definedParameter = declaredParameter.getAnnotation(EdmParameter.class);
        if (definedParameter == null)
          // Function parameter %1$s of method %2$s at class %3$s without required annotation
          throw new ODataJPAModelException(ODataJPAModelException.MessageKeys.FUNC_PARAM_ANNOTATION_MISSING,
              declaredParameter.getName(), javaFunction.getName(), javaFunction
                  .getDeclaringClass().getName());
        JPAParameter parameter = new IntermediateFunctionParameter(definedParameter, nameBuilder
            .buildPropertyName(definedParameter.name()), declaredParameter.getName(), types[i]);
        parameterList.add(parameter);
      }
    }
    return parameterList;
  }

  @Override
  public JPAParameter getParameter(String internalName) throws ODataJPAModelException {
    for (JPAParameter parameter : getParameter()) {
      if (parameter.getInternalName().equals(internalName))
        return parameter;
    }
    return null;
  }

  @Override
  public JPAOperationResultParameter getResultParameter() {
    return new IntermediateOperationResultParameter(this, jpaFunction.returnType(), javaFunction.getReturnType(),
        IntermediateOperationHelper.isCollection(javaFunction.getReturnType()));
  }

  @Override
  public CsdlReturnType getReturnType() {
    return edmFunction.getReturnType();
  }

  @Override
  protected List<CsdlParameter> determineEdmInputParameter() throws ODataJPAModelException {
    List<CsdlParameter> parameters = new ArrayList<>();
    for (Parameter declaredParameter : javaFunction.getParameters()) {
      CsdlParameter parameter = new CsdlParameter();
      EdmParameter definedParameter = declaredParameter.getAnnotation(EdmParameter.class);
      parameter.setName(nameBuilder.buildPropertyName(definedParameter.name()));
      parameter.setType(determineParameterType(declaredParameter.getType(), definedParameter));
      parameters.add(parameter);
    }
    return parameters;
  }

  // TODO handle multiple schemas
  @Override
  protected CsdlReturnType determineEdmResultType(final ReturnType definedReturnType) throws ODataJPAModelException {
    final CsdlReturnType edmResultType = new CsdlReturnType();
    Class<?> declaredReturnType = javaFunction.getReturnType();

    if (IntermediateOperationHelper.isCollection(declaredReturnType)) {
      if (definedReturnType.type() == Object.class)
        // Type parameter expected for %1$s
        throw new ODataJPAModelException(ODataJPAModelException.MessageKeys.FUNC_RETURN_TYPE_EXP, javaFunction.getName());
      edmResultType.setCollection(true);
      edmResultType.setType(IntermediateOperationHelper.determineReturnType(definedReturnType, definedReturnType.type(),
          schema, javaFunction.getName()));
    } else {
      if (definedReturnType.type() != Object.class
          && !definedReturnType.type().getCanonicalName().equals(declaredReturnType.getCanonicalName()))
        // The return type %1$s from EdmFunction does not match type %2$s declared at method %3$s
        throw new ODataJPAModelException(ODataJPAModelException.MessageKeys.FUNC_RETURN_TYPE_INVALID, definedReturnType.type().getName(),
            declaredReturnType.getName(), javaFunction.getName());

      edmResultType.setCollection(false);
      edmResultType.setType(IntermediateOperationHelper.determineReturnType(definedReturnType, declaredReturnType,
          schema, javaFunction.getName()));
    }

    edmResultType.setNullable(definedReturnType.isNullable());
    if (definedReturnType.maxLength() >= 0)
      edmResultType.setMaxLength(definedReturnType.maxLength());
    if (definedReturnType.precision() >= 0)
      edmResultType.setPrecision(definedReturnType.precision());
    if (definedReturnType.scale() >= 0)
      edmResultType.setScale(definedReturnType.scale());
    /*definedReturnType.srid();
    if (!definedReturnType.srid().srid().isEmpty()) {
      final SRID srid = SRID.valueOf(definedReturnType.srid().srid());
      srid.setDimension(definedReturnType.srid().dimension());
      edmResultType.setSrid(srid);
    }*/

    return edmResultType;
  }

  @Override
  protected void lazyBuildEdmItem() throws ODataJPAModelException {
    super.lazyBuildEdmItem();
    edmFunction.setBound(false);
  }

  @Override
  boolean hasImport() {
    return true;
  }

  @Override
  protected FullQualifiedName determineParameterType(final Class<?> type,
      final EdmParameter definedParameter) throws ODataJPAModelException {
    final EdmPrimitiveTypeKind edmType = JPATypeConverter.convertToEdmSimpleType(type);
    if (edmType != null)
      return edmType.getFullQualifiedName();
    else {
      final IntermediateEnumerationType enumType = schema.getEnumerationType(type);
      if (enumType != null) {
        return enumType.getExternalFQN();
      } else
        throw new ODataJPAModelException(ODataJPAModelException.MessageKeys.FUNC_PARAM_ONLY_PRIMITIVE, javaFunction
            .getDeclaringClass().getName(), javaFunction.getName(), definedParameter.name());
    }
  }

}