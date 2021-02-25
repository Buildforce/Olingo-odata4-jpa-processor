/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;

/**
 * Represents the system query option $apply, defined in the data aggregation extension.
 */
public interface ApplyOption extends SystemQueryOption {

  /**
   * @return a list of transformations
   */
  List<ApplyItem> getApplyItems();

  /**
   * @return the structured type associated with this apply option
   */
  EdmStructuredType getEdmStructuredType();
}
