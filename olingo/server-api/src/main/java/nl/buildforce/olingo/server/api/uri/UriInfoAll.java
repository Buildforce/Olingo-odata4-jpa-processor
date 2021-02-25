/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;
import nl.buildforce.olingo.server.api.uri.queryoption.CustomQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.DeltaTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FormatOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.TopOption;

/**
 * Used for URI info kind {@link UriInfoKind#all} to describe URIs like
 * http://.../serviceroot/$all
 */
public interface UriInfoAll {
  
  /**
   * @return List of custom query options used in the URI (without alias definitions)
   */
  List<CustomQueryOption> getCustomQueryOptions();

  /**
   * @return Object containing information of the $format option
   */
  FormatOption getFormatOption();

  /**
   * @return Object containing information of the $count option
   */
  CountOption getCountOption();

  /**
   * @return Object containing information of the $search option
   */
  SearchOption getSearchOption();

  /**
   * @return Object containing information of the $skip option
   */
  SkipOption getSkipOption();

  /**
   * @return Object containing information of the $skiptoken option
   */
  SkipTokenOption getSkipTokenOption();

  /**
   * @return Object containing information of the $top option
   */
  TopOption getTopOption();
  
  /**
   * @return Object containing information of the $deltatoken option
   */
  DeltaTokenOption getDeltaTokenOption();
}
