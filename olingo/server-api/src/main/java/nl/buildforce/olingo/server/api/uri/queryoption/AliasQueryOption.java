/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents an alias value defined as query option
 * For example:
 * http://.../?filter=@value eq name&@value='test'
 */
public interface AliasQueryOption extends QueryOption {

  /**
   * @return Value of the alias
   */
  Expression getValue();
}
