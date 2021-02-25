/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

/**
 * The type Csdl property ref.
 */
public class CsdlPropertyRef extends CsdlAbstractEdmItem implements CsdlNamed {

  private String name;

  private String alias;

  @Override
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   * @return the name
   */
  public CsdlPropertyRef setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Gets alias.
   *
   * @return the alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Sets alias.
   *
   * @param alias the alias
   * @return the alias
   */
  public CsdlPropertyRef setAlias(String alias) {
    this.alias = alias;
    return this;
  }

}