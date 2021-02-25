/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;

/**
 * Abstract singleton implementation of the EDM primitive-type interface.
 */
public abstract class SingletonPrimitiveType extends AbstractPrimitiveType {

  protected String name;

  @Override
  public boolean equals(Object obj) {
    return this == obj || obj != null && getClass() == obj.getClass();
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String getNamespace() {
    return EDM_NAMESPACE;
  }

  @Override
  public String getName() {
    if(name == null) {
      name = getClass().getSimpleName().substring(3);
    }
    return name;
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.PRIMITIVE;
  }
}
