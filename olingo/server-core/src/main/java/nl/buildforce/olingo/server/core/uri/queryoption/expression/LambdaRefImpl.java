/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.expression;

import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.LambdaRef;

public class LambdaRefImpl implements LambdaRef {

  private final String variableText;

  public LambdaRefImpl(String text) {
    variableText = text;
  }

  @Override
  public String getVariableName() {
    return variableText;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) throws ODataApplicationException {
    return visitor.visitLambdaReference(variableText);
  }

  @Override
  public String toString() {
    return variableText;
  }

}