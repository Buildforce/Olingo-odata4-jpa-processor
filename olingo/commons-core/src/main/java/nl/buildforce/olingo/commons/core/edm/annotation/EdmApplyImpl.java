/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmApply;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlApply;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlExpression;

public class EdmApplyImpl extends AbstractEdmAnnotatableDynamicExpression implements EdmApply {

  private final CsdlApply csdlExp;

  private String function;
  private List<EdmExpression> parameters;

  public EdmApplyImpl(Edm edm, CsdlApply csdlExp) {
    super(edm, "Apply", csdlExp);
    this.csdlExp = csdlExp;
  }

  @Override
  public String getFunction() {
    if (function == null) {
      if (csdlExp.getFunction() == null) {
        throw new EdmException("An Apply expression must specify a function.");
      }
      function = csdlExp.getFunction();
    }
    return function;
  }

  @Override
  public List<EdmExpression> getParameters() {
    if (parameters == null) {
      List<EdmExpression> localParameters = new ArrayList<>();
      if (csdlExp.getParameters() != null) {
        for (CsdlExpression param : csdlExp.getParameters()) {
          localParameters.add(getExpression(edm, param));
        }
      }
      parameters = Collections.unmodifiableList(localParameters);
    }
    return parameters;
  }

  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.Apply;
  }
}