/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

/**
 * Implementation of the EDM primitive type Single.
 */
public final class EdmSingle extends SingletonPrimitiveType {

  private static final Pattern PATTERN = Pattern.compile(
      "[+-]?\\p{Digit}{1,9}(?:\\.\\p{Digit}{1,9})?(?:[Ee][+-]?\\p{Digit}{1,2})?");

  private static final EdmSingle INSTANCE = new EdmSingle();

  public static EdmSingle getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean isCompatible(EdmPrimitiveType primitiveType) {
    return primitiveType instanceof EdmByte
        || primitiveType instanceof EdmSByte
        || primitiveType instanceof EdmInt16
        || primitiveType instanceof EdmInt32
        || primitiveType instanceof EdmInt64
        || primitiveType instanceof EdmSingle;
  }

  @Override
  public Class<?> getDefaultType() {
    return Float.class;
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable, Integer maxLength, Integer precision,
                                        Integer scale, Boolean isUnicode, Class<T> returnType) throws EdmPrimitiveTypeException {

    float result;
    BigDecimal bigDecimalValue = null;
    // Handle special values first.
    switch (value) {
      case EdmDouble.NEGATIVE_INFINITY -> result = Float.NEGATIVE_INFINITY;
      case EdmDouble.POSITIVE_INFINITY -> result = Float.POSITIVE_INFINITY;
      case EdmDouble.NaN -> result = Float.NaN;
      default -> {
        // Now only "normal" numbers remain.
        if (!PATTERN.matcher(value).matches()) {
          throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.");
        }

        // The number format is checked above, so we don't have to catch NumberFormatException.
        bigDecimalValue = new BigDecimal(value);
        result = bigDecimalValue.floatValue();
        // "binary32" infinite values have been treated already above, so we can throw an exception
        // if the conversion to a float results in an infinite value.
        if (Float.isInfinite(result) /*|| bigDecimalValue.compareTo(new BigDecimal(result.toString())) != 0*/) {
          throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.");
        }
      }
    }

    if (returnType.isAssignableFrom(Float.class)) {
      return returnType.cast(result);
    } else if (Float.isInfinite(result) || Float.isNaN(result)) {
      if (returnType.isAssignableFrom(Double.class)) {
        return returnType.cast((double) result);
      } else {
        throw new EdmPrimitiveTypeException("The literal '" + value
            + "' cannot be converted to value type " + returnType + ".");
      }
    } else {
      try {
        return EdmDecimal.convertDecimal(bigDecimalValue, returnType);
      } catch (IllegalArgumentException e) {
        throw new EdmPrimitiveTypeException("The literal '" + value
            + "' cannot be converted to value type " + returnType + ".", e);
      } catch (ClassCastException e) {
        throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.", e);
      }
    }
  }

  @Override
  protected <T> String internalValueToString(T value,
                                             Boolean isNullable, Integer maxLength, Integer precision,
                                             Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {

    if (value instanceof Long || value instanceof Integer) {
      if (Math.abs(((Number) value).longValue()) < 1L << 22) {
        return value.toString();
      } else {
        throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
      }
    } else if (value instanceof Short || value instanceof Byte) {
      return value.toString();
    } else if (value instanceof Double) {
      if (((Double) value).isInfinite()) {
        return (Double) value == Double.NEGATIVE_INFINITY ? EdmDouble.NEGATIVE_INFINITY : EdmDouble.POSITIVE_INFINITY;
      } else {
        String floatString = Float.toString(((Double) value).floatValue());
        if (floatString.equals(value.toString())) {
          return floatString;
        } else {
          throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
        }
      }
    } else if (value instanceof Float) {
      return (Float) value == Float.NEGATIVE_INFINITY ? EdmDouble.NEGATIVE_INFINITY
          : (Float) value == Float.POSITIVE_INFINITY ? EdmDouble.POSITIVE_INFINITY : value.toString();
    } else if (value instanceof BigDecimal) {
      float floatValue = ((BigDecimal) value).floatValue();
      if (!Float.isInfinite(floatValue) && BigDecimal.valueOf(floatValue).compareTo((BigDecimal) value) == 0) {
        return value.toString();
      } else {
        throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
      }
    } else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }
  }

}