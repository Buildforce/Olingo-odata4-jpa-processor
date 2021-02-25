/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * EdmNamed is the base interface for nearly all CSDL constructs.
 */
public interface EdmNamed {

  /**
   * @return name as String
   */
  String getName();
}
