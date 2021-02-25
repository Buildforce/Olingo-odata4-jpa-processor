/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

/**
 * Super interface used for any query option
 */
public interface QueryOption {

  /**
   * @return Name of query option
   */
  String getName();

  /**
   * @return Value of query option
   */
  String getText();

}