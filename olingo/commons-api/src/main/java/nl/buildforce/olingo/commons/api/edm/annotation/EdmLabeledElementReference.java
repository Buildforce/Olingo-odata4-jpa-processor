/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

/**
 *  The edm:LabeledElementReference expression returns the value of an 
 *  edm:LabeledElement (see {@link EdmLabeledElement}) expression.
 */
public interface EdmLabeledElementReference extends EdmDynamicExpression {
  
  /**
   * Returns the value of the edm:LabeledElement expression
   * @return value of the edm:LabeledElement expression
   */
  String getValue();
}
