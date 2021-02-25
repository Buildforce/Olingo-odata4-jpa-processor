/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntitySet;

public class EdmEntitySetImpl extends AbstractEdmBindingTarget implements EdmEntitySet {

  private final CsdlEntitySet entitySet;

  public EdmEntitySetImpl(Edm edm, EdmEntityContainer container, CsdlEntitySet entitySet) {
    super(edm, container, entitySet);
    this.entitySet = entitySet;
  }

  @Override
  public boolean isIncludeInServiceDocument() {
    return entitySet.isIncludeInServiceDocument();
  }

}