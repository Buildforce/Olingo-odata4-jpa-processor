/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmKeyPropertyRef;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlPropertyRef;

public class EdmKeyPropertyRefImpl implements EdmKeyPropertyRef {

  private final CsdlPropertyRef ref;
  private final EdmEntityType edmEntityType;
  private EdmProperty property;

  public EdmKeyPropertyRefImpl(EdmEntityType edmEntityType, CsdlPropertyRef ref) {
    this.edmEntityType = edmEntityType;
    this.ref = ref;
  }

  @Override
  public String getName() {
    return ref.getName();
  }

  @Override
  public String getAlias() {
    return ref.getAlias();
  }

  @Override
  public EdmProperty getProperty() {
    if (property == null) {
      if (getAlias() == null) {
        property = edmEntityType.getStructuralProperty(getName());
        if (property == null) {
          throw new EdmException("Invalid key property ref specified. Can´t find property with name: "
              + getName());
        }
      } else {
        if (getName() == null || getName().isEmpty()) {
          throw new EdmException("Alias but no path specified for propertyRef");
        }
        String[] splitPath = getName().split("/");
        EdmStructuredType structType = edmEntityType;
        for (int i = 0; i < splitPath.length - 1; i++) {
          EdmProperty _property = structType.getStructuralProperty(splitPath[i]);
          if (_property == null) {
            throw new EdmException("Invalid property ref specified. Can´t find property with name: " + splitPath[i]
                + " at type: " + structType.getNamespace() + "." + structType.getName());
          }
          structType = (EdmStructuredType) _property.getType();
        }
        property = structType.getStructuralProperty(splitPath[splitPath.length - 1]);
        if (property == null) {
          throw new EdmException("Invalid property ref specified. Can´t find property with name: "
              + splitPath[splitPath.length - 1] + " at type: " + structType.getNamespace() + "."
              + structType.getName());
        }
      }
    }

    return property;
  }
}