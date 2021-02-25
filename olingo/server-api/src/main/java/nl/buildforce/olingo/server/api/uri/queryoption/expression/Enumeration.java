/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmEnumType;

/**
 * Represents a enumeration expression node in the expression tree
 * <br>
 * A enumeration expression node is inserted in the expression tree for any valid
 * enumeration. E.g. for $filter=style has Sales.Pattern'Yellow'
 */
public interface Enumeration extends Expression {

  /**
   * @return A list of enumeration values
   */
  List<String> getValues();

  /**
   * @return The enumeration type used before the enumeration values
   */
  EdmEnumType getType();

}