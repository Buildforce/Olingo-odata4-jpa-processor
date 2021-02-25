/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents an aggregate expression.
 * @see Aggregate
 */
public interface AggregateExpression {

  /** Standard aggregation method. */
  enum StandardMethod { SUM, MIN, MAX, AVERAGE, COUNT_DISTINCT }

  /**
   * Gets the path prefix and the path segment.
   * @return a (potentially empty) list of path segments (and never <code>null</code>)
   */
  List<UriResource> getPath();

  /**
   * Gets the common expression to be aggregated.
   * @return an {@link Expression} that could be <code>null</code>
   */
  Expression getExpression();

  /**
   * Gets the standard aggregation method if used.
   * @return a {@link StandardMethod} or <code>null</code>
   * @see #getCustomMethod()
   */
  StandardMethod getStandardMethod();

  /**
   * Gets the name of the custom aggregation method if used.
   * @return a {@link FullQualifiedName} or <code>null</code>
   * @see #getStandardMethod()
   */
  FullQualifiedName getCustomMethod();

  /**
   * Gets the name of the aggregate if an alias name has been set.
   * @return an identifier String or <code>null</code>
   */
  String getAlias();

  /**
   * Gets the inline aggregation expression to be applied to the target of the path if used.
   * @return an aggregation expression or <code>null</code>
   * @see #getPath()
   */
  AggregateExpression getInlineAggregateExpression();

  /**
   * Gets the aggregate expressions for <code>from</code>.
   * @return a (potentially empty) list of aggregate expressions (but never <code>null</code>)
   */
  List<AggregateExpression> getFrom();
}