/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

/**
 * Class indicating the $root reference. $root may be used within expressions to
 * refer to the current OData service
 * For example: http://.../serviceroot/entityset(1)?$filter=property eq $root/singleton/configstring
 */
public interface UriResourceRoot extends UriResource {
  // No additional methods needed for now.
}
