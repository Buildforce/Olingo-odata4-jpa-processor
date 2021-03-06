/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.ex;

import java.util.List;
import java.util.Map;

/**
 * OData error.
 */
public class ODataError {

  private String code;
  private String message;
  private String target;
  private List<ODataErrorDetail> details;
  private Map<String, String> innerError;
  private Map<String, Object> additionalProperties;

  /**
   * The value for the code name/value pair is a language-independent string. Its value is a service-defined error code.
   * This code serves as a sub-status for the HTTP error code specified in the response. MAY be null.
   * @return the error code as a string
   */
  public String getCode() {
    return code;
  }

  /**
   * The value for the code name/value pair is a language-independent string. Its value is a service-defined error code.
   * This code serves as a sub-status for the HTTP error code specified in the response. MAY be null.
   * @param code the service defined error code for this error
   * @return this for method chaining
   */
  public ODataError setCode(String code) {
    this.code = code;
    return this;
  }

  /**
   * The value for the message name/value pair MUST be a human-readable, language-dependent representation of the error.
   * MUST not be null
   * @return the message string
   */
  public String getMessage() {
    return message;
  }

  /**
   * The value for the message name/value pair MUST be a human-readable, language-dependent representation of the error.
   * MUST not be null
   * @param message message for this error
   * @return this for method chaining
   */
  public ODataError setMessage(String message) {
    this.message = message;
    return this;
  }

  /**
   * The value for the target name/value pair is the target of the particular error (for example, the name of the
   * property in error). MAY be null.
   * @return the target string
   */
  public String getTarget() {
    return target;
  }

  /**
   * The value for the target name/value pair is the target of the particular error (for example, the name of the
   * property in error). MAY be null.
   * @param target target to which this error is related to
   * @return this for method chaining
   */
  public ODataError setTarget(String target) {
    this.target = target;
    return this;
  }

  /**
   * Gets error details.
   *
   * @return ODataErrorDetail list.
   */
  public List<ODataErrorDetail> getDetails() {
    return details;
  }

  /**
   * Sets error details.
   *
   * @return this for method chaining.
   */
  public ODataError setDetails(List<ODataErrorDetail> details) {
    this.details = details;
    return this;
  }

  /**
   * Gets server defined key-value pairs for debug environment only.
   *
   * @return a pair representing server defined object. MAY be null.
   */
  public Map<String, String> getInnerError() {
    return innerError;
  }

  /**
   * Sets server defined key-value pairs for debug environment only.
   *
   * @return this for method chaining.
   */
  public ODataError setInnerError(Map<String, String> innerError) {
    this.innerError = innerError;
    return this;
  }
  
  /**
   * Sets server defined additional properties
   * @param additionalProperties
   * @return this for method chaining.
   */
  public ODataError setAdditionalProperties(Map<String, Object> additionalProperties) {
	  this.additionalProperties = additionalProperties;
	  return this;
  }
  
  /**
   * Gets server defined additional properties.
   * @return a pair representing server defined object.
   */
  public Map<String, Object> getAdditionalProperties() {
	  return additionalProperties;
  }
}
