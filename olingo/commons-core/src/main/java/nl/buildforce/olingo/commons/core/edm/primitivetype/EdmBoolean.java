/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

/**
 * Implementation of the EDM primitive type Boolean.
 */
public final class EdmBoolean extends SingletonPrimitiveType {

  private static final EdmBoolean INSTANCE = new EdmBoolean();

  public static EdmBoolean getInstance() {
    return INSTANCE;
  }

  @Override
  public Class<?> getDefaultType() {
    return Boolean.class;
  }

  @Override
  public boolean validate(String value,
                          Boolean isNullable,
                          Integer maxLength,
                          Integer precision,
                          Integer scale,
                          Boolean isUnicode) {

    return value == null ? isNullable == null || isNullable : validateLiteral(value);
  }

  private static boolean validateLiteral(String value) {
    return "true".equals(value) || "false".equals(value);
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable,
                                        Integer maxLength,
                                        Integer precision,
                                        Integer scale,
                                        Boolean isUnicode,
                                        Class<T> returnType) throws EdmPrimitiveTypeException {

    if (validateLiteral(value)) {
      if (returnType.isAssignableFrom(Boolean.class)) {
        return returnType.cast("true".equals(value));
      } else {
        throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.");
      }
    } else {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.");
    }
  }

  @Override
  protected <T> String internalValueToString(T value,
                                             Boolean isNullable, Integer maxLength, Integer precision,
                                             Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {

    if (value instanceof Boolean) {
      return Boolean.toString((Boolean) value);
    } else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }
  }

}