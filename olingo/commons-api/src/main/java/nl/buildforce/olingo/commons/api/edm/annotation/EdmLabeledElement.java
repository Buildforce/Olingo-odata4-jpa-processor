/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;

/**
 * The edm:LabeledElement expression assigns a name to a child expression. The value of the child expression can 
 * then be reused elsewhere with an edm:LabeledElementReference (See {@link EdmLabeledElementReference}) expression.
 */
public interface EdmLabeledElement extends EdmDynamicExpression, EdmAnnotatable {
  
  /**
   * Returns the assigned name
   * @return assigned name
   */
  String getName();

  /**
   * Returns the child expression
   * 
   * @return child expression
   */
  EdmExpression getValue();
}
