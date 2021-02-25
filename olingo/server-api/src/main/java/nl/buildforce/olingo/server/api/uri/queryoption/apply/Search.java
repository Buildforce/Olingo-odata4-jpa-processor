/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;
import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;

/**
 * Represents the search transformation.
 */
public interface Search extends ApplyItem {

  /**
   * Gets the search option.
   * @return a {@link SearchOption} (but never <code>null</code>)
   */
  SearchOption getSearchOption();
}
