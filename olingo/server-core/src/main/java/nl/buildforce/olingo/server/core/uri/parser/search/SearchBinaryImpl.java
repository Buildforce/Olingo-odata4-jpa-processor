/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser.search;

import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchBinary;
import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchBinaryOperatorKind;
import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchExpression;

public class SearchBinaryImpl extends SearchExpressionImpl implements SearchBinary {

  private final SearchBinaryOperatorKind operator;
  private final SearchExpression left;
  private final SearchExpression right;

  public SearchBinaryImpl(SearchExpression left, SearchBinaryOperatorKind operator,
                          SearchExpression right) {
    this.left = left;
    this.operator = operator;
    this.right = right;
  }

  @Override
  public SearchBinaryOperatorKind getOperator() {
    return operator;
  }

  @Override
  public SearchExpression getLeftOperand() {
    return left;
  }

  @Override
  public SearchExpression getRightOperand() {
    return right;
  }

  @Override
  public String toString() {
    return "{" + left + " " + operator.name() + " " + right + '}';
  }
}
