/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

/**
 * Represents the system query option $skiptoken
 * For example: http://.../entitySet?$skiptoken=abv
 */
public interface SkipTokenOption extends SystemQueryOption {

  /**
   * @return Value of $skiptoken
   */
  String getValue();

}
