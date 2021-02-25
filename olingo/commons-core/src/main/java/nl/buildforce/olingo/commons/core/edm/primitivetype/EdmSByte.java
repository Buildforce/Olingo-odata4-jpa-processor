/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import java.math.BigInteger;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

/**
 * Implementation of the EDM primitive type SByte.
 */
public final class EdmSByte extends SingletonPrimitiveType {

  private static final EdmSByte INSTANCE = new EdmSByte();

  public static EdmSByte getInstance() {
    return INSTANCE;
  }

  @Override
  public Class<?> getDefaultType() {
    return Byte.class;
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable, Integer maxLength, Integer precision,
                                        Integer scale, Boolean isUnicode, Class<T> returnType) throws EdmPrimitiveTypeException {

    byte valueByte;
    try {
      valueByte = Byte.parseByte(value);
    } catch (NumberFormatException e) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.", e);
    }

    try {
      return EdmInt64.convertNumber(valueByte, returnType);
    } catch (ClassCastException e) {
      throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.", e);
    }
  }

  @Override
  protected <T> String internalValueToString(T value,
                                             Boolean isNullable, Integer maxLength, Integer precision,
                                             Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {

    if (value instanceof Byte) {
      return value.toString();
    } else if (value instanceof Short || value instanceof Integer || value instanceof Long) {
      if (((Number) value).longValue() >= Byte.MIN_VALUE && ((Number) value).longValue() <= Byte.MAX_VALUE) {
        return value.toString();
      } else {
        throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
      }
    } else if (value instanceof BigInteger) {
      if (((BigInteger) value).bitLength() < Byte.SIZE) {
        return value.toString();
      } else {
        throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
      }
    } else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }
  }

}