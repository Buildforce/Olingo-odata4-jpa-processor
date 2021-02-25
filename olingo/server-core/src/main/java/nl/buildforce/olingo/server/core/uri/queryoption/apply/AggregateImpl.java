/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.apply.AggregateExpression;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.Aggregate;

/**
 * Represents the aggregate transformation.
 */
public class AggregateImpl implements Aggregate {

  private final List<AggregateExpression> expressions = new ArrayList<>();

  @Override
  public Kind getKind() {
    return Kind.AGGREGATE;
  }

  @Override
  public List<AggregateExpression> getExpressions() {
    return expressions;
  }

  public AggregateImpl addExpression(AggregateExpression expression) {
    expressions.add(expression);
    return this;
  }
}