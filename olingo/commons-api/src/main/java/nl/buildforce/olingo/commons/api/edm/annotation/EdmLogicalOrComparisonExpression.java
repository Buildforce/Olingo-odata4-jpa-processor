/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;

/**
 * Represents a generic expression with two child exprssions
 */
public interface EdmLogicalOrComparisonExpression extends EdmDynamicExpression, EdmAnnotatable {

  /**
   * Returns the first expression (left child)
   * @return Child expression
   */
  EdmExpression getLeftExpression();
  
  /**
   * Returns the second expression (right child)
   * @return Child expression
   */
  EdmExpression getRightExpression();
}
