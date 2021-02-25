/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;

/**
 * Represents the compute transformation.
 */
public interface Compute extends ApplyItem {

  /**
   * Gets the compute expressions.
   * @return a non-empty list of compute expressions (and never <code>null</code>)
   */
  List<ComputeExpression> getExpressions();
}
