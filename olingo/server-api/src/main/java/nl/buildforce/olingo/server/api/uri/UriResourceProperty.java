/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import nl.buildforce.olingo.commons.api.edm.EdmProperty;

/**
 * Used to describe an resource part which is an property (super interface)
 */
public interface UriResourceProperty extends UriResourcePartTyped {

  /**
   * @return Property used in the resource path
   */
  EdmProperty getProperty();

}
