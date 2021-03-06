/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser.search;

import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchTerm;
import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchUnary;
import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchUnaryOperatorKind;

public class SearchUnaryImpl extends SearchExpressionImpl implements SearchUnary {
  private final SearchTerm operand;

  public SearchUnaryImpl(SearchTerm operand) {
    this.operand = operand;
  }

  @Override
  public SearchUnaryOperatorKind getOperator() {
    return SearchUnaryOperatorKind.NOT;
  }

/*
  @Override
  public SearchTerm getOperand() {
    return operand;
  }
*/


  @Override
  public String toString() {
    return "{" + getOperator().name() + " " + operand + '}';
  }

}