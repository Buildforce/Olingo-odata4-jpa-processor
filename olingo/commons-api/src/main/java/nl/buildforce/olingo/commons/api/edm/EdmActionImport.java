/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * An EdmActionImport element.
 */
public interface EdmActionImport extends EdmOperationImport {

  /**
   * Gets unbound action.
   *
   * @return unbound action.
   */
  EdmAction getUnboundAction();
}
