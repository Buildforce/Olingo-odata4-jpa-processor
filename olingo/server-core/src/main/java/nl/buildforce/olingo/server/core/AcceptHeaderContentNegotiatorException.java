/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

public class AcceptHeaderContentNegotiatorException extends ContentNegotiatorException {
  //     private static final long serialVersionUID = -8112658467394158700L;

  public enum MessageKeys implements MessageKey {
    /** parameter: list of content-type ranges */
    UNSUPPORTED_ACCEPT_TYPES,
    /** parameter: format string */
    UNSUPPORTED_FORMAT_OPTION,
    /** parameter: accept charset header*/
     UNSUPPORTED_ACCEPT_CHARSET_HEADER_OPTIONS,
     /** parameter: charset option in accept header*/
     UNSUPPORTED_ACCEPT_HEADER_CHARSET;
    
    @Override
    public String getKey() {
      return name();
    }
  }

  public AcceptHeaderContentNegotiatorException(String developmentMessage, MessageKey messageKey,
                                                String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }
  
  public AcceptHeaderContentNegotiatorException(String developmentMessage, Throwable cause,
                                                MessageKey messageKey,
                                                String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

  @Override
  protected String getBundleName() {
    return DEFAULT_SERVER_BUNDLE_NAME;
  }

}