/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import nl.buildforce.olingo.commons.api.edm.EdmType;

/**
 * Used to describe an typed resource part (super interface)
 */
public interface UriResourcePartTyped extends UriResource {

  /**
   * @return Type of the resource part
   */
  EdmType getType();

  /**
   * @return True if the resource part is a collection, otherwise false
   */
  boolean isCollection();

  /**
   * @return String representation of the type
   */
  String getSegmentValue(boolean includeFilters);

  /**
   * @return String representation of the type
   */
  String toString(boolean includeFilters);

}