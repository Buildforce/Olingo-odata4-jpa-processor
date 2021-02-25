/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.ex;

import java.util.Map;

/**
 * OData details， for example <tt>{ "error": {..., "details":[
 * {"code": "301","target": "$search" ,"message": "$search query option not supported"}
 * ],...}}</tt>.
 */
public class ODataErrorDetail {

  private String code;
  private String message;
  private String target;
  private Map<String, Object> additionalProperties;

  /**
   * Gets error code.
   *
   * @return error code.
   */
  public String getCode() {
    return code;
  }

  public ODataErrorDetail setCode(String code) {
    this.code = code;
    return this;
  }

  /**
   * Gets error message.
   *
   * @return error message.
   */
  public String getMessage() {
    return message;
  }

  public ODataErrorDetail setMessage(String message) {
    this.message = message;
    return this;
  }

  /**
   * Gets error target.
   *
   * @return error message.
   */
  public String getTarget() {
    return target;
  }

  /**
   * Set the error target.
   *
   * @param target the error target
   * @return this ODataErrorDetail instance (fluent builder)
   */
  public ODataErrorDetail setTarget(String target) {
    this.target = target;
    return this;
  }
  
  /**
   * Sets server defined additional properties
   * @param additionalProperties additionalProperties
   * @return this ODataErrorDetail instance (fluent builder)
   */
  public ODataErrorDetail setAdditionalProperties(Map<String, Object> additionalProperties) {
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
