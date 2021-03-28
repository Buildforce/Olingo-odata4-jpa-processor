/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;
import nl.buildforce.olingo.server.api.uri.queryoption.DeltaTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FilterOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FormatOption;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.TopOption;

/**
 * Used for URI info kind {@link UriInfoKind#crossjoin} to describe URIs like
 * http://.../serviceroot/$crossjoin(...)
 */
public interface UriInfoCrossjoin {

  /**
   * @return List of entity set names
   */
  List<String> getEntitySetNames();

  /**
   * @return Object containing information of the $expand option
   */
  ExpandOption getExpandOption();

  /**
   * @return Object containing information of the $filter option
   */
  FilterOption getFilterOption();

  /**
   * @return Object containing information of the $format option
   */
  // FormatOption getFormatOption();

  /**
   * @return Object containing information of the $count option
   */
  // CountOption getCountOption();

  /**
   * @return Object containing information of the $orderby option
   */
  OrderByOption getOrderByOption();
  
  /**
   * @return Object containing information of the $search option
   */
  // SearchOption getSearchOption();

  /**
   * @return Object containing information of the $select option
   */
  SelectOption getSelectOption();

  /**
   * @return Object containing information of the $skip option
   */
  // SkipOption getSkipOption();

  /**
   * @return Object containing information of the $skiptoken option
   */
  // SkipTokenOption getSkipTokenOption();

  /**
   * @return Object containing information of the $top option
   */
  // TopOption getTopOption();
  
  /**
   * @return Object containing information of the $deltatoken option
   */
  //DeltaTokenOption getDeltaTokenOption();

}