/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

/**
 * Super interface used for any system query option
 */
public interface SystemQueryOption extends QueryOption {

  /**
   * @return Kind of system query option
   */
  SystemQueryOptionKind getKind();
}
