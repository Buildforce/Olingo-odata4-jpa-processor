/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

public class EdmException extends RuntimeException {

  //   private static final long serialVersionUID = 1L;

  public EdmException(Exception cause) {
    super(cause);
  }

  public EdmException(String msg) {
    super(msg);
  }

  public EdmException(String string, Exception cause) {
    super(string, cause);
  }

}