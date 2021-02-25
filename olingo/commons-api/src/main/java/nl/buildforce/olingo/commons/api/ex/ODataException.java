/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.ex;

public class ODataException extends Exception {

  //     private static final long serialVersionUID = 3057981437954048107L;

  /**
   * Creates exception with <code>message</code>.
   * @param msg message text for exception
   */
  public ODataException(String msg) {
    super(msg);
  }

  /**
   * Creates exception with <code>message</code> and <code>cause</code> of exception.
   * @param msg   message text for exception
   * @param cause cause of exception
   */
  public ODataException(String msg, Throwable cause) {
    super(msg, cause);
  }

  /**
   * Creates exception with <code>cause</code> of exception.
   * @param cause cause of exception
   */
  public ODataException(Throwable cause) {
    super(cause);
  }

}