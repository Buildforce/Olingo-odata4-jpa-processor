/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;
import nl.buildforce.olingo.server.api.uri.queryoption.FilterOption;

/**
 * Represents the filter transformation.
 */
public interface Filter extends ApplyItem {

  /**
   * Gets the filter option.
   * @return a {@link FilterOption} (but never <code>null</code>)
   */
  FilterOption getFilterOption();
}
