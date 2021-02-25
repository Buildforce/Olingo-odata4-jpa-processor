/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents a transformation with one of the pre-defined methods
 * <code>bottomcount</code>, <code>bottompercent</code>, <code>bottomsum</code>,
 * <code>topcount</code>, <code>toppercent</code>, <code>topsum</code>.
 */
public interface BottomTop extends ApplyItem {

  /** Pre-defined method for partial aggregration. */
  enum Method { BOTTOM_COUNT, BOTTOM_PERCENT, BOTTOM_SUM, TOP_COUNT, TOP_PERCENT, TOP_SUM }

  /**
   * Gets the partial-aggregation method.
   * @return a {@link Method} (but never <code>null</code>)
   */
  Method getMethod();

  /**
   * Gets the expression that determines the number of items to aggregate.
   * @return an {@link Expression} (but never <code>null</code>)
   */
  Expression getNumber();

  /**
   * Gets the expression that determines the values to aggregate.
   * @return an {@link Expression} (but never <code>null</code>)
   */
  Expression getValue();
}