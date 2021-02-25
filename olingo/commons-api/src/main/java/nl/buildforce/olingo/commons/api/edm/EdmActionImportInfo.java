/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.net.URI;

/**
 * Objects of this class contain information about one action import inside the EntityDataModel.
 */
public interface EdmActionImportInfo extends EdmOperationImportInfo {

  /**
   * @return the action import name
   */
  String getActionImportName();

  /**
   * We use a {@link URI} object here to ensure the right encoding. If a string representation is needed the
   * toASCIIString() method can be used.
   *
   * @return the uri to this function import
   */
  URI getActionImportUri();
}
