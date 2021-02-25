/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.io.Serializable;
import java.util.Objects;

/**
 * A full qualified name of any element in the EDM consists of a name and a namespace.
 */
public final class FullQualifiedName implements Serializable {

  // private static final long serialVersionUID = -4063629050858999076L;

  private final String namespace;
  private final String name;

  private final String fqn;

  /**
   * Create the FQN with given namespace and name
   * @param namespace namespace of FQN
   * @param name name of FQN
   */
  public FullQualifiedName(String namespace, String name) {
    this.namespace = namespace;
    this.name = name;
    fqn = namespace + "." + name;
  }

  /**
   * Create the FQN with given namespace and name (which is split of last <code>.</code> of the parameter).
   * @param namespaceAndName namespace and name of FQN
   */
  public FullQualifiedName(String namespaceAndName) {
    int dotIdx = namespaceAndName.lastIndexOf('.');
    if (dotIdx == -1 || dotIdx == 0 || dotIdx == namespaceAndName.length() - 1) {
      throw new IllegalArgumentException(
          "Malformed " + FullQualifiedName.class.getSimpleName() + ": " + namespaceAndName);
    }

    fqn = namespaceAndName;
    namespace = fqn.substring(0, dotIdx);
    name = fqn.substring(dotIdx + 1);
  }

  /**
   * @return namespace
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * @return namespace.name
   */
  public String getFullQualifiedNameAsString() {
    return fqn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FullQualifiedName that = (FullQualifiedName) o;
    return (Objects.equals(namespace, that.namespace))
        && (Objects.equals(name, that.name));
  }

  @Override
  public int hashCode() {
    return fqn == null ? 0 : fqn.hashCode();
  }

  @Override
  public String toString() {
    return fqn;
  }

}