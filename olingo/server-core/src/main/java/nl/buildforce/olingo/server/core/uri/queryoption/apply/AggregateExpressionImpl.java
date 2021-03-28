/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.AggregateExpression;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents an aggregate expression.
 */
public class AggregateExpressionImpl implements AggregateExpression {

  private String              alias;
  private FullQualifiedName   customMethod;
  private Expression          expression;
  private final List<AggregateExpression> from = new ArrayList<>();
  private AggregateExpression inlineAggregateExpression;
  private UriInfo             path;
  private StandardMethod      standardMethod;

  public AggregateExpressionImpl setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  @Override
  public FullQualifiedName getCustomMethod() {
    return customMethod;
  }

  public AggregateExpressionImpl setCustomMethod(FullQualifiedName customMethod) {
    this.customMethod = customMethod;
    return this;
  }

  public AggregateExpressionImpl setExpression(Expression expression) {
    this.expression = expression;
    return this;
  }

  public AggregateExpressionImpl addFrom(AggregateExpression from) {
    this.from.add(from);
    return this;
  }

  public AggregateExpressionImpl setInlineAggregateExpression(AggregateExpression aggregateExpression) {
    inlineAggregateExpression = aggregateExpression;
    return this;
  }

  public AggregateExpressionImpl setPath(UriInfo uriInfo) {
    path = uriInfo;
    return this;
  }

  @Override
  public StandardMethod getStandardMethod() {
    return standardMethod;
  }

  public AggregateExpressionImpl setStandardMethod(StandardMethod standardMethod) {
    this.standardMethod = standardMethod;
    return this;
  }

/*
  @Override
  public List<UriResource> getPath() {
    return path == null ? Collections.emptyList() : path.getUriResourceParts();
  }

  @Override
  public Expression getExpression() {
    return expression;
  }

  @Override
  public AggregateExpression getInlineAggregateExpression() {
    return inlineAggregateExpression;
  }

  @Override
  public List<AggregateExpression> getFrom() {
    return Collections.unmodifiableList(from);
  }

  @Override
  public String getAlias() {
    return alias;
  }
*/

}