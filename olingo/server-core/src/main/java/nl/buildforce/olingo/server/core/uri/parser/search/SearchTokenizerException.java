/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser.search;

import nl.buildforce.olingo.server.core.uri.parser.UriParserSyntaxException;

public class SearchTokenizerException extends UriParserSyntaxException {

  //     private static final long serialVersionUID = -8295456415309640166L;

  public enum MessageKeys implements MessageKey {
    /** parameter: character, TOKEN */
    FORBIDDEN_CHARACTER,
    /** parameter: TOKEN */
    NOT_EXPECTED_TOKEN,
    /** parameter: TOKEN */
    NOT_FINISHED_QUERY,
    /** parameter: TOKEN */
    INVALID_TOKEN_STATE,
    /** parameter: TOKEN */
    ALREADY_FINISHED;

    @Override
    public String getKey() {
      return name();
    }
  }

  public SearchTokenizerException(String developmentMessage, MessageKey messageKey,
                                  String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  public SearchTokenizerException(String developmentMessage, Throwable cause, MessageKey messageKey,
                                  String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

}