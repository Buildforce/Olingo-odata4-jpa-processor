/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

/**
 * Represents the system query option $skip
 * For example: http://.../entitySet?$skip=10
 */
public interface SkipOption extends SystemQueryOption {

  /**
   * @return Value of $skip
   */
  int getValue();

}
