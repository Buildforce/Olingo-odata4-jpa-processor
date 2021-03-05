/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.expression;

import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.AliasQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Alias;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;

public class AliasImpl implements Alias {

  private final String parameterName;
  private final AliasQueryOption alias;

  public AliasImpl(String parameterName, AliasQueryOption alias) {
    this.parameterName = parameterName;
    this.alias = alias;
  }

  @Override
  public String getParameterName() {
    return parameterName;
  }

  public AliasQueryOption getAlias() {
    return alias;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) throws ODataApplicationException {
    return visitor.visitAlias(parameterName);
  }

  @Override
  public String toString() {
    return parameterName;
  }

}