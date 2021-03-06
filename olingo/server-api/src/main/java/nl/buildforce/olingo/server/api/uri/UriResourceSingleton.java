/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmSingleton;

/**
 * Used to describe an entity set used within an resource path
 * For example: http://.../serviceroot/singleton
 */
public interface UriResourceSingleton extends UriResourcePartTyped {

  /**
   * @return Singleton used in the resource path
   */
  EdmSingleton getSingleton();

  /**
   * @return Type of the Singleton
   */
  EdmEntityType getEntityType();

// --Commented out by Inspection START (''21-03-06 11:48):
//  /**
//   * @return Type filter
//   */
//  EdmEntityType getEntityTypeFilter();
// --Commented out by Inspection STOP (''21-03-06 11:48)

}