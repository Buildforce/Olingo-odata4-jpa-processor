/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import java.math.BigInteger;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

/**
 * Implementation of the EDM primitive type Byte.
 */
public final class EdmByte extends SingletonPrimitiveType {

  private static final EdmByte INSTANCE = new EdmByte();

  public static EdmByte getInstance() {
    return INSTANCE;
  }

  @Override
  public Class<?> getDefaultType() {
    return Short.class;
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable, Integer maxLength, Integer precision,
                                        Integer scale, Boolean isUnicode, Class<T> returnType) throws EdmPrimitiveTypeException {

    short valueShort;
    try {
      valueShort = Short.parseShort(value);
    } catch (NumberFormatException e) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.", e);
    }
    if (valueShort < 0 || valueShort >= 1 << Byte.SIZE) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.");
    }

    try {
      return EdmInt64.convertNumber(valueShort, returnType);
    } catch (IllegalArgumentException e) {
      throw new EdmPrimitiveTypeException("The literal '" + value
          + "' cannot be converted to value type " + returnType + ".", e);
    } catch (ClassCastException e) {
      throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.", e);
    }
  }

  @Override
  protected <T> String internalValueToString(T value,
                                             Boolean isNullable, Integer maxLength, Integer precision,
                                             Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {

    if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
      if (((Number) value).longValue() >= 0 && ((Number) value).longValue() < 1 << Byte.SIZE) {
        return value.toString();
      } else {
        throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
      }
    } else if (value instanceof BigInteger) {
      if (((BigInteger) value).compareTo(BigInteger.ZERO) >= 0
          && ((BigInteger) value).compareTo(BigInteger.valueOf(1 << Byte.SIZE)) < 0) {
        return value.toString();
      } else {
        throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
      }
    } else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }
  }
}
