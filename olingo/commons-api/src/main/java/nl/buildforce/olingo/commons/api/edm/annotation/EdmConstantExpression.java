/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

import java.util.List;

/**
 * Represents a constant expression
 */
public interface EdmConstantExpression extends EdmExpression {

  // TODO: Is methods

  /*
   * The value object of this expression or null if it is of type enum or geospatial.
   * @return a value object or null
   */
  // Object asPrimitive();

  /*
   * A list of enum members or empty list if this expression is of type primitive or geospatial.
   * @return a list of all enum members or empty list
   */
  // List<String> asEnumMembers();

  /*
   * Return the Enum type name or null if this expression is of type primitive or geospatial.
   * @return enum type name or null
   */
  // String getEnumTypeName();

  /**
   * Returns the value of the expression as String.
   * @return String representation of the expression
   */
  String getValueAsString();

}