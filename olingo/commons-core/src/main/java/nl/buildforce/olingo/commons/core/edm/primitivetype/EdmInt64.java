/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import java.math.BigInteger;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

/**
 * Implementation of the EDM primitive type Int64.
 */
public final class EdmInt64 extends SingletonPrimitiveType {

  private static final EdmInt64 INSTANCE = new EdmInt64();

  public static EdmInt64 getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean isCompatible(EdmPrimitiveType primitiveType) {
    return primitiveType instanceof EdmByte
        || primitiveType instanceof EdmSByte
        || primitiveType instanceof EdmInt16
        || primitiveType instanceof EdmInt32
        || primitiveType instanceof EdmInt64;
  }

  @Override
  public Class<?> getDefaultType() {
    return Long.class;
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable, Integer maxLength, Integer precision,
                                        Integer scale, Boolean isUnicode, Class<T> returnType) throws EdmPrimitiveTypeException {

    long valueLong;
    try {
      valueLong = Long.parseLong(value);
    } catch (NumberFormatException e) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.", e);
    }

    try {
      return convertNumber(valueLong, returnType);
    } catch (IllegalArgumentException e) {
      throw new EdmPrimitiveTypeException("The literal '" + value
          + "' cannot be converted to value type " + returnType + ".", e);
    } catch (ClassCastException e) {
      throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.", e);
    }
  }

  /**
   * Converts a whole {@link Number} value into the requested return type if possible.
   *
   * @param value the value
   * @param returnType the class of the returned value; it must be one of {@link BigInteger}, {@link Long},
   * {@link Integer}, {@link Short}, or {@link Byte}
   * @return the converted value
   * @throws IllegalArgumentException if the conversion is not possible
   * @throws ClassCastException if the return type is not allowed
   */
  public static <T> T convertNumber(Number value, Class<T> returnType)
      throws IllegalArgumentException, ClassCastException {

    if (returnType.isAssignableFrom(Long.class)) {
      return returnType.cast(value.longValue());
    } else if (returnType.isAssignableFrom(BigInteger.class)) {
      return returnType.cast(BigInteger.valueOf(value.longValue()));
    } else if (returnType.isAssignableFrom(Byte.class)) {
      if (value.longValue() >= Byte.MIN_VALUE && value.longValue() <= Byte.MAX_VALUE) {
        return returnType.cast(value.byteValue());
      } else {
        throw new IllegalArgumentException();
      }
    } else if (returnType.isAssignableFrom(Short.class)) {
      if (value.longValue() >= Short.MIN_VALUE && value.longValue() <= Short.MAX_VALUE) {
        return returnType.cast(value.shortValue());
      } else {
        throw new IllegalArgumentException();
      }
    } else if (returnType.isAssignableFrom(Integer.class)) {
      if (value.longValue() >= Integer.MIN_VALUE && value.longValue() <= Integer.MAX_VALUE) {
        return returnType.cast(value.intValue());
      } else {
        throw new IllegalArgumentException();
      }
    } else {
      throw new ClassCastException("unsupported return type " + returnType.getSimpleName());
    }
  }

  @Override
  protected <T> String internalValueToString(T value,
                                             Boolean isNullable, Integer maxLength, Integer precision,
                                             Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {

    if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long) {
      return value.toString();
    } else if (value instanceof BigInteger) {
      if (((BigInteger) value).bitLength() < Long.SIZE) {
        return value.toString();
      } else {
        throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
      }
    } else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }
  }
}
