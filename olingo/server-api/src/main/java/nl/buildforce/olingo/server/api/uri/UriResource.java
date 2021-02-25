/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

/**
 * Super interface for all objects representing resource parts.
 * See {@link UriInfoResource} for details.
 */
public interface UriResource {

  /**
   * @return Kind of the resource part
   */
  UriResourceKind getKind();

  /**
   * In case of an EntitySet this method will return the EntitySet name. In Case of $ref this method will return '$ref"
   * as a String.
   * @return the value of this URI Resource Segment
   */
  String getSegmentValue();

  @Override
  String toString();

}
