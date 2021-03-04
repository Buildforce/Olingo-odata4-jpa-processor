/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Encodes a Java String (in its internal UTF-16 encoding) into its
 * percent-encoded UTF-8 representation according to
 * <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>
 * (with consideration of its predecessor RFC 2396).
 *
 */
public class UrlEncoder {

  /**
   * Encodes a Java String (in its internal UTF-16 encoding) into its
   * percent-encoded UTF-8 representation according to
   * <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>,
   * suitable for parts of an OData path segment.
   * @param value the Java String
   * @return the encoded String

   */

  public static String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8).replaceAll("%27", "'");
  }

  // OData has special handling for "'", so we allow that to remain unencoded.
  // Other sub-delims not used neither by JAX-RS nor by OData could be added
  // if the encoding is considered to be too aggressive.
  // RFC 3986 would also allow the gen-delims ":" and "@" to appear literally
  // in path-segment parts.
  // private static final String ODATA_UNENCODED = "'";

  // private static final UrlEncoder encoder = new UrlEncoder(ODATA_UNENCODED);

  /** characters to remain unencoded in addition to {@link #UNRESERVED}
  private final String unencoded;

  private UrlEncoder(String unencoded) {
    this.unencoded = unencoded == null ? "" : unencoded;
  }
 */
  /**
   * <p>Returns the percent-encoded UTF-8 representation of a String.</p>
   * <p>In order to avoid producing percent-encoded CESU-8 (as described in
   * the Unicode Consortium's <a href="http://www.unicode.org/reports/tr26/">
   * Technical Report #26</a>), this is done in two steps:
   * <ol>
   * <li>Re-encode the characters from their Java-internal UTF-16 representations
   * into their UTF-8 representations.</li>
   * <li>Percent-encode each of the bytes in the UTF-8 representation.
   * This is possible on byte level because all characters that do not have
   * a <code>%xx</code> representation are represented in one byte in UTF-8.</li>
   * </ol></p>
   * @param input input String
   * @return encoded representation
   */
  /* private String encodeInternal(String input) {
    StringBuilder resultStr = new StringBuilder();

    for (byte utf8Byte : input.getBytes(StandardCharsets.UTF_8)) {
      char character = (char) utf8Byte;
      if (isUnreserved(character)) {
        resultStr.append(character);
      } else if (isUnencoded(character)) {
        resultStr.append(character);
      } else if (utf8Byte >= 0) {
        resultStr.append(hex[utf8Byte]);
      } else {
        // case UTF-8 continuation byte
        resultStr.append(hex[256 + utf8Byte]); // index adjusted for the usage of signed bytes
      }
    }
    return resultStr.toString();
  }

  private static boolean isUnreserved(char character) {
    return 'A' <= character && character <= 'Z' // case A..Z
        || 'a' <= character && character <= 'z' // case a..z
        || '0' <= character && character <= '9' // case 0..9
        || UNRESERVED.indexOf(character) >= 0;
  }

  private boolean isUnencoded(char character) {
    return unencoded.indexOf(character) >= 0;
  }*/

}