/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.etag;

import java.util.Collection;

/**
 * Information about the values of an ETag-relevant HTTP header.
 */
public class ETagInformation {
  private final boolean all;
  private final Collection<String> eTags;

  public ETagInformation(boolean all, Collection<String> eTags) {
    this.all = all;
    this.eTags = eTags;
  }

  /**
   * Gets the information whether the values contain "*".
   */
  public boolean isAll() {
    return all;
  }

  /**
   * Gets the collection of ETag values found.
   * It is empty if {@link #isAll()} returns <code>true</code>.
   */
  public Collection<String> getETags() {
    return eTags;
  }

  /**
   * <p>Checks whether a given ETag value is matched by this ETag information,
   * using weak comparison as described in
   * <a href="https://www.ietf.org/rfc/rfc7232.txt">RFC 7232</a>, section 2.3.2.</p>
   * <p>If the given value is <code>null</code>, or if this ETag information
   * does not contain anything, the result is <code>false</code>.</p>
   * @param eTag the ETag value to match
   * @return a boolean match result
   */
  public boolean isMatchedBy(String eTag) {
    if (eTag == null) {
      return false;
    } else if (all) {
      return true;
    } else {
      for (String candidate : eTags) {
        if ((eTag.startsWith("W/") ? eTag.substring(2) : eTag)
            .equals(candidate.startsWith("W/") ? candidate.substring(2) : candidate)) {
          return true;
        }
      }
      return false;
    }
  }
}