/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

/**
 * Defining the various info kinds
 */
public enum UriInfoKind {

  /**
   * Class: {@link UriInfoAll}<br>
   * URI: http://.../serviceroot/$all
   */
  all,

  /**
   * Class: {@link UriInfoBatch}<br>
   * URI: http://.../serviceroot/$batch
   */
  batch,

  /**
   * Class: {@link UriInfoCrossjoin}<br>
   * URI: http://.../serviceroot/$crossjoin
   */
  crossjoin,

  /**
   * Class: {@link UriInfoEntityId}<br>
   * URI: http://.../serviceroot/$entity(...)
   */
  entityId,

  /**
   * Class: {@link UriInfoMetadata}<br>
   * URI: http://.../serviceroot/$metadata...
   */
  metadata,

  /**
   * Class: {@link UriInfoResource}<br>
   * URI: http://.../serviceroot/entitySet
   */
  resource,

  /**
   * Class: {@link UriInfoService}<br>
   * URI: http://.../serviceroot
   */
  service
}
