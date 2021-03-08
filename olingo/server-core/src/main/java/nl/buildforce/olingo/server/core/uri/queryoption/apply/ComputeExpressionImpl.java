/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.apply.ComputeExpression;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents an aggregate expression.
 */
public class ComputeExpressionImpl implements ComputeExpression {

  private Expression expression;
  private String alias;

/*
  @Override
  public Expression getExpression() {
    return expression;
  }

  @Override
  public String getAlias() {
    return alias;
  }
*/

  public ComputeExpressionImpl setExpression(Expression expression) {
    this.expression = expression;
    return this;
  }

  public ComputeExpressionImpl setAlias(String alias) {
    this.alias = alias;
    return this;
  }

}