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

// --Commented out by Inspection START (''21-03-07 22:26):
//  /**
//   * Gets the partial-aggregation method.
// --Commented out by Inspection START (''21-03-07 22:26):
////   * @return a {@link Method} (but never <code>null</code>)
////   */
////  Method getMethod();
// --Commented out by Inspection START (''21-03-07 22:26):
////// --Commented out by Inspection STOP (''21-03-07 22:26)
////
////  /**
////   * Gets the expression that determines the number of items to aggregate.
//// --Commented out by Inspection STOP (''21-03-07 22:26)
//   * @return an {@link Expression} (but never <code>null</code>)
//   */
//  Expression getNumber();
//
//  /**
//   * Gets the expression that determines the values to aggregate.
//   * @return an {@link Expression} (but never <code>null</code>)
//   */
//  Expression getValue();
// --Commented out by Inspection STOP (''21-03-07 22:26)

}