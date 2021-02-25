/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.OrderByItem;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

public class OrderByItemImpl implements OrderByItem {

  private Expression expression;
  // default sort order is ascending
  private boolean descending;

  @Override
  public boolean isDescending() {
    return descending;
  }

  public OrderByItemImpl setDescending(boolean descending) {
    this.descending = descending;
    return this;
  }

  @Override
  public Expression getExpression() {
    return expression;
  }

  public OrderByItemImpl setExpression(Expression expression) {
    this.expression = expression;
    return this;
  }

}
