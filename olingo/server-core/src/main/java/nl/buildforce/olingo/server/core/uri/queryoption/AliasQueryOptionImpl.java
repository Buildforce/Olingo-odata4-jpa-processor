/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.AliasQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

public class AliasQueryOptionImpl extends QueryOptionImpl implements AliasQueryOption {

  private Expression aliasValue;

  @Override
  public Expression getValue() {
    return aliasValue;
  }

  public AliasQueryOptionImpl setAliasValue(Expression aliasValue) {
    this.aliasValue = aliasValue;
    return this;
  }

}
