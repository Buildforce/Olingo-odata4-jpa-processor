/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmFunctionImport;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlFunctionImport;

public class EdmFunctionImportImpl extends AbstractEdmOperationImport implements EdmFunctionImport {

  private final CsdlFunctionImport functionImport;

  public EdmFunctionImportImpl(Edm edm, EdmEntityContainer container,
                               CsdlFunctionImport functionImport) {
    super(edm, container, functionImport);
    this.functionImport = functionImport;
  }

  @Override
  public FullQualifiedName getFunctionFqn() {
    return functionImport.getFunctionFQN();
  }

  @Override
  public EdmFunction getUnboundFunction(List<String> parameterNames) {
    return edm.getUnboundFunction(getFunctionFqn(), parameterNames);
  }

  @Override
  public List<EdmFunction> getUnboundFunctions() {
    return edm.getUnboundFunctions(getFunctionFqn());
  }

  @Override
  public boolean isIncludeInServiceDocument() {
    return functionImport.isIncludeInServiceDocument();
  }
  
  @Override
  public String getTitle() {
    return functionImport.getTitle();
  }
}
