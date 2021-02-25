/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmOperationImport;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlOperationImport;

public abstract class AbstractEdmOperationImport extends AbstractEdmNamed implements EdmOperationImport {

  protected final EdmEntityContainer container;
  private final Target entitySet;
  private EdmEntitySet returnedEntitySet;

  public AbstractEdmOperationImport(Edm edm, EdmEntityContainer container,
                                    CsdlOperationImport operationImport) {
    super(edm, operationImport.getName(), operationImport);
    this.container = container;
    if (operationImport.getEntitySet() != null) {
      entitySet = new Target(operationImport.getEntitySet(), container);
    } else {
      entitySet = null;
    }
  }

  @Override
  public FullQualifiedName getFullQualifiedName() {
    return new FullQualifiedName(container.getNamespace(), getName());
  }

  @Override
  public EdmEntitySet getReturnedEntitySet() {
    if (entitySet != null && returnedEntitySet == null) {
      EdmEntityContainer entityContainer = edm.getEntityContainer(entitySet.getEntityContainer());
      if (entityContainer == null) {
        throw new EdmException("Can´t find entity container with name: " + entitySet.getEntityContainer());
      }
      returnedEntitySet = entityContainer.getEntitySet(entitySet.getTargetName());
      if (returnedEntitySet == null) {
        throw new EdmException("Can´t find entity set with name: " + entitySet.getTargetName());
      }
    }
    return returnedEntitySet;
  }

  @Override
  public EdmEntityContainer getEntityContainer() {
    return container;
  }
}
