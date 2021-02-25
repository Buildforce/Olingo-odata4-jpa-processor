/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmActionImport;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlActionImport;

public class EdmActionImportImpl extends AbstractEdmOperationImport implements EdmActionImport {

  private final CsdlActionImport actionImport;

  public EdmActionImportImpl(Edm edm, EdmEntityContainer container, CsdlActionImport actionImport) {

    super(edm, container, actionImport);
    this.actionImport = actionImport;
  }

  @Override
  public EdmAction getUnboundAction() {
    return edm.getUnboundAction(actionImport.getActionFQN());
  }
}
