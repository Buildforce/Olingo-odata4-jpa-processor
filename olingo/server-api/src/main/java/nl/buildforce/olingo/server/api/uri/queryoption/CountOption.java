/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

/**
 * Represents the system query option $count
 * For example:
 * http://.../entitySet?$count=true
 */
public interface CountOption extends SystemQueryOption {

  /**
   * @return Value of $count
   */
  boolean getValue();

}
