/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmFunctionImport;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriResourceFunction;
import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

/**
 * Covers Function imports and BoundFunction in URI
 */
public class UriResourceFunctionImpl extends UriResourceWithKeysImpl implements UriResourceFunction {

  private final EdmFunctionImport functionImport;
  private final EdmFunction function;
  private final List<UriParameter> parameters;

  public UriResourceFunctionImpl(EdmFunctionImport edmFunctionImport, EdmFunction function,
                                 List<UriParameter> parameters) {
    super(UriResourceKind.function);
    functionImport = edmFunctionImport;
    this.function = function;
    this.parameters = parameters;
  }

  @Override
  public List<UriParameter> getParameters() {
    return parameters == null ?
        Collections.emptyList() :
          Collections.unmodifiableList(parameters);
  }

  @Override
  public EdmFunction getFunction() {
    return function;
  }

  @Override
  public EdmFunctionImport getFunctionImport() {
    return functionImport;
  }

  @Override
  public EdmType getType() {
    return function.getReturnType().getType();
  }

  @Override
  public boolean isCollection() {
    return keyPredicates == null && function.getReturnType().isCollection();
  }

  @Override
  public String getSegmentValue() {
    return functionImport == null ? (function == null ? "" : function.getName()) : functionImport.getName();
  }
}
