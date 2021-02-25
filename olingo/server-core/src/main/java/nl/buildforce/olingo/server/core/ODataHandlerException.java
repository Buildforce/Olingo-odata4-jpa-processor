/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import nl.buildforce.olingo.server.api.ODataLibraryException;

/** Exception thrown during basic request handling. */
public class ODataHandlerException extends ODataLibraryException {
  //     private static final long serialVersionUID = -907752788975531134L;

  public enum MessageKeys implements MessageKey {
    /** parameters: HTTP method, HTTP method */
    AMBIGUOUS_XHTTP_METHOD,
    /** parameter: HTTP method */
    INVALID_HTTP_METHOD,
    /** parameter: HTTP method */
    HTTP_METHOD_NOT_ALLOWED,
    /** parameter: processor interface */
    PROCESSOR_NOT_IMPLEMENTED,
    /** no parameter */
    FUNCTIONALITY_NOT_IMPLEMENTED,
    /** no parameter */
    MISSING_CONTENT_TYPE,
    /** parameter: content type */
    UNSUPPORTED_CONTENT_TYPE,
    /** parameter: content type */
    INVALID_CONTENT_TYPE,
    /** parameter: version */
    ODATA_VERSION_NOT_SUPPORTED,
    /** parameter: prefer header */
    INVALID_PREFER_HEADER,
    /** invalid payload */
    INVALID_PAYLOAD;

    @Override
    public String getKey() {
      return name();
    }
  }

  public ODataHandlerException(String developmentMessage, MessageKey messageKey,
                               String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  public ODataHandlerException(String developmentMessage, Throwable cause, MessageKey messageKey,
                               String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

  @Override
  protected String getBundleName() {
    return DEFAULT_SERVER_BUNDLE_NAME;
  }

}