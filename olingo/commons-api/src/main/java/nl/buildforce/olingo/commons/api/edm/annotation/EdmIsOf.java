/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;
import nl.buildforce.olingo.commons.api.edm.EdmType;

/**
 *  The edm:IsOf expression evaluates a child expression and returns a Boolean value indicating whether 
 *  the child expression returns the specified type
 */
public interface EdmIsOf extends EdmDynamicExpression, EdmAnnotatable {
 
  /**
   * Facet MaxLength
   * @return fact MaxLength
   */
  Integer getMaxLength();

  /**
   * Facet Precision
   * @return fact Precision
   */
  Integer getPrecision();
  
  /**
   * Facet Scale
   * @return facet Scale
   */
  Integer getScale();
  
  /*
    Facet SRID
    @return facet SRID
   */
  // SRID getSrid();
  
  /**
   * The type which is checked again the child expression
   * @return EdmType
   */
  EdmType getType();
  
  /**
   * Returns true if the child expression returns the specified typed 
   * @return Returns true if the child expression returns the specified typed 
   */
  EdmExpression getValue();
}