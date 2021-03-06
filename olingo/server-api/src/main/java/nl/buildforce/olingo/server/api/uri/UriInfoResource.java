/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;
import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;
import nl.buildforce.olingo.server.api.uri.queryoption.CustomQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.DeltaTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FilterOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FormatOption;
import nl.buildforce.olingo.server.api.uri.queryoption.IdOption;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.TopOption;

/**
 * Used for URI info kind {@link UriInfoKind#resource} to describe URIs like
 * E.g. http://.../serviceroot/entitySet
 */
public interface UriInfoResource {

  /**
   * @return List of custom query options used in the URI (without alias definitions)
   */
  List<CustomQueryOption> getCustomQueryOptions();

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
  FormatOption getFormatOption();

  /**
   * @return Object containing information of the $id option
   */
  IdOption getIdOption();

  /**
   * @return Object containing information of the $count option
   */
  CountOption getCountOption();
  
  /**
   * @return Object containing information of the $deltatoken option
   */
  DeltaTokenOption getDeltaTokenOption();
  
  /**
   * @return Object containing information of the $orderby option
   */
  OrderByOption getOrderByOption();

  /**
   * @return Object containing information of the $search option
   */
  SearchOption getSearchOption();

  /**
   * @return Object containing information of the $select option
   */
  SelectOption getSelectOption();

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
   * @return information about the $apply option
   */
  ApplyOption getApplyOption();

  /**
   * The path segments behind the service root define which resources are
   * requested by that URI. This may be entities/functions/actions and more.
   * Each segments information (name, key predicates, function parameters, ...) is
   * stored within an resource object dedicated for that segment type.</p>
   * For example: the URI http://.../serviceroot/entitySet(1)/Adresse will
   * have 2 ResourceParts:<br>
   * - The first one of type {@link UriResourceEntitySet} containing the name of the entity set and also the key
   * predicate information.<br>
   * - The second one of type {@link UriResourceComplexProperty} containing the name of
   * the accessed complex property
   *
   * @return List of resource parts.
   */
  List<UriResource> getUriResourceParts();

  /**
   * @param alias
   * @return the value for the given alias or null if no value is defined
   */
  String getValueForAlias(String alias);

}
