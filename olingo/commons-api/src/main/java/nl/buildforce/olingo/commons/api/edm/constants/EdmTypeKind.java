/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.constants;

/**
 * EdmTypeKind specifies the type of an EDM element.
 */
public enum EdmTypeKind {
  /**
   * Primitive type
   */
  PRIMITIVE, 
  /**
   * Enum type
   */
  ENUM, 
  /**
   * Type definition
   */
  DEFINITION, 
  /**
   * Complex type
   */
  COMPLEX, 
  /**
   * Entity type
   */
  ENTITY, 
  /**
   * Navigation property type
   */
  // NAVIGATION,
  /**
   * Action type
   */
  ACTION, 
  /**
   * Function type
   */
  FUNCTION

}