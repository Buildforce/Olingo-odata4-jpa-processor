/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * EdmMappable can be applied to CSDL elements to associate additional information.
 */
public interface EdmMappable {

  /**
   * Get mapping information applied to an EDM element. May return null if no mapping is defined.
   *
   * @return {@link EdmMapping} or <b>null</b> if no mapping is defined
   */
  EdmMapping getMapping();
}
