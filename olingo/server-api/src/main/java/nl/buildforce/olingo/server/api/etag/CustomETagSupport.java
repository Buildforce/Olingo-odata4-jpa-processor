/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.etag;

import nl.buildforce.olingo.commons.api.edm.EdmBindingTarget;
import nl.buildforce.olingo.server.api.OlingoExtension;

/**
 * <p>Processors that would like to support etags for certain entity sets can implement this
 * interface.</p>
 * <p>If implemented this interface can be registered at the ODataHttpHandler. This will result in change request to
 * require an if-match/if-none-match or an if-modified-since/if-unmodified-since header. Otherwise the request will
 * result in a "Precondition Required" response</p>
 */
public interface CustomETagSupport extends OlingoExtension {

  /**
   * This method will be called for update requests which target an entity or a property of an entity.
   * If this method returns true and an header is not specified we will return a "Precondition Required" response.
   * Validation has to be performed inside the processor methods after the dispatching.
   * If this method returns false and an header is specified we will ignore the header.
   * @param entitySetOrSingleton
   * @return true if the entity set specified needs an if-match/if-none-match header
   */
  boolean hasETag(EdmBindingTarget entitySetOrSingleton);

  /**
   * This method will be called for update requests which target a media entity value.
   * If this method returns true and an header is not specified we will return a "Precondition Required" response.
   * Validation has to be performed inside the processor methods after the dispatching.
   * If this method returns false and an header is specified we will ignore the header.
   * @param entitySetOrSingleton
   * @return true if the entity set specified needs an if-match/if-none-match header
   */
  boolean hasMediaETag(EdmBindingTarget entitySetOrSingleton);
}
