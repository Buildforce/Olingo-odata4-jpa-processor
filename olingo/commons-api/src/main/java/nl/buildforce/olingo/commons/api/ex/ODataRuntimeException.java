/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.ex;

/**
 * Core runtime exception for OData.
 */
public class ODataRuntimeException extends RuntimeException {

  //     private static final long serialVersionUID = 5492375572049190883L;

  /**
   * Create with <code>message</code>.
   *
   * @param msg message text for exception
   */
  public ODataRuntimeException(String msg) {
    super(msg);
  }

  /**
   * Create with <code>message</code> for and <code>cause</code> of exception.
   *
   * @param msg message text for exception
   * @param cause cause of exception
   */
  public ODataRuntimeException(String msg, Exception cause) {
    super(msg, cause);
  }

  /**
   * Create with <code>cause</code> of exception.
   *
   * @param cause cause of exception
   */
  public ODataRuntimeException(Exception cause) {
    super(cause);
  }

}