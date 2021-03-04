/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException.MessageKeys;

public class HttpRequestStatusLine {
  private static final Pattern PATTERN_RELATIVE_URI = Pattern.compile("([^/][^?]*)(?:\\?(.*))?");

  private static final Set<HttpMethod> HTTP_CHANGE_SET_METHODS = new HashSet<>(Arrays.asList(
          HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH));
  private static final String HTTP_VERSION = "HTTP/1.1";

  final private Line statusLine;
  final private String requestBaseUri;

  private HttpMethod method;
  private String httpVersion;
  private final String rawServiceResolutionUri;
  private String rawQueryPath;
  private String rawODataPath;
  private String rawBaseUri;
  private String rawRequestUri;

  public HttpRequestStatusLine(Line httpStatusLine, String baseUri, String serviceResolutionUri)
      throws BatchDeserializerException {
    statusLine = httpStatusLine;
    requestBaseUri = baseUri;
    rawServiceResolutionUri = serviceResolutionUri;

    parse();
  }

  private void parse() throws BatchDeserializerException {
    String[] parts = statusLine.toString().split(" ");

    //Status line consists of 3 parts: Method, URI and HTTP Version
    if (parts.length == 3) {
      method = parseMethod(parts[0]);
      parseUri(parts[1], requestBaseUri);
      httpVersion = parseHttpVersion(parts[2]);
    } else {
      throw new BatchDeserializerException("Invalid status line", MessageKeys.INVALID_STATUS_LINE,
          Integer.toString(statusLine.getLineNumber()));
    }
  }

  private void parseUri(String rawUri, String baseUri) throws BatchDeserializerException {
    try {
      URI uri = new URI(rawUri);

      if (uri.isAbsolute()) {
        parseAbsoluteUri(rawUri, baseUri);
      } else {
        URI base = URI.create(baseUri);
        if (rawUri.startsWith(base.getRawPath())) {
          parseRelativeUri(removeLeadingSlash(rawUri.substring(base.getRawPath().length())));
        } else {
          parseRelativeUri(rawUri);
        }
      }
    } catch (URISyntaxException e) {
      throw new BatchDeserializerException("Malformed uri", e, MessageKeys.INVALID_URI,
          Integer.toString(statusLine.getLineNumber()));
    }
  }

  private void parseAbsoluteUri(String rawUri, String baseUri) throws BatchDeserializerException {
    if (rawUri.startsWith(baseUri)) {
      String relativeUri = removeLeadingSlash(rawUri.substring(baseUri.length()));
      parseRelativeUri(relativeUri);
    } else {
      throw new BatchDeserializerException("Base uri does not match", MessageKeys.INVALID_BASE_URI,
          Integer.toString(statusLine.getLineNumber()));
    }
  }

  private String removeLeadingSlash(String value) {
    return (value.length() > 0 && value.charAt(0) == '/') ? value.substring(1) : value;
  }

  private void parseRelativeUri(String rawUri) throws BatchDeserializerException {
    Matcher relativeUriMatcher = PATTERN_RELATIVE_URI.matcher(rawUri);

    if (relativeUriMatcher.matches()) {
      buildUri(relativeUriMatcher.group(1), relativeUriMatcher.group(2));
    } else {
      throw new BatchDeserializerException("Malformed uri", MessageKeys.INVALID_URI,
          Integer.toString(statusLine.getLineNumber()));
    }
  }

  private void buildUri(String oDataPath, String queryOptions) {
    rawBaseUri = requestBaseUri;
    rawODataPath = "/" + oDataPath;
    rawRequestUri = requestBaseUri + rawODataPath;

    if (queryOptions != null) {
      rawRequestUri += "?" + queryOptions;
      rawQueryPath = queryOptions;
    } else {
      rawQueryPath = "";
    }
  }

  private HttpMethod parseMethod(String method) throws BatchDeserializerException {
    try {
      return HttpMethod.valueOf(method.trim());
    } catch (IllegalArgumentException e) {
      throw new BatchDeserializerException("Illegal http method", e, MessageKeys.INVALID_METHOD,
          Integer.toString(statusLine.getLineNumber()));
    }
  }

  private String parseHttpVersion(String httpVersion) throws BatchDeserializerException {
    if (!HTTP_VERSION.equals(httpVersion.trim())) {
      throw new BatchDeserializerException("Invalid http version", MessageKeys.INVALID_HTTP_VERSION,
          Integer.toString(statusLine.getLineNumber()));
    } else {
      return HTTP_VERSION;
    }
  }

  public void validateHttpMethod(boolean isChangeSet) throws BatchDeserializerException {
    if (isChangeSet && !HTTP_CHANGE_SET_METHODS.contains(getMethod())) {
      throw new BatchDeserializerException("Invalid change set method", MessageKeys.INVALID_CHANGESET_METHOD,
          Integer.toString(statusLine.getLineNumber()));
    }
  }

  public HttpMethod getMethod() {
    return method;
  }

  public String getHttpVersion() {
    return httpVersion;
  }

  public int getLineNumber() {
    return statusLine.getLineNumber();
  }

  public String getRequestBaseUri() {
    return requestBaseUri;
  }

  public String getRawServiceResolutionUri() {
    return rawServiceResolutionUri;
  }

  public String getRawQueryPath() {
    return rawQueryPath;
  }

  public String getRawODataPath() {
    return rawODataPath;
  }

  public String getRawBaseUri() {
    return rawBaseUri;
  }

  public String getRawRequestUri() {
    return rawRequestUri;
  }

}