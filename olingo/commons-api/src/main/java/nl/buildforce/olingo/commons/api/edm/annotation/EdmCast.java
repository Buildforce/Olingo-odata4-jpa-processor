/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;
import nl.buildforce.olingo.commons.api.edm.EdmType;

/**
 * Represents an edm:Cast expression.
 * Casts the value obtained from its single child expression to the specified type
 */
public interface EdmCast extends EdmDynamicExpression, EdmAnnotatable {
  /**
   * Returns the facet attribute MaxLength
   * @return Returns the facet attribute MaxLength
   */
  Integer getMaxLength();
  
  /**
   * Returns the facet attribute Precision
   * @return Returns the facet attribute Precision
   */
  Integer getPrecision();
  
  /**
   * Returns the facet attribute Scale
   * @return Returns the facet attribute Scale
   */
  Integer getScale();

  /*
    Returns the facet attribute SRID
    @return Returns the facet attribute SRID
   */
  // SRID getSrid();

  /**
   * Value cast to
   * @return value cast to
   */
  EdmType getType();
  
  /**
   * Cast value of the expression
   * @return Cast value
   */
  EdmExpression getValue();
}