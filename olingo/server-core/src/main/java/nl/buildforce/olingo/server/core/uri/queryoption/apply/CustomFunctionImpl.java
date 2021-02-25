/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.CustomFunction;
import nl.buildforce.olingo.server.api.uri.UriParameter;

/**
 * Represents a transformation with a custom function.
 */
public class CustomFunctionImpl implements CustomFunction {

  private EdmFunction function;
  private List<UriParameter> parameters;

  @Override
  public Kind getKind() {
    return Kind.CUSTOM_FUNCTION;
  }

  @Override
  public EdmFunction getFunction() {
    return function;
  }

  public CustomFunctionImpl setFunction(EdmFunction function) {
    this.function = function;
    return this;
  }

  @Override
  public List<UriParameter> getParameters() {
    return parameters == null ?
        Collections.emptyList() :
        Collections.unmodifiableList(parameters);
  }

  public CustomFunctionImpl setParameters(List<UriParameter> parameters) {
    this.parameters = parameters;
    return this;
  }
}
