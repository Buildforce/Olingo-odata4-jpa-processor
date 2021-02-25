/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;

/**
 * Represents a edm:If expression
 */
public interface EdmIf extends EdmDynamicExpression, EdmAnnotatable {
  
  /**
   * Returns the first expression of the edm:If expression.
   * This expression represents the condition of the if expression
   * 
   * @return First expression of the if expression
   */
  EdmExpression getGuard();
  
  /**
   * Return the second expression of the edm:If expression.
   * If the condition of the condition is evaluated to true,
   * this expression as to be executed.
   * 
   * @return Second Expression of the edm:If expression
   */
  EdmExpression getThen();

  /**
   * Return the third expression of the edm:If expression.
   * If the condition of the condition is evaluated to false,
   * this expression as to be executed.
   * 
   * @return Third Expression of the edm:If expression
   */
  EdmExpression getElse();
}
