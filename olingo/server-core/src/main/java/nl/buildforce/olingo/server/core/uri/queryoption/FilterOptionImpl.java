/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.FilterOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

public class FilterOptionImpl extends SystemQueryOptionImpl implements FilterOption {

  private Expression expression;

  public FilterOptionImpl() {
    setKind(SystemQueryOptionKind.FILTER);
  }

  public FilterOptionImpl setExpression(Expression expression) {
    this.expression = expression;
    return this;
  }

  @Override
  public Expression getExpression() {
    return expression;
  }

}
