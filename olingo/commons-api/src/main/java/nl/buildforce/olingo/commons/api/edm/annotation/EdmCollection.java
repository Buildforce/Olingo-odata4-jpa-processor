/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

import java.util.List;

/**
 * The edm:Collection expression enables a value to be obtained from zero or more child expressions. 
 * The value calculated by the collection expression is the collection of the values calculated 
 * by each of the child expressions.
 */
public interface EdmCollection extends EdmDynamicExpression {

  /**
   * Returns a list of child expression
   * @return List of child expression
   */
  List<EdmExpression> getItems();
}
