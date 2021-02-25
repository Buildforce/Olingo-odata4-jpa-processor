/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

/**
 * Enumeration for all OData property types.
 */
public enum PropertyType {

  /**
   * Primitive (including geospatial).
   */
  PRIMITIVE,
  /**
   * Enum.
   */
  ENUM,
  /**
   * Collection.
   */
  COLLECTION,
  /**
   * Complex.
   */
  COMPLEX,
  /**
   * Empty type (possibly, no type information could be retrieved).
   */
  EMPTY
}
