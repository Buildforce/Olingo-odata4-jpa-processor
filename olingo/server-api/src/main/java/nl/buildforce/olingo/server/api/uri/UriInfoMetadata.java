/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import nl.buildforce.olingo.server.api.uri.queryoption.FormatOption;

/**
 * Used for URI info kind {@link UriInfoKind#metadata} to describe URIs like
 * http://.../serviceroot/$metadata...
 */
public interface UriInfoMetadata {

  /**
   * @return Object containing information of the $format option
   */
  FormatOption getFormatOption();

  /**
   * @return Object containing information of the URI fragment
   */
  String getFragment();

}
