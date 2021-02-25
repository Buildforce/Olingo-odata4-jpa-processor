/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import nl.buildforce.olingo.commons.api.edm.EdmComplexType;

/**
 * Used to describe an complex property used within an resource path
 * E.g. http://.../serviceroot/entityset(1)/complexproperty
 */
public interface UriResourceComplexProperty extends UriResourceProperty {

  /**
   * @return Complex property used in the resource path
   */
  EdmComplexType getComplexType();

  /**
   * Behind a complex property may be a type filter
   * E.g. http://.../serviceroot/entityset(1)/complexproperty/namespace.complextype
   * @return Type filter if found, otherwise null
   */
  EdmComplexType getComplexTypeFilter();

}
