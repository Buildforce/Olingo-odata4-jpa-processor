/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.apply.Compute;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.ComputeExpression;

/**
 * Represents the compute transformation.
 */
public class ComputeImpl implements Compute {

  private final List<ComputeExpression> expressions = new ArrayList<>();

  @Override
  public Kind getKind() {
    return Kind.COMPUTE;
  }

/*
  @Override
  public List<ComputeExpression> getExpressions() {
    return expressions;
  }
*/

  public ComputeImpl addExpression(ComputeExpressionImpl expression) {
    expressions.add(expression);
    return this;
  }

}