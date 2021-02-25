/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;

/**
 * EdmType holds the namespace of a given type and its type as {@link EdmTypeKind}.
 */
public interface EdmType extends EdmNamed {

  /**
   * @return full qualified name
   */
  FullQualifiedName getFullQualifiedName();

  /**
   * Namespace of this {@link EdmType}.
   *
   * @return namespace as String
   */
  String getNamespace();

  /**
   * @return {@link EdmTypeKind} of this {@link EdmType}
   */
  EdmTypeKind getKind();

}
