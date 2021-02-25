/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;

public class EdmTypeImpl extends AbstractEdmNamed implements EdmType {

  protected final FullQualifiedName typeName;
  protected final EdmTypeKind kind;

  public EdmTypeImpl(Edm edm, FullQualifiedName typeName, EdmTypeKind kind,
                     CsdlAnnotatable annotatable) {
    super(edm, typeName.getName(), annotatable);
    this.typeName = typeName;
    this.kind = kind;
  }

  @Override
  public FullQualifiedName getFullQualifiedName() {
    return typeName;
  }

  @Override
  public String getNamespace() {
    return typeName.getNamespace();
  }

  @Override
  public EdmTypeKind getKind() {
    return kind;
  }
}
