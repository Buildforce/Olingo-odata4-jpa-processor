/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

/**
 * The edm:PropertyPath expression provides a value for terms or term properties that specify the built-in 
 * abstract type Edm.PropertyPath.
 */
public interface EdmPropertyPath extends EdmDynamicExpression {
  
  /**
   * Returns the property path itself.
   * @return the property path itself
   */
  String getValue();

}
