/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;

/**
 * Represents the grouping transformation.
 */
public interface GroupBy extends ApplyItem {

  /**
   * Gets the items to group.
   * @return a non-empty list of {@link GroupByItem}s (but never <code>null</code>)
   */
  List<GroupByItem> getGroupByItems();

  /**
   * Gets the apply option to be applied to the grouped items.
   * @return an {@link ApplyOption} (but never <code>null</code>)
   */
  ApplyOption getApplyOption();
}
