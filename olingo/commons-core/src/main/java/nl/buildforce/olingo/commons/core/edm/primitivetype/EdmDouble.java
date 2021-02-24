package nl.buildforce.olingo.commons.core.edm.primitivetype;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

import java.math.BigDecimal;

/**
 * Implementation of the EDM primitive type Double.
 */
public final class EdmDouble extends SingletonPrimitiveType {

  protected static final String NEGATIVE_INFINITY = "-INF";
  protected static final String POSITIVE_INFINITY = "INF";
  protected static final String NaN = "NaN";

/*  private static final Pattern PATTERN = Pattern.compile(
      "[+-]?\\p{Digit}+(?:\\.\\p{Digit}+)?(?:[Ee][+-]?\\p{Digit}{1,3})?");
*/

  private static final EdmDouble INSTANCE = new EdmDouble();

  public static EdmDouble getInstance() { return INSTANCE; }

  @Override
  public boolean isCompatible(EdmPrimitiveType primitiveType) {
    return primitiveType instanceof EdmByte
        || primitiveType instanceof EdmSByte
        || primitiveType instanceof EdmInt16
        || primitiveType instanceof EdmInt32
        || primitiveType instanceof EdmInt64
        || primitiveType instanceof EdmSingle
        || primitiveType instanceof EdmDouble;
  }

  @Override
  public Class<?> getDefaultType() {
    return Double.class;
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable, Integer maxLength, Integer precision,
                                        Integer scale,
                                        Boolean isUnicode,
                                        Class<T> returnType) throws EdmPrimitiveTypeException {

    try {
      double result = switch (value) {
        case NEGATIVE_INFINITY -> Double.NEGATIVE_INFINITY;
        case POSITIVE_INFINITY -> Double.POSITIVE_INFINITY;
        case NaN -> Double.NaN;
        default -> Double.parseDouble(value);
      };

      if (returnType.isAssignableFrom(Double.class)) return returnType.cast(result);
      else if (Double.isInfinite(result) || Double.isNaN(result))
        if (returnType.isAssignableFrom(Float.class)) return returnType.cast((float) result);
        else throw new EdmPrimitiveTypeException(
                "The literal '" + value + "' cannot be converted to value type " + returnType + ".");
      else return EdmDecimal.convertDecimal(new BigDecimal(value), returnType);
    } catch (NumberFormatException e) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.");
    } catch (IllegalArgumentException e) {
      throw new EdmPrimitiveTypeException(
              "The literal '" + value + "' cannot be converted to value type " + returnType + ".", e);
    } catch (ClassCastException e) {
      throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.", e);
    }
  }

  @Override
  protected <T> String internalValueToString(T value,
                                             Boolean isNullable, Integer maxLength, Integer precision,
                                             Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {
    if (value instanceof Long) {
      if (Math.abs((long) value) < 1L << 51) return value.toString();
      else throw new EdmPrimitiveTypeException("The value '" + value + "' is not valid.");
    } else if (value instanceof Integer || value instanceof Short || value instanceof Byte) return value.toString();
    else if (value instanceof Double) {
      return (Double) value == Double.NEGATIVE_INFINITY ? NEGATIVE_INFINITY
          : (Double) value == Double.POSITIVE_INFINITY ? POSITIVE_INFINITY : value.toString();
    } else if (value instanceof Float) {
      return (Float) value == Float.NEGATIVE_INFINITY ? NEGATIVE_INFINITY
          : (Float) value == Float.POSITIVE_INFINITY ? POSITIVE_INFINITY : value.toString();
    } else if (value instanceof BigDecimal) {
      double doubleValue = ((BigDecimal) value).doubleValue();
      if (!Double.isInfinite(doubleValue) && BigDecimal.valueOf(doubleValue).compareTo((BigDecimal) value) == 0)
        return value.toString();
      else throw new EdmPrimitiveTypeException("The value '" + value + "' is found not valid.");
    } else throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
  }
}