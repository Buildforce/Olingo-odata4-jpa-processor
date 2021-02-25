/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.AliasQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOption;

/**
 * <p>Object acting as general access to URI information extracted from the request URI.</p>
 * <p>Depending on the URI info kind different interfaces are used to provide access to that information.
 * Use method {@link #getKind()} to obtain URI info kind information and to perform an appropriate cast.</p>
 */
public interface UriInfo extends UriInfoService, UriInfoMetadata, UriInfoResource, UriInfoBatch,
UriInfoAll, UriInfoCrossjoin, UriInfoEntityId {

  /**
   * See {@link UriInfoKind} for more details which kinds are allowed.
   * @return the kind of this URI info object.
   */
  UriInfoKind getKind();

  /**
   * Convenience casting method.
   * @return this as a {@link UriInfoService} object
   */
  UriInfoService asUriInfoService();

  /**
   * Convenience casting method.
   * @return this as a {@link UriInfoAll} object
   */
  UriInfoAll asUriInfoAll();

  /**
   * Convenience casting method.
   * @return this as a {@link UriInfoBatch} object
   */
  UriInfoBatch asUriInfoBatch();

  /**
   * Convenience casting method.
   * @return this as a {@link UriInfoCrossjoin} object
   */
  UriInfoCrossjoin asUriInfoCrossjoin();

  /**
   * Convenience casting method.
   * @return this as a {@link UriInfoEntityId} object
   */
  UriInfoEntityId asUriInfoEntityId();

  /**
   * Convenience casting method.
   * @return this as a {@link UriInfoMetadata} object
   */
  UriInfoMetadata asUriInfoMetadata();

  /**
   * Convenience casting method.
   * @return this as a {@link UriInfoResource} object
   */
  UriInfoResource asUriInfoResource();

  /**
   * Gets a list of all system query options which were in the URI.
   * @return a list of all system query options used
   */
  List<SystemQueryOption> getSystemQueryOptions();

  /**
   * Gets a list of all alias definitions which were in the URI (including aliases not used anywhere).
   * @return a list of all alias definitions
   */
  List<AliasQueryOption> getAliases();
}
