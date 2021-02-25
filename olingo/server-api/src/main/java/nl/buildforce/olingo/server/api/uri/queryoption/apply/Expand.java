/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;

/**
 * Represents the expand transformation.
 */
public interface Expand extends ApplyItem {

  /**
   * Gets the expand option.
   * @return an {@link ExpandOption} (but never <code>null</code>)
   */
  ExpandOption getExpandOption();
}
