/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * The edm:Record expression enables a new entity type or complex type instance to be constructed.
 * A record expression contains zero or more edm:PropertyValue (See {@link EdmPropertyValue} )elements.
 */
public interface EdmRecord extends EdmDynamicExpression, EdmAnnotatable {
  
  /**
   * List of edm:PropertyValues (See {@link EdmPropertyValue}
   * @return List of edm:PropertyValues (See {@link EdmPropertyValue}
   */
  List<EdmPropertyValue> getPropertyValues();
  
  /**
   * Returns the entity type or complex type to be constructed.
   * @return Entity type or complex type
   */
  EdmStructuredType getType();
  
  /**
   * 
   * @return Fully Qualified Name of the Record
   */
  FullQualifiedName getTypeFQN();
}
