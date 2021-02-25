/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

/**
 * The type Csdl entity set path.
 */
public class CsdlEntitySetPath {

  private String bindingParameter;

  private String path;

  /**
   * Gets binding parameter.
   *
   * @return the binding parameter
   */
  public String getBindingParameter() {
    return bindingParameter;
  }

  /**
   * Sets binding parameter.
   *
   * @param bindingParameter the binding parameter
   * @return the binding parameter
   */
  public CsdlEntitySetPath setBindingParameter(String bindingParameter) {
    this.bindingParameter = bindingParameter;
    return this;
  }

  /**
   * Gets path.
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * Sets path.
   *
   * @param path the path
   * @return the path
   */
  public CsdlEntitySetPath setPath(String path) {
    this.path = path;
    return this;
  }

}