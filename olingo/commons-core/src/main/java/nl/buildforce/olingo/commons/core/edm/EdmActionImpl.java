/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAction;

public class EdmActionImpl extends AbstractEdmOperation implements EdmAction {

  public EdmActionImpl(Edm edm, FullQualifiedName name, CsdlAction action) {
    super(edm, name, action, EdmTypeKind.ACTION);
  }
}
