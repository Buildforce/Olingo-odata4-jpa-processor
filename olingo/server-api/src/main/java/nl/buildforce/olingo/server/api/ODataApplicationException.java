/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import java.util.Locale;

import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;

/**
 * Exception thrown by OData service implementations.
 * @see ODataException
 */
public class ODataApplicationException extends ODataException {

  //     private static final long serialVersionUID = 5358683245923127425L;
  private int statusCode = HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode();
  private final Locale locale;
  private String oDataErrorCode;

  /**
   * Exception in an OData service implementation.
   * @param msg the text of the exception
   * @param statusCode the HTTP status code of the error response; the default is 500 - Internal Server Error
   * @param locale a {@link Locale} to enable translation of error messages
   * @see ODataException
   * @see HttpStatusCode
   */
  public ODataApplicationException(String msg, int statusCode, Locale locale) {
    super(msg);
    this.statusCode = statusCode;
    this.locale = locale;
  }

  /**
   * Exception in an OData service implementation.
   * @param msg the text of the exception
   * @param statusCode the HTTP status code of the error response; the default is 500 - Internal Server Error
   * @param locale a {@link Locale} to enable translation of error messages
   * @param oDataErrorCode the error code of the exception as defined by the OData standard
   * @see ODataException
   * @see HttpStatusCode
   */
  public ODataApplicationException(String msg, int statusCode, Locale locale,
                                   String oDataErrorCode) {
    this(msg, statusCode, locale);
    this.oDataErrorCode = oDataErrorCode;
  }

  /**
   * Exception in an OData service implementation.
   * @param msg the text of the exception
   * @param statusCode the HTTP status code of the error response; the default is 500 - Internal Server Error
   * @param locale a {@link Locale} to enable translation of error messages
   * @param cause the cause of this exception
   * @see ODataException
   * @see HttpStatusCode
   * @see Throwable#getCause()
   */
  public ODataApplicationException(String msg, int statusCode, Locale locale,
                                   Throwable cause) {
    super(msg, cause);
    this.statusCode = statusCode;
    this.locale = locale;
  }

  /**
   * Exception in an OData service implementation.
   * @param msg the text of the exception
   * @param statusCode the HTTP status code of the error response; the default is 500 - Internal Server Error
   * @param locale a {@link Locale} to enable translation of error messages
   * @param cause the cause of this exception
   * @param oDataErrorCode the error code of the exception as defined by the OData standard
   * @see ODataException
   * @see HttpStatusCode
   * @see Throwable#getCause()
   */
  public ODataApplicationException(String msg, int statusCode, Locale locale, Throwable cause,
                                   String oDataErrorCode) {
    this(msg, statusCode, locale, cause);
    this.oDataErrorCode = oDataErrorCode;
  }

  /**
   * Will return the status code which will be used as a status code for the HTTP response. If not set the default is a
   * 500 Internal Server Error.
   * @return status code for this exception
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * Returns the Locale which was used for the error message. The default is null.
   * @return locale used for the error message
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * This method will return the error code specified by the application. The default is null.
   * @return the applications error code.
   */
  public String getODataErrorCode() {
    return oDataErrorCode;
  }

}