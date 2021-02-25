/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

/**
 * The edm:NavigationPropertyPath expression provides a value for terms or term properties that specify the 
 * built-in abstract type Edm.NavigationPropertyPath
 */
public interface EdmNavigationPropertyPath extends EdmDynamicExpression {
  /**
   * Returns the navigation property path itself.
   * 
   * @return navigation property
   */
  String getValue();
}