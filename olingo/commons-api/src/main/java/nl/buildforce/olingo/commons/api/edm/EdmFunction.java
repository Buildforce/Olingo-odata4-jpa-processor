/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * An EdmFunction as described in the OData specification
 */
public interface EdmFunction extends EdmOperation {

  /**
   * @return true if this function is composable
   */
  boolean isComposable();

}
