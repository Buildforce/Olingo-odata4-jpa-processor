/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.AggregateExpression;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents an aggregate expression.
 */
public class AggregateExpressionImpl implements AggregateExpression {

  private UriInfo path;
  private Expression expression;
  private StandardMethod standardMethod;
  private FullQualifiedName customMethod;
  private String alias;
  private AggregateExpression inlineAggregateExpression;
  private final List<AggregateExpression> from = new ArrayList<>();

  @Override
  public List<UriResource> getPath() {
    return path == null ? Collections.emptyList() : path.getUriResourceParts();
  }

  public AggregateExpressionImpl setPath(UriInfo uriInfo) {
    path = uriInfo;
    return this;
  }

  @Override
  public Expression getExpression() {
    return expression;
  }

  public AggregateExpressionImpl setExpression(Expression expression) {
    this.expression = expression;
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

  @Override
  public FullQualifiedName getCustomMethod() {
    return customMethod;
  }

  public AggregateExpressionImpl setCustomMethod(FullQualifiedName customMethod) {
    this.customMethod = customMethod;
    return this;
  }

  @Override
  public AggregateExpression getInlineAggregateExpression() {
    return inlineAggregateExpression;
  }

  public AggregateExpressionImpl setInlineAggregateExpression(AggregateExpression aggregateExpression) {
    inlineAggregateExpression = aggregateExpression;
    return this;
  }

  @Override
  public List<AggregateExpression> getFrom() {
    return Collections.unmodifiableList(from);
  }

  public AggregateExpressionImpl addFrom(AggregateExpression from) {
    this.from.add(from);
    return this;
  }

  @Override
  public String getAlias() {
    return alias;
  }

  public AggregateExpressionImpl setAlias(String alias) {
    this.alias = alias;
    return this;
  }
}