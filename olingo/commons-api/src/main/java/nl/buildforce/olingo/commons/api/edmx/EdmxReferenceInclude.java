/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edmx;

/**
 * edmx:Include elements that specify the schemas to include from the target document.
 */
public class EdmxReferenceInclude {
  private final String namespace;
  private final String alias;

  /**
   * Create include with given namespace and alias.
   *
   * @param namespace of include
   * @param alias of include
   */
  public EdmxReferenceInclude(String namespace, String alias) {
    this.namespace = namespace;
    this.alias = alias;
  }

  /**
   * Create include with given namespace and empty (<code>NULL</code>) alias.
   *
   * @param namespace of include
   */
  public EdmxReferenceInclude(String namespace) {
    this(namespace, null);
  }

  /**
   * @return Namespace of the include
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return alias of the include if one defined; null otherwise
   */
  public String getAlias() {
    return alias;
  }
}