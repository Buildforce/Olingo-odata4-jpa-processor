/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser;

import nl.buildforce.olingo.server.api.ODataLibraryException;

abstract public class UriParserException extends ODataLibraryException {

  //     private static final long serialVersionUID = -6438700016830955949L;

  public UriParserException(String developmentMessage, MessageKey messageKey, String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  public UriParserException(String developmentMessage, Throwable cause, MessageKey messageKey,
                            String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

  @Override
  protected String getBundleName() {
    return DEFAULT_SERVER_BUNDLE_NAME;
  }

}