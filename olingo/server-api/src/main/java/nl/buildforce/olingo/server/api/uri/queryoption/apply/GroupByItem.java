/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import java.util.List;

import nl.buildforce.olingo.server.api.uri.UriResource;

/**
 * Represents a grouping property.
 * @see GroupBy
 */
public interface GroupByItem {

  /**
   * Gets the path.
   * @return a (potentially empty) list of path segments (and never <code>null</code>)
   */
  List<UriResource> getPath();

  /**
   * Whether a nested rollup clause contains the special value '$all'.
   * @return <code>true</code> if '$all' has been given in rollup, <code>false</code> otherwise
   */
  boolean isRollupAll();

  /**
   * Gets the rollup.
   * @return a (potentially empty) list of grouping items (and never <code>null</code>)
   */
  List<GroupByItem> getRollup();
}
