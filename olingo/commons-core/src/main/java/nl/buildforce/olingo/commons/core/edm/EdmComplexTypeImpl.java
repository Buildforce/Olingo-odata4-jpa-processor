/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlComplexType;

public class EdmComplexTypeImpl extends AbstractEdmStructuredType implements EdmComplexType {

  public EdmComplexTypeImpl(Edm edm, FullQualifiedName name, CsdlComplexType complexType) {
    super(edm, name, EdmTypeKind.COMPLEX, complexType);
  }

  @Override
  protected EdmStructuredType buildBaseType(FullQualifiedName baseTypeName) {
    EdmComplexType baseType = null;
    if (baseTypeName != null) {
      baseType = edm.getComplexType(baseTypeName);
      if (baseType == null) {
        throw new EdmException("Can't find base type with name: " + baseTypeName + " for complex type: "
            + getName());
      }
    }
    return baseType;
  }

  @Override
  public EdmComplexType getBaseType() {
    checkBaseType();
    return (EdmComplexType) baseType;
  }

  @Override
  protected void checkBaseType() {
    if (baseTypeName != null && baseType == null) {
      baseType = buildBaseType(baseTypeName);
    }
  }
}
