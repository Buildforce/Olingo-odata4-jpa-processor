/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.search;

public interface SearchBinary extends SearchExpression {

  SearchBinaryOperatorKind getOperator();

  SearchExpression getLeftOperand();

  SearchExpression getRightOperand();

}
