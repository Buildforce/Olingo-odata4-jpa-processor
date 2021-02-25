/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser;

/** Exception thrown during URI parsing in cases where the URI violates the URI construction rules. */
public class UriParserSyntaxException extends UriParserException {

  //     private static final long serialVersionUID = 5887744747812478226L;

  public enum MessageKeys implements MessageKey {
    /** parameter: segment */
    MUST_BE_LAST_SEGMENT,
    /** parameter: query-option name */
    UNKNOWN_SYSTEM_QUERY_OPTION,
    /** parameter: query-option name */
    DOUBLE_SYSTEM_QUERY_OPTION,
    /** parameters: query-option name, query-option value */
    WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION,
    /** parameter: $format option value */
    WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION_FORMAT,
    SYSTEM_QUERY_OPTION_LEVELS_NOT_ALLOWED_HERE,
    SYNTAX,
    /** parameter: alias name */
    DUPLICATED_ALIAS,
    /**Entity id must be followed by system query option id */
    ENTITYID_MISSING_SYSTEM_QUERY_OPTION_ID;

    @Override
    public String getKey() {
      return name();
    }
  }

  public UriParserSyntaxException(String developmentMessage, MessageKey messageKey,
                                  String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  public UriParserSyntaxException(String developmentMessage, Throwable cause, MessageKey messageKey,
                                  String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

}