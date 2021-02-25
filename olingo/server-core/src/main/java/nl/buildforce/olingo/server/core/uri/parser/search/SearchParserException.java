/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser.search;

import nl.buildforce.olingo.server.core.uri.parser.UriParserSyntaxException;

public class SearchParserException extends UriParserSyntaxException {

  //     private static final long serialVersionUID = 5781553037561337795L;

  public enum MessageKeys implements MessageKey {
    NO_EXPRESSION_FOUND,
    /** parameter: message */
    TOKENIZER_EXCEPTION,
    /** parameter: operatorkind */
    INVALID_NOT_OPERAND,
    /** parameters: - */
    MISSING_CLOSE,
    /** parameters: expectedToken actualToken */
    EXPECTED_DIFFERENT_TOKEN,
    /** parameter: actual token */
    INVALID_END_OF_QUERY;

    @Override
    public String getKey() {
      return name();
    }
  }

  public SearchParserException(String developmentMessage, MessageKey messageKey,
                               String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  public SearchParserException(String developmentMessage, Throwable cause, MessageKey messageKey,
                               String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

}