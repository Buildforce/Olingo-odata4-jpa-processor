/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmReturnType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlFunction;

public class EdmFunctionImpl extends AbstractEdmOperation implements EdmFunction {

  private final CsdlFunction function;

  public EdmFunctionImpl(Edm edm, FullQualifiedName name, CsdlFunction function) {
    super(edm, name, function, EdmTypeKind.FUNCTION);
    this.function = function;
  }

  @Override
  public boolean isComposable() {
    return function.isComposable();
  }

  @Override
  public EdmReturnType getReturnType() {
    EdmReturnType returnType = super.getReturnType();
    if (returnType == null) {
      throw new EdmException("ReturnType for a function must not be null");
    }
    return returnType;
  }

}
