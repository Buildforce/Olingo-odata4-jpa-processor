/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmActionImport;

/**
 * Used to describe an action used within an resource path
 * For example: http://.../serviceroot/action()
 */
public interface UriResourceAction extends UriResourcePartTyped {

  /**
   * If the resource path specifies an action import this method will deliver the unbound action for the action import.
   * @return Action used in the resource path or action import
   */
  EdmAction getAction();

  /**
   * Convenience method which returns the {@link EdmActionImport} which was used in
   * the resource path to define the {@link EdmAction}.
   * @return Action Import used in the resource path
   */
  EdmActionImport getActionImport();

}
