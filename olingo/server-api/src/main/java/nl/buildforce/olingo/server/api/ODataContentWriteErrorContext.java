/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

/**
 * The WriteContentErrorErrorContext is the parameter for the WriteContentErrorCallback.
 * and contains all relevant error information
 */
public interface ODataContentWriteErrorContext {
  /**
   * Get the exception which caused this error (as Java exception).
   * If the cause exception is a ODataLibraryException this method will
   * return the same exception as the {@link #getODataLibraryException()} method.
   *
   * @return the exception which caused this error (as Java exception).
   */
  //Exception getException();
  /**
   * Get the exception which caused this error (as ODataLibraryException exception).
   * If the cause exception is an ODataLibraryException this method will
   * return the same exception as the {@link #getException()} method.
   * If the cause exception is <b>NOT</b> an ODataLibraryException this method will
   * return <code>NULL</code>.
   *
   * @return the cause exception if it is an ODataLibraryException otherwise
   *          this method will return <code>NULL</code>.
   */
  //ODataLibraryException getODataLibraryException();

}