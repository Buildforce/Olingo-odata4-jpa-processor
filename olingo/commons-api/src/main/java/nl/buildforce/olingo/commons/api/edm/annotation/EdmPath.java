/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

/**
 *  The edm:Path expression enables a value to be obtained by traversing an object graph. 
 *  It can be used in annotations that target entity containers, entity sets, entity types, complex types, 
 *  navigation properties of structured types, and properties of structured types.
 */
public interface EdmPath extends EdmDynamicExpression {
  
  /**
   * Returns the target value of the expression
   * 
   * @return target value of the expression
   */
  String getValue();

}
