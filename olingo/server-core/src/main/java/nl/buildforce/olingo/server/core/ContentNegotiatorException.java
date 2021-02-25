/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import nl.buildforce.olingo.server.api.ODataLibraryException;

public class ContentNegotiatorException extends ODataLibraryException {
  //     private static final long serialVersionUID = -8112658467394158700L;

  public enum MessageKeys implements MessageKey {
    /** parameter: list of content-type ranges */
    UNSUPPORTED_ACCEPT_TYPES,
    /** parameter: content type */
    UNSUPPORTED_CONTENT_TYPE,
    /** no parameter */
    NO_CONTENT_TYPE_SUPPORTED,
    /** parameter: format string */
    UNSUPPORTED_FORMAT_OPTION,
    /**parameter: accept charset */
    UNSUPPORTED_ACCEPT_CHARSET,
    /** parameter: accept header charset */
    UNSUPPORTED_ACCEPT_HEADER_CHARSET;

    @Override
    public String getKey() {
      return name();
    }
  }

  public ContentNegotiatorException(String developmentMessage, MessageKey messageKey,
                                    String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  public ContentNegotiatorException(String developmentMessage, Throwable cause,
                                    MessageKey messageKey,
                                    String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

  @Override
  protected String getBundleName() {
    return DEFAULT_SERVER_BUNDLE_NAME;
  }

}