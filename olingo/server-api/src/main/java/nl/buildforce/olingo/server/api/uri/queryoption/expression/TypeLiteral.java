/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

import nl.buildforce.olingo.commons.api.edm.EdmType;

/**
 * Represents a type literal expression in the expression tree
 */
public interface TypeLiteral extends Expression {

  /**
   * @return Type defined by the type literal
   */
  EdmType getType();

}