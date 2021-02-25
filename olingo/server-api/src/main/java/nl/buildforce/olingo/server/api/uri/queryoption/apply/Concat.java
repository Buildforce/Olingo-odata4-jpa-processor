/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;

/**
 * Represents the concat transformation.
 */
public interface Concat extends ApplyItem {

  /**
   * Gets the concatenated apply options.
   * @return a non-empty list of apply options (and never <code>null</code>)
   */
  List<ApplyOption> getApplyOptions();
}
