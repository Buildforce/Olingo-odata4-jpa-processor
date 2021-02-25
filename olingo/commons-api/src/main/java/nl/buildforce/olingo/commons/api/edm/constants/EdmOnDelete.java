/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.constants;

/**
 * A navigation property MAY define one edm:OnDelete element. It describes the action the service will take on 
 * related entities when the entity on which the navigation property is defined is deleted.
 */
public enum EdmOnDelete {
  
  /**
   * Cascade, meaning the related entities will be deleted if the source entity is deleted
   */
  Cascade, 
  
  /**
   * None, meaning a DELETE request on a source entity with related entities will fail,
   */
  None, 
  
  /**
   * SetNull, meaning all properties of related entities that are tied to properties of the source entity via a 
   * referential constraint and that do not participate in other referential constraints will be set to null,
   */
  SetNull, 
  
  /**
   * SetDefault, meaning all properties of related entities that are tied to properties of the source entity via 
   * a referential constraint and that do not participate in other referential constraints will be set to 
   * their default value.
   */
  SetDefault
}
