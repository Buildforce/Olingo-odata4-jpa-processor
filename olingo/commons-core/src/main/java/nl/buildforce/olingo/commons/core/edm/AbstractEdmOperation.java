/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmBindingTarget;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmOperation;
import nl.buildforce.olingo.commons.api.edm.EdmParameter;
import nl.buildforce.olingo.commons.api.edm.EdmReturnType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlOperation;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlParameter;

public abstract class AbstractEdmOperation extends EdmTypeImpl implements EdmOperation {

  private final CsdlOperation operation;
  private Map<String, EdmParameter> parameters;
  private List<String> parameterNames;
  private EdmReturnType returnType;

  protected AbstractEdmOperation(Edm edm, FullQualifiedName name, CsdlOperation operation,
                                 EdmTypeKind kind) {

    super(edm, name, kind, operation);
    this.operation = operation;
  }

  @Override
  public EdmParameter getParameter(String name) {
    if (parameters == null) {
      createParameters();
    }
    return parameters.get(name);
  }

  @Override
  public List<String> getParameterNames() {
    if (parameterNames == null) {
      createParameters();
    }
    return Collections.unmodifiableList(parameterNames);
  }

  private void createParameters() {
    if (parameters == null) {
      Map<String, EdmParameter> parametersLocal = new LinkedHashMap<>();
      List<CsdlParameter> providerParameters = operation.getParameters();
      if (providerParameters != null) {
        List<String> parameterNamesLocal = new ArrayList<>(providerParameters.size());
        for (CsdlParameter parameter : providerParameters) {
          parametersLocal.put(parameter.getName(), new EdmParameterImpl(edm, parameter));
          parameterNamesLocal.add(parameter.getName());
        }

        parameters = parametersLocal;
        parameterNames = parameterNamesLocal;
      } else {
        parameterNames = Collections.emptyList();
      }
    }
  }

  @Override
  public EdmEntitySet getReturnedEntitySet(EdmEntitySet bindingParameterEntitySet) {
    EdmEntitySet returnedEntitySet = null;
    if (bindingParameterEntitySet != null && operation.getEntitySetPath() != null) {
      EdmBindingTarget relatedBindingTarget =
          bindingParameterEntitySet.getRelatedBindingTarget(operation.getEntitySetPath());
      if (relatedBindingTarget == null) {
        throw new EdmException("Cannot find entity set with path: " + operation.getEntitySetPath());
      }
      if (relatedBindingTarget instanceof EdmEntitySet) {
        returnedEntitySet = (EdmEntitySet) relatedBindingTarget;
      } else {
        throw new EdmException("BindingTarget with name: " + relatedBindingTarget.getName()
            + " must be an entity set");
      }
    }
    return returnedEntitySet;
  }

  @Override
  public EdmReturnType getReturnType() {
    if (returnType == null && operation.getReturnType() != null) {
      returnType = new EdmReturnTypeImpl(edm, operation.getReturnType());
    }
    return returnType;
  }

  @Override
  public boolean isBound() {
    return operation.isBound();
  }

  @Override
  public FullQualifiedName getBindingParameterTypeFqn() {
    if (isBound()) {
      CsdlParameter bindingParameter = operation.getParameters().get(0);
      return bindingParameter.getTypeFQN();
    }
    return null;
  }

  @Override
  public Boolean isBindingParameterTypeCollection() {
    if (isBound()) {
      CsdlParameter bindingParameter = operation.getParameters().get(0);
      return bindingParameter.isCollection();
    }
    return null;
  }

  @Override
  public String getEntitySetPath() {
    return operation.getEntitySetPath();
  }
}
