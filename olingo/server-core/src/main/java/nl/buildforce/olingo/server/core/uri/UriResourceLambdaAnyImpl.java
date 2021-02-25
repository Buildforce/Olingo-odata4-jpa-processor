/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.UriResourceLambdaAny;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

public class UriResourceLambdaAnyImpl extends UriResourceTypedImpl implements UriResourceLambdaAny {

  private final String lambdaVariable;
  private final Expression expression;

  public UriResourceLambdaAnyImpl(String lambdaVariable, Expression expression) {
    super(UriResourceKind.lambdaAny);
    this.lambdaVariable = lambdaVariable;
    this.expression = expression;
  }

  @Override
  public EdmType getType() {
    return EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Boolean);
  }

  @Override
  public boolean isCollection() {
    return false;
  }

  @Override
  public String getLambdaVariable() {
    return lambdaVariable;
  }

  @Override
  public Expression getExpression() {
    return expression;
  }

  @Override
  public String getSegmentValue() {
    return "any";
  }
}
