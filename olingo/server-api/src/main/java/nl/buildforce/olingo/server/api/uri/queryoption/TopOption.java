/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

/**
 * Represents the system query option $top
 * For example: http://.../entitySet?$top=10
 */
public interface TopOption extends SystemQueryOption {

  /**
   * @return Value of $top
   */
  int getValue();

}
