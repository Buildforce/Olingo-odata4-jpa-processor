/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

/**
 * The type Csdl navigation property binding.
 */
public class CsdlNavigationPropertyBinding extends CsdlAbstractEdmItem {

  private String path;

  private String target;

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
  public CsdlNavigationPropertyBinding setPath(String path) {
    this.path = path;
    return this;
  }

  /**
   * Gets target.
   *
   * @return the target
   */
  public String getTarget() {
    return target;
  }

  /**
   * Sets target.
   *
   * @param target the target
   * @return the target
   */
  public CsdlNavigationPropertyBinding setTarget(String target) {
    this.target = target;
    return this;
  }

}