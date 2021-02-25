/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;

/**
 * Represents a transformation with a custom function.
 */
public interface CustomFunction extends ApplyItem {

  /**
   * Gets the function to use.
   * @return an {@link EdmFunction} (but never <code>null</code>)
   */
  EdmFunction getFunction();

  /**
   * Gets the function parameters.
   * @return a (potentially empty) list of parameters (but never <code>null</code>)
   */
  List<UriParameter> getParameters();
}
