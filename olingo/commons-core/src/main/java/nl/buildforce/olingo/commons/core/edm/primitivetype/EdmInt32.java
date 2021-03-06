/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import java.math.BigInteger;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

/**
 * Implementation of the EDM primitive type Int32.
 */
public final class EdmInt32 extends SingletonPrimitiveType {

  private static final EdmInt32 INSTANCE = new EdmInt32();

  public static EdmInt32 getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean isCompatible(EdmPrimitiveType primitiveType) {
    return primitiveType instanceof EdmByte
        || primitiveType instanceof EdmSByte
        || primitiveType instanceof EdmInt16
        || primitiveType instanceof EdmInt32;
  }

  @Override
  public Class<?> getDefaultType() {
    return Integer.class;
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable, Integer maxLength, Integer precision,
                                        Integer scale, Boolean isUnicode, Class<T> returnType) throws EdmPrimitiveTypeException {

    int valueInteger;
    try {
      valueInteger = Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.", e);
    }

    try {
      return EdmInt64.convertNumber(valueInteger, returnType);
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

    if (value instanceof Byte || value instanceof Short || value instanceof Integer) {
      return value.toString();
    } else if (value instanceof Long) {
      if ((Long) value >= Integer.MIN_VALUE && (Long) value <= Integer.MAX_VALUE) {
        return value.toString();
      } else {
        throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
      }
    } else if (value instanceof BigInteger) {
      if (((BigInteger) value).bitLength() < Integer.SIZE) {
        return value.toString();
      } else {
        throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
      }
    } else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }
  }
}
