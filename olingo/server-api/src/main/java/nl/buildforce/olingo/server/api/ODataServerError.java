/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import nl.buildforce.olingo.commons.api.ex.ODataError;
import nl.buildforce.olingo.commons.api.ex.ODataErrorDetail;

/**
 * Class to hold all server relevant error information.
 */
public class ODataServerError extends ODataError {

  private Exception exception;
  private int statusCode;
  private Locale locale;

  /**
   * Gets the locale.
   * @return the locale for the exception message
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * Sets the locale.
   * @return this for method chaining
   */
  public ODataServerError setLocale(Locale locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Gets the exception.
   * @return the exception with its hierarchy
   */
  public Exception getException() {
    return exception;
  }

  /**
   * Sets the exception.
   * @return this for method chaining
   */
  public ODataServerError setException(Exception exception) {
    this.exception = exception;
    return this;
  }

  /**
   * Gets the status code.
   * @return the status code which this error results in.
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * Sets the status code.
   * @param statusCode the status code which this error results in
   * @return this for method chaining
   */
  public ODataServerError setStatusCode(int statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * The value for the code name/value pair is a language-independent string.
   * Its value is a service-defined error code. This code serves as a sub-status
   * for the HTTP error code specified in the response. MAY be null.
   * @return this for method chaining
   */
  @Override
  public ODataServerError setCode(String code) {
    super.setCode(code);
    return this;
  }

  /**
   * The value for the message name/value pair MUST be a human-readable,
   * language-dependent representation of the error. MUST not be null.
   * @return this for method chaining
   */
  @Override
  public ODataServerError setMessage(String message) {
    super.setMessage(message);
    return this;
  }

  /**
   * The value for the target name/value pair is the target of the particular error (for example, the name of the
   * property in error). MAY be null.
   * @return this for method chaining
   */
  @Override
  public ODataServerError setTarget(String target) {
    super.setTarget(target);
    return this;
  }

  /**
   * Sets error details.
   * @return this for method chaining.
   */
  @Override
  public ODataServerError setDetails(List<ODataErrorDetail> details) {
    super.setDetails(details);
    return this;
  }

  /**
   * Sets server defined key-value pairs for debug environment only.
   * @return this for method chaining.
   */
  @Override
  public ODataServerError setInnerError(Map<String, String> innerError) {
    super.setInnerError(innerError);
    return this;
  }

  /**
   * Sets server defined key-value pairs.
   * @return this for method chaining.
   */
  @Override
  public ODataServerError setAdditionalProperties(Map<String, Object> additionalProperties) {
    super.setAdditionalProperties(additionalProperties);
    return this;
  }
}
