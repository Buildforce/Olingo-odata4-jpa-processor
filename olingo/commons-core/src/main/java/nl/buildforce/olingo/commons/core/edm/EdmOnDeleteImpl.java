/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmOnDelete;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlOnDelete;

public class EdmOnDeleteImpl extends AbstractEdmAnnotatable implements EdmOnDelete {
  
  private final CsdlOnDelete csdlOnDelete;
  
  public EdmOnDeleteImpl(Edm edm, CsdlOnDelete csdlOnDelete) {
    super(edm, csdlOnDelete);
    this.csdlOnDelete = csdlOnDelete;
  }

  @Override
  public String getAction() {
    return csdlOnDelete.getAction().name();
  }

}
