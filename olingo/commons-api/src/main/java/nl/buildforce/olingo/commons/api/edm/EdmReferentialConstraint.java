/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * A referential constraint of a navigation property.
 */
public interface EdmReferentialConstraint extends EdmAnnotatable {

  /**
   * @return property name of the property inside this entity type or complex type.
   */
  String getPropertyName();

  /**
   * @return property name of the referenced entity type or complex type.
   */
  String getReferencedPropertyName();

}
