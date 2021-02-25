/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmSingleton;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlSingleton;

public class EdmSingletonImpl extends AbstractEdmBindingTarget implements EdmSingleton {

  public EdmSingletonImpl(Edm edm, EdmEntityContainer container, CsdlSingleton singleton) {
    super(edm, container, singleton);
  }
}
