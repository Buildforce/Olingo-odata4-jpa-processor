/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.validator;

import nl.buildforce.olingo.server.api.ODataLibraryException;

public class UriValidationException extends ODataLibraryException {

  //     private static final long serialVersionUID = -3179078078053564742L;

  public enum MessageKeys implements MessageKey {
    /** parameter: unsupported query option */
    UNSUPPORTED_QUERY_OPTION,
// --Commented out by Inspection START (''21-03-06 00:29):
//    /** parameter: unsupported uri kind */
//    UNSUPPORTED_URI_KIND,
// --Commented out by Inspection STOP (''21-03-06 00:29)
    /** parameter: unsupported uri resource kind */
    UNSUPPORTED_URI_RESOURCE_KIND,
    /** parameter: unsupported function return type */
    UNSUPPORTED_FUNCTION_RETURN_TYPE,
    /** parameter: unsupported action return type */
    UNSUPPORTED_ACTION_RETURN_TYPE,
    /** parameter: unsupported http method */
    UNSUPPORTED_HTTP_METHOD,
// --Commented out by Inspection START (''21-03-06 00:29):
//    /** parameter: unsupported parameter name */
//    UNSUPPORTED_PARAMETER,
// --Commented out by Inspection STOP (''21-03-06 00:29)
    /** parameter: system query option */
    SYSTEM_QUERY_OPTION_NOT_ALLOWED,
    /** parameters: system query option, http method */
    SYSTEM_QUERY_OPTION_NOT_ALLOWED_FOR_HTTP_METHOD,
    /** parameter: invalid key property */
    INVALID_KEY_PROPERTY,
    /** parameter: key property */
    DOUBLE_KEY_PROPERTY,
    /** parameter: untyped segment name */
    LAST_SEGMENT_NOT_TYPED,
    /** parameter: unallowed kind before $value */
    UNALLOWED_KIND_BEFORE_VALUE,
    /** parameter: unallowed kind before $count */
    UNALLOWED_KIND_BEFORE_COUNT,
    /** parameter: unallowed resource path */
    UNALLOWED_RESOURCE_PATH,
    /** parameter: missing parameter name */
    MISSING_PARAMETER,
    /** parameter: missing alias name */
    MISSING_ALIAS,
    /** invalid value for Property**/
    INVALID_VALUE_FOR_PROPERTY;

    @Override
    public String getKey() {
      return name();
    }
  }

  public UriValidationException(String developmentMessage, MessageKey messageKey,
                                String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  public UriValidationException(String developmentMessage, Throwable cause, MessageKey messageKey,
                                String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

  @Override
  protected String getBundleName() {
    return DEFAULT_SERVER_BUNDLE_NAME;
  }

}