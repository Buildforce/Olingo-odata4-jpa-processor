/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * EdmTyped indicates if an EDM element is of a special type and holds the multiplicity of that type.
 */
public interface EdmTyped {

  /**
   * See {@link EdmType} for more information about possible types.
   *
   * @return {@link EdmType}
   */
  EdmType getType();

  /**
   * @return true if this typed element is a collection
   */
  boolean isCollection();
}
