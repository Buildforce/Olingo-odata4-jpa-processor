/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.UriResourceLambdaVariable;

public class UriResourceLambdaVarImpl extends UriResourceTypedImpl implements UriResourceLambdaVariable {

  private final String variableText;
  private final EdmType type;

  public UriResourceLambdaVarImpl(String variableText, EdmType type) {
    super(UriResourceKind.lambdaVariable);
    this.variableText = variableText;
    this.type = type;
  }

  @Override
  public String getVariableName() {
    return variableText;
  }

  @Override
  public EdmType getType() {
    return type;
  }

  @Override
  public boolean isCollection() {
    return false;
  }

  @Override
  public String getSegmentValue() {
    return variableText;
  }
}
