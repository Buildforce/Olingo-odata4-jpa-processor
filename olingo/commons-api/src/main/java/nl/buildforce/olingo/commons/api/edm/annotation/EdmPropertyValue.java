/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;

/**
 * The edm:PropertyValue element supplies a value to a property on the type instantiated by an 
 * edm:Record expression (See {@link EdmRecord}). The value is obtained by evaluating an expression.
 */
public interface EdmPropertyValue extends EdmAnnotatable {
  /**
   * Property name
   * @return Property name
   */
  String getProperty();
  
  /**
   * Evaluated value of the expression (property value)
   * @return evaluated value of the expression
   */
  EdmExpression getValue();

}
