/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import nl.buildforce.olingo.commons.api.http.HttpStatusCode;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Response object to carry OData-relevant HTTP information (status code, response headers, and content).
 */
public class ODataResponse {

  private int statusCode = HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode();
  private final HttpHeaders headers = new HttpHeaders();
  private InputStream content;

  /**
   * Sets the status code.
   * @see HttpStatusCode
   */
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  /**
   * Gets the status code.
   * @see HttpStatusCode
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * <p>Set a header to the response.</p>
   * <p>The header name will be handled as case-insensitive key.</p>
   * <p>If a header already exists then the header will be replaced by this new value.</p>
   * @param name case-insensitive header name
   * @param value value for the given header name
   * @see <a href="http://ietf.org/rfc/rfc7230.txt">RFC 7230, section 3.2.2</a>
   */
  public void setHeader(String name, String value) {
    headers.setHeader(name, value);
  }

  /**
   * <p>Adds a header to the response.</p>
   * <p>The header name will be handled as case-insensitive key.</p>
   * <p>If a header already exists then the list of values will just be extended.</p>
   * @param name case-insensitive header name
   * @param value value for the given header name
   * @see <a href="http://ietf.org/rfc/rfc7230.txt">RFC 7230, section 3.2.2</a>
   */
  public void addHeader(String name, String value) {
    headers.setHeader(name, value);
  }

  /**
   * <p>Adds a header to the response.</p>
   * <p>The header name will be handled as case-insensitive key.</p>
   * <p>If a header already exists then the list of values will just be extended.</p>
   * @param name case-insensitive header name
   * @param values list of values for the given header name
   * @see <a href="http://ietf.org/rfc/rfc7230.txt">RFC 7230, section 3.2.2</a>
   */
  public void addHeader(String name, List<String> values) {
    headers.addHeader(name, values);
  }

  /**
   * Get all headers with the according values.
   *
   * @return an unmodifiable Map of header names/values
   */
  public Map<String, List<String>> getAllHeaders() {
    return headers.getHeaderToValues();
  }

  /**
   * Gets header value for a given name.
   * @param name the header name as a case-insensitive key
   * @return the header value(s) or null if not found
   */
  public List<String> getHeaders(String name) {
    return headers.getHeader(name);
  }

  /**
   * Gets first header value for a given name.
   * If header name is not known <code>null</code> is returned.
   *
   * @param name the header name as a case-insensitive key
   * @return the first header value or null if not found
   */
  public String getHeader(String name) {
    List<String> values = getHeaders(name);
    return values == null || values.isEmpty() ? null : values.get(0);
  }

  /**
   * Sets the content (body).
   * @param content the content as {@link InputStream}
   */
  public void setContent(InputStream content) {
    this.content = content;
  }

  /**
   * Gets the content (body).
   * @return the content as {@link InputStream}
   */
  public InputStream getContent() {
    return content;
  }

  private ODataContent odataContent;

  public void setODataContent(ODataContent result) {
    odataContent = result;
  }

  public ODataContent getODataContent() {
    return odataContent;
  }
}
