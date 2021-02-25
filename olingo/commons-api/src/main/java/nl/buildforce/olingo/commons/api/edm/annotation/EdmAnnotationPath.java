/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

/**
 * Represents an annotation path
 */
public interface EdmAnnotationPath extends EdmDynamicExpression {
  
  /**
   * Value of the path
   * @return value of the path
   */
  String getValue();
}
