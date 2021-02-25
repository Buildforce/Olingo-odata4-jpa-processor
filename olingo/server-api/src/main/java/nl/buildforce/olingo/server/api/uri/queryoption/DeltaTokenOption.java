/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

public interface DeltaTokenOption extends SystemQueryOption {
  
  /**
   * @return Value of $deltatoken
   */
  String getValue();

}