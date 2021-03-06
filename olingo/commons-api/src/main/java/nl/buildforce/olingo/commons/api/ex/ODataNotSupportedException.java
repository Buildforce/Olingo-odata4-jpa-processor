/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.ex;

/**
 * Core runtime exception for OData.
 */
public class ODataNotSupportedException extends ODataRuntimeException {

  // private static final long serialVersionUID = 42L;

  /**
   * Create with <code>message</code>.
   *
   * @param msg message text for exception
   */
  public ODataNotSupportedException(String msg) {
    super(msg);
  }

// --Commented out by Inspection START (''21-03-09 21:47):
//  /**
//   * Create with <code>message</code> for and <code>cause</code> of exception.
//   *
//   * @param msg message text for exception
//   * @param cause cause of exception
//   */
//  public ODataNotSupportedException(String msg, Exception cause) {
//    super(msg, cause);
//  }
// --Commented out by Inspection STOP (''21-03-09 21:47)

// --Commented out by Inspection START (''21-03-09 21:47):
//  /**
//   * Create with <code>cause</code> of exception.
//   *
//   * @param cause cause of exception
//   */
//  public ODataNotSupportedException(Exception cause) {
//    super(cause);
//  }
// --Commented out by Inspection STOP (''21-03-09 21:47)

}