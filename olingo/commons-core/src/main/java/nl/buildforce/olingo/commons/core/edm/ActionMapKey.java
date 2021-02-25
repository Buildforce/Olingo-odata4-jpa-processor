/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

public class ActionMapKey {

  private final FullQualifiedName actionName;

  private final FullQualifiedName bindingParameterTypeName;

  private final Boolean isBindingParameterCollection;

  public ActionMapKey(FullQualifiedName actionName, FullQualifiedName bindingParameterTypeName,
                      Boolean isBindingParameterCollection) {

    if (actionName == null || bindingParameterTypeName == null || isBindingParameterCollection == null) {
      throw new EdmException("Action name, binding parameter type and binding parameter collection "
          + "must not be null for bound actions");
    }
    this.actionName = actionName;
    this.bindingParameterTypeName = bindingParameterTypeName;
    this.isBindingParameterCollection = isBindingParameterCollection;
  }

  @Override
  public int hashCode() {
    String forHash = actionName.toString()
        + bindingParameterTypeName
        + isBindingParameterCollection;
    return forHash.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ActionMapKey)) {
      return false;
    }
    ActionMapKey other = (ActionMapKey) obj;
    return actionName.equals(other.actionName) && bindingParameterTypeName.equals(other.bindingParameterTypeName)
            && isBindingParameterCollection.equals(other.isBindingParameterCollection);
  }

}