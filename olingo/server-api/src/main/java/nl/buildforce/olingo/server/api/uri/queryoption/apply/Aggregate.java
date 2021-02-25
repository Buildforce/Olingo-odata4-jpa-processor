/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;

/**
 * Represents the aggregate transformation.
 */
public interface Aggregate extends ApplyItem {

  /**
   * Gets the aggregate expressions.
   * @return a non-empty list of aggregate expressions (and never <code>null</code>)
   */
  List<AggregateExpression> getExpressions();
}
