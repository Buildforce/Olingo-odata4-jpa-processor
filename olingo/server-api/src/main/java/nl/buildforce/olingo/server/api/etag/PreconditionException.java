/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.etag;

import nl.buildforce.olingo.server.api.ODataLibraryException;

/**
 * This exception is thrown for invalid precondition error cases.
 */
public class PreconditionException extends ODataLibraryException {
  //     private static final long serialVersionUID = -8112658467394158700L;

  public enum MessageKeys implements MessageKey {
    /** no parameter */
    MISSING_HEADER,
    /** no parameter */
    FAILED,
    /** no parameter */
    INVALID_URI;

    @Override
    public String getKey() {
      return name();
    }
  }

  public PreconditionException(String developmentMessage, MessageKey messageKey,
                               String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  public PreconditionException(String developmentMessage, Throwable cause,
                               MessageKey messageKey, String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

  @Override
  protected String getBundleName() {
    return DEFAULT_SERVER_BUNDLE_NAME;
  }

}