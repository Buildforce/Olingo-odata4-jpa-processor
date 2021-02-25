/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

/**
 * Represents an alias info CSDL item
 */
public class CsdlAliasInfo {

  private String namespace;

  private String alias;

  /**
   * Returns the namespace of the alias
   * @return namespace of the alias
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * Sets the namespace of the alias
   * @param namespace the namespace of the alias
   * @return this instance
   */
  public CsdlAliasInfo setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * Returns the alias of item
   * @return Alias alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Sets the alias of the item
   * @param alias Alias
   * @return this instance
   */
  public CsdlAliasInfo setAlias(String alias) {
    this.alias = alias;
    return this;
  }

}
