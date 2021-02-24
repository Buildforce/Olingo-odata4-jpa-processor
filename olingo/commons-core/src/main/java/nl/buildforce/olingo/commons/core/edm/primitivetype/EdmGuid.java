package nl.buildforce.olingo.commons.core.edm.primitivetype;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Implementation of the EDM primitive type Guid.
 */
public final class EdmGuid extends SingletonPrimitiveType {

  private static final EdmGuid INSTANCE = new EdmGuid();

  public static EdmGuid getInstance() {
    return INSTANCE;
  }

  @Override
  public Class<?> getDefaultType() {
    return UUID.class;
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

  private boolean validateLiteral(String value) {
    final String PATTERN = "\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}";

    return value.matches(PATTERN);
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable,
                                        Integer maxLength,
                                        Integer precision,
                                        Integer scale,
                                        Boolean isUnicode,
                                        Class<T> returnType) throws EdmPrimitiveTypeException {
    try {
      UUID valueUUID = UUID.fromString(value);

      if (returnType == String.class) {
        return returnType.cast(value);
      } else if (returnType.isAssignableFrom(UUID.class)) {
        return returnType.cast(valueUUID);
      } else if (returnType == Byte[].class || returnType == byte[].class) {
        byte[] buffer = new byte[16];
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.putLong(valueUUID.getMostSignificantBits());
        bb.putLong(valueUUID.getLeastSignificantBits());
        return (returnType == Byte[].class) ? returnType.cast(ArrayUtils.toObject(buffer)) : returnType.cast(buffer);
      }
      throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.");
    } catch (IllegalArgumentException e) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.", e);
    }
  }

  @Override
  protected <T> String internalValueToString(T value,
                                             Boolean isNullable,
                                             Integer maxLength,
                                             Integer precision,
                                             Integer scale,
                                             Boolean isUnicode) throws EdmPrimitiveTypeException {

    if (value instanceof UUID) {
      return value.toString();
    } else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }
  }

}