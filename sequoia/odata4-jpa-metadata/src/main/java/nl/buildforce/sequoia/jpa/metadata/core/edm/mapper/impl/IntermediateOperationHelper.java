package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction.ReturnType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException.MessageKeys;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

import jakarta.persistence.EntityManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;

public class IntermediateOperationHelper {

  private IntermediateOperationHelper() {
// Must not create instances
  }

  static Constructor<?> determineConstructor(final Method javaFunction) throws ODataJPAModelException {
    Constructor<?> result = null;
    Constructor<?>[] constructors = javaFunction.getDeclaringClass().getConstructors();
    for (Constructor<?> constructor : constructors) {
      Parameter[] parameters = constructor.getParameters();
      if (parameters.length == 0)
        result = constructor;
      else if (parameters.length == 1 && parameters[0].getType() == EntityManager.class) {
        result = constructor;
        break;
      }
    }
    if (result == null)
      throw new ODataJPAModelException(ODataJPAModelException.MessageKeys.FUNC_CONSTRUCTOR_MISSING, javaFunction
          .getClass().getName());
    return result;
  }

  static boolean isCollection(Class<?> declaredReturnType) {
    for (Class<?> inter : declaredReturnType.getInterfaces()) {
      if (inter == Collection.class)
        return true;
    }
    return false;
  }

  static FullQualifiedName determineReturnType(final ReturnType definedReturnType, final Class<?> declaredReturnType,
      final IntermediateSchema schema, final String operationName) throws ODataJPAModelException {

    IntermediateStructuredType structuredType = schema.getStructuredType(declaredReturnType);
    if (structuredType != null)
      return structuredType.getExternalFQN();
    else {
      final IntermediateEnumerationType enumType = schema.getEnumerationType(declaredReturnType);
      if (enumType != null) {
        return enumType.getExternalFQN();
      } else if (declaredReturnType.equals(Blob.class) || declaredReturnType.equals(Clob.class)) {
        // The return type '%1$s' used at method '%3$s' is not supported
        throw new ODataJPAModelException(MessageKeys.FUNC_RETURN_NOT_SUPPORTED, declaredReturnType.getName(),
            operationName);
      } else {
        final EdmPrimitiveTypeKind edmType = JPATypeConverter.convertToEdmSimpleType(declaredReturnType);
        if (edmType == null)
          throw new ODataJPAModelException(MessageKeys.FUNC_RETURN_TYPE_INVALID, definedReturnType.type().getName(),
              declaredReturnType.getName(), operationName);
        return edmType.getFullQualifiedName();
      }
    }
  }

}