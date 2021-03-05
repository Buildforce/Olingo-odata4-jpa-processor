/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Implementation of the EDM primitive type Binary.
 */
public class EdmBinary extends SingletonPrimitiveType {

  private static final Charset UTF_8 = StandardCharsets.UTF_8;

  /**
   * Byte used to pad output.
   *
   * <b>NOTE</b>: this is provided here from Commons Codec for Android compatibility.
   */
  private static final byte PAD_DEFAULT = '=';


  private static final EdmBinary INSTANCE = new EdmBinary();

  {
    uriPrefix = "binary'";
    uriSuffix = "'";
  }

  public static EdmBinary getInstance() {
    return INSTANCE;
  }

  @Override
  public Class<?> getDefaultType() {
    return byte[].class;
  }

  /**
   * Checks if a byte value is whitespace or not. Whitespace is taken to mean: space, tab, CR, LF
   * <br/>
   * <b>NOTE</b>: this method is provided here from Commons Codec for Android compatibility.
   *
   * @param byteToCheck the byte to check
   * @return true if byte is whitespace, false otherwise
   */
  private static boolean isWhiteSpace(byte byteToCheck) {
    return switch (byteToCheck) {
      case ' ', '\n', '\r', '\t' -> true;
      default -> false;
    };
  }

  /**
   * Returns whether or not the <code>octet</code> is in the base 64 alphabet.
   * <br/>
   * <b>NOTE</b>: this method is provided here from Commons Codec for Android compatibility.
   *
   * @param octet The value to test
   * @return {@code true} if the value is defined in the the base 64 alphabet, {@code false} otherwise.
   * @since 1.4
   */
  private static boolean isBase64(byte octet) {
    /**
     * This array is a lookup table that translates Unicode characters drawn from the "Base64 Alphabet" (as specified in
     * Table 1 of RFC 2045) into their 6-bit positive integer equivalents. Characters that are not in the Base64 alphabet
     * but fall within the bounds of the array are translated to -1.
     *
     * Note: '+' and '-' both decode to 62. '/' and '_' both decode to 63. This means decoder seamlessly handles both
     * URL_SAFE and STANDARD base64. (The encoder, on the other hand, needs to know ahead of time what to emit).
     *
     * Thanks to "commons" project in ws.apache.org for this code.
     * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
     *
     * <b>NOTE</b>: this is provided here from Commons Codec for Android compatibility.
     */
    final byte[] DECODE_TABLE = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54,
            55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
            5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
            24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34,
            35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51
    };

    return octet == PAD_DEFAULT || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1);
  }

  /**
   * Tests a given byte array to see if it contains only valid characters within the Base64 alphabet. Currently the
   * method treats whitespace as valid.
   * <br/>
   * <b>NOTE</b>: this method is provided here from Commons Codec for Android compatibility.
   *
   * @param arrayOctet byte array to test
   * @return {@code true} if all bytes are valid characters in the Base64 alphabet or if the byte array is empty;
   * {@code false}, otherwise
   */
  private static boolean isBase64(byte[] arrayOctet) {
    for (byte b : arrayOctet) if (!isBase64(b) && !isWhiteSpace(b)) return false;
    return true;
  }

  @Override
  public boolean validate(String value,
                          Boolean isNullable, Integer maxLength, Integer precision,
                          Integer scale, Boolean isUnicode) {

    return value == null ?
        isNullable == null || isNullable :
        isBase64(value.getBytes(UTF_8)) && validateMaxLength(value, maxLength);
  }

  private static boolean validateMaxLength(String value, Integer maxLength) {
    // Every three bytes are represented as four base-64 characters.
    // Additionally, there could be up to two padding "=" characters
    // if the number of bytes is not a multiple of three,
    // and there could be line feeds, possibly with carriage returns.
    return maxLength == null || maxLength >= (value.length() - lineEndingsLength(value)) * 3 / 4
            - (value.endsWith("==") ? 2 : value.endsWith("=") ? 1 : 0);
  }

  private static int lineEndingsLength(String value) {
    int result = 0;
    int index = 0;
    while ((index = value.indexOf('\n', index)) >= 0) {
      result += index > 0 && value.charAt(index - 1) == '\r' ? 2 : 1;
      index++;
    }
    return result;
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable, Integer maxLength, Integer precision,
                                        Integer scale, Boolean isUnicode, Class<T> returnType) throws EdmPrimitiveTypeException {

    if (value == null || !isBase64(value.getBytes(UTF_8))) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' has illegal content.");
    }
    if (!validateMaxLength(value, maxLength)) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' does not match the facets' constraints.");
    }

    byte[] result = Base64.decodeBase64(value.getBytes(UTF_8));

    if (returnType.isAssignableFrom(byte[].class)) {
      return returnType.cast(result);
    } else if (returnType.isAssignableFrom(Byte[].class)) {
      Byte[] byteArray = ArrayUtils.toObject(result);
      return returnType.cast(byteArray);
    } else {
      throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.");
    }
  }

  @Override
  protected <T> String internalValueToString(T value,
                                             Boolean isNullable, Integer maxLength, Integer precision,
                                             Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {

    byte[] byteArrayValue;
    if (value instanceof byte[]) { byteArrayValue = (byte[]) value; }
    else if (value instanceof Byte[]) { byteArrayValue =  ArrayUtils.toPrimitive((Byte[]) value); }
    else {
      throw new EdmPrimitiveTypeException("The value type " + value.getClass() + " is not supported.");
    }

    if (maxLength != null && byteArrayValue.length > maxLength) {
      throw new EdmPrimitiveTypeException("The value '" + value + "' does not match the facets' constraints.");
    }

    return new String(Base64.encodeBase64(byteArrayValue, false), UTF_8);
  }

}