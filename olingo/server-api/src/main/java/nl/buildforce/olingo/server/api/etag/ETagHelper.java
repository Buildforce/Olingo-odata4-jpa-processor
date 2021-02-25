/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.etag;

import java.util.Collection;

/**
 * Used for ETag-related tasks.
 */
public interface ETagHelper {
  /**
   * <p>Checks the preconditions of a read request with a given ETag value
   * against the If-Match and If-None-Match HTTP headers.</p>
   * <p>If the given ETag value is not matched by the ETag information in the If-Match headers,
   * and there are ETags in the headers to be matched, a "Precondition Failed" exception is
   * thrown.</p>
   * <p>If the given ETag value is matched by the ETag information in the If-None-Match headers,
   * <code>true</code> is returned, and applications are supposed to return an empty response
   * with a "Not Modified" status code and the ETag header, <code>false</code> otherwise.</p>
   * <p>All matching uses weak comparison as described in
   * <a href="https://www.ietf.org/rfc/rfc7232.txt">RFC 7232</a>, section 2.3.2.</p>
   * <p>This method does not nothing and returns <code>false</code> if the ETag value is
   * <code>null</code>.</p>
   * @param eTag the ETag value to match
   * @param ifMatchHeaders the If-Match header values
   * @param ifNoneMatchHeaders the If-None-Match header values
   * @return whether a "Not Modified" response should be used
   */
  boolean checkReadPreconditions(String eTag,
                                 Collection<String> ifMatchHeaders, Collection<String> ifNoneMatchHeaders)
          throws PreconditionException;

  /**
   * <p>Checks the preconditions of a change request (with HTTP methods PUT, PATCH, or DELETE)
   * with a given ETag value against the If-Match and If-None-Match HTTP headers.</p>
   * <p>If the given ETag value is not matched by the ETag information in the If-Match headers,
   * and there are ETags in the headers to be matched, or
   * if the given ETag value is matched by the ETag information in the If-None-Match headers,
   * a "Precondition Failed" exception is thrown.</p>
   * <p>All matching uses weak comparison as described in
   * <a href="https://www.ietf.org/rfc/rfc7232.txt">RFC 7232</a>, section 2.3.2.</p>
   * <p>This method does not nothing if the ETag value is <code>null</code>.</p>
   * @param eTag the ETag value to match
   * @param ifMatchHeaders the If-Match header values
   * @param ifNoneMatchHeaders the If-None-Match header values
   */
  void checkChangePreconditions(String eTag,
                                Collection<String> ifMatchHeaders, Collection<String> ifNoneMatchHeaders)
          throws PreconditionException;
}