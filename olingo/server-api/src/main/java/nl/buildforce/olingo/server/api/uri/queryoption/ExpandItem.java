/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;

/**
 * Represents a single resource path which should be expanded with using the system query option $expand
 * For example: http://.../entitySet?$expand=Products($filter=DiscontinuedDate eq null)
 */
public interface ExpandItem {

  /**
   * @return Information of the option $level when used within $expand
   */
  LevelsExpandOption getLevelsOption();

  /**
   * @return Information of the option $filter when used within $expand
   */
  FilterOption getFilterOption();

  /**
   * @return Information of the option $search when used within $expand
   */
  SearchOption getSearchOption();

  /**
   * @return Information of the option $orderby when used within $expand
   */
  OrderByOption getOrderByOption();

  /**
   * @return Information of the option $skip when used within $expand
   */
  SkipOption getSkipOption();

  /**
   * @return Information of the option $top when used within $expand
   */
  TopOption getTopOption();

  /**
   * @return Information of the option $count when used within $expand
   */
  CountOption getCountOption();

  /**
   * @return Information of the option $select when used within $expand
   */
  SelectOption getSelectOption();

  /**
   * @return Information of the option $expand when used within $expand
   */
  ExpandOption getExpandOption();

  /**
   * @return Information on the option $apply when used within $expand
   */
  ApplyOption getApplyOption();

  /**
   * @return A {@link UriInfoResource} object containing the resource path segments to be expanded
   */
  UriInfoResource getResourcePath();

  /**
   * @return A star is used within $expand.
   * For example: ...?$expand=*
   */
  boolean isStar();

  /**
   * @return A $ref is used within $expand.
   * For example: ...?$expand=navigation/$ref
   */
  boolean isRef();

  /**
   * @return A $count is used within $expand.
   * For example: ...?$expand=navigation/$count
   */  
  boolean hasCountPath();
  
  /**
   * @return Before resource path segments which should be expanded a type filter may be used.
   * For example: ...persons?$expand=namespace.managertype/team
   */
  EdmType getStartTypeFilter();

}
