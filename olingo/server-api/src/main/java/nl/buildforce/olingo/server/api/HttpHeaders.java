/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpHeader container for internal use in this package.
 * @see ODataRequest
 * @see ODataResponse
 */
final class HttpHeaders {
  private final Map<String, List<String>> headers = new LinkedHashMap<>();

  /**
   * Adds a header with given name and value.
   * If a header with that name already exists the value is added to this header.
   * @param name name of header
   * @param value value for header
   * @return this container (fluent interface)
   */
  public HttpHeaders addHeader(String name, String value) {
    String canonicalName = getCanonicalName(name);
    List<String> header = headers.get(canonicalName);
    if (header == null) {
      header = new ArrayList<>();
    }
    header.add(value);
    headers.put(canonicalName, header);
    return this;
  }

  /**
   * Adds a header with the given name and values.
   * If a header with that name already exists the values are added to this header.
   * @param name name of header
   * @param values values for header
   * @return this container (fluent interface)
   */
  public HttpHeaders addHeader(String name, List<String> values) {
    String canonicalName = getCanonicalName(name);
    List<String> header = headers.get(canonicalName);
    if (header == null) {
      header = new ArrayList<>();
    }
    header.addAll(values);
    headers.put(canonicalName, header);
    return this;
  }

  /**
   * Set a header with given name and value.
   * If a header with that name already exists the old header is replaced with the new one.
   * @param name name of header
   * @param value value for header
   * @return this container (fluent interface)
   */
  public HttpHeaders setHeader(String name, String value) {
    removeHeader(name);
    addHeader(name, value);
    return this;
  }

  /**
   * Gets header values for the given name.
   * @param name name of header requested
   * @return corresponding header values or null if no values have been found
   */
  public List<String> getHeader(String name) {
    List<String> values = headers.get(getCanonicalName(name));
    return values == null || values.isEmpty() ? null : Collections.unmodifiableList(values);
  }

  /**
   * Removes header of the given name.
   * @param name name of header to be removed
   * @return removed header values or null if no header was known for this name
   */
  public List<String> removeHeader(String name) {
    return headers.remove(getCanonicalName(name));
  }

  /**
   * Gets all headers with the according values.
   * @return an unmodifiable Map of header names/values or an empty collection if no headers have been set
   */
  public Map<String, List<String>> getHeaderToValues() {
    return headers.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(headers);
  }

  /**
   * Gets all header names.
   * @return all header names or an empty collection if no headers have been set
   */
  public Collection<String> getHeaderNames() {
    return headers.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(headers.keySet());
  }

  /**
   * The canonical form of a header name is the already-used form regarding case,
   * enabling applications to have pretty-looking headers instead of getting them
   * converted to all lowercase.
   * @param name HTTP header name
   */
  private String getCanonicalName(String name) {
    for (String headerName : headers.keySet()) {
      if (headerName.equalsIgnoreCase(name)) {
        return headerName;
      }
    }
    return name;
  }
}
