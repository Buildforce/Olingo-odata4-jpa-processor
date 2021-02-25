/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

/**
 * Defines the type of a value (see Valuable).
 */
public enum ValueType {
  /**
   * Primitive value
   */
  PRIMITIVE, 
  /**
   * Enum type
   */
  ENUM, 
  /**
   * Complex value
   */
  COMPLEX, 
  /**
   * Entity value
   */
  ENTITY,
  /**
   * Collection of primitive values
   */
  COLLECTION_PRIMITIVE(PRIMITIVE),
  /**
   * Collection of enum values
   */
  COLLECTION_ENUM(ENUM),
  /**
   * Collection of complex values
   */
  COLLECTION_COMPLEX(COMPLEX),
  /**
   * Collection of entities
   */
  COLLECTION_ENTITY(ENTITY);

  private final ValueType baseType;

  ValueType() {
    baseType = this;
  }

  ValueType(ValueType baseType) {
    this.baseType = baseType;
  }

  /**
   * Get base type for this value type.
   * @return base type
   */
  public ValueType getBaseType() {
    return baseType;
  }

}