/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.server.api.uri.queryoption.CustomQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FormatOption;
import nl.buildforce.olingo.server.api.uri.queryoption.IdOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;

/**
 * Used for URI info kind {@link UriInfoKind#entityId} to describe URIs like
 * http://.../serviceroot/$entity...
 */
public interface UriInfoEntityId {

  /**
   * @return List of custom query options used in the URI (without alias definitions)
   */
  // List<CustomQueryOption> getCustomQueryOptions();

  /**
   * Behind $entity a optional type cast can be used in the URI.
   * For example: http://.../serviceroot/$entity/namespace.entitytype
   * @return Type cast if found, otherwise null
   */
  EdmEntityType getEntityTypeCast();

  /**
   * @return Object containing information of the $expand option
   */
  ExpandOption getExpandOption();

  /**
   * @return Object containing information of the $format option
   */
  // FormatOption getFormatOption();

  /**
   * @return Object containing information of the $id option
   */
  IdOption getIdOption();

  /**
   * @return Object containing information of the $select option
   */
  SelectOption getSelectOption();

}