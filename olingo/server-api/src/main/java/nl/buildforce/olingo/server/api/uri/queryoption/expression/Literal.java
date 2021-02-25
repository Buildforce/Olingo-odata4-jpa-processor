/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

import nl.buildforce.olingo.commons.api.edm.EdmType;

/**
 * Represents a literal expression node in the expression tree
 * Literal is not validated by default
 */
public interface Literal extends Expression {

  /**
   * @return Literal
   */
  String getText();

  /**
   * Numeric literals without an dot and without an e return the smallest possible Edm Integer type.
   * Numeric literals without an dot, without an e and larger than 2^63 - 1 are considered as Edm.Decimal
   * Numeric literals with an e, are considered to be Edm.Double
   * Numeric literals with an dot and without an e, are supposed to be Edm.Decimal
   *
   * @return Type of the literal if detected. The type of the literal is guessed by the parser.
   */
  EdmType getType();
  
}