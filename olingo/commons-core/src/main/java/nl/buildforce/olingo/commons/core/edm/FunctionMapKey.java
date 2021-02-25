/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

public final class FunctionMapKey {

  private final FullQualifiedName functionName;

  private final FullQualifiedName bindingParameterTypeName;

  private final Boolean isBindingParameterCollection;

  private final List<String> parameterNames;

  public FunctionMapKey(FullQualifiedName functionName, FullQualifiedName bindingParameterTypeName,
                        Boolean isBindingParameterCollection, List<String> parameterNames) {

    this.functionName = functionName;
    if (bindingParameterTypeName != null && isBindingParameterCollection == null) {
      throw new EdmException(
          "Indicator that the bindingparameter is a collection must not be null if its an bound function.");
    }
    this.bindingParameterTypeName = bindingParameterTypeName;
    this.isBindingParameterCollection = isBindingParameterCollection;
    this.parameterNames = new ArrayList<>();
    if (parameterNames != null) {
      this.parameterNames.addAll(parameterNames);
      Collections.sort(this.parameterNames);
    }
  }

  @Override
  public int hashCode() {
    int result = functionName != null ? functionName.hashCode() : 0;
    result = 31 * result + (bindingParameterTypeName != null ? bindingParameterTypeName.hashCode() : 0);
    result = 31 * result + (isBindingParameterCollection != null ? isBindingParameterCollection.hashCode() : 0);
    result = 31 * result + parameterNames.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof FunctionMapKey)) {
      return false;
    }
    FunctionMapKey other = (FunctionMapKey) obj;

    if (functionName.equals(other.functionName)
        && (bindingParameterTypeName == null && other.bindingParameterTypeName == null)
        || (bindingParameterTypeName != null && bindingParameterTypeName.equals(other.bindingParameterTypeName))
        && (isBindingParameterCollection == null
        && other.isBindingParameterCollection == null)
        || (isBindingParameterCollection != null
        && isBindingParameterCollection.equals(other.isBindingParameterCollection))
            && parameterNames.size() == other.parameterNames.size()) {

      for (String name : parameterNames) {
        if (!other.parameterNames.contains(name)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
}
