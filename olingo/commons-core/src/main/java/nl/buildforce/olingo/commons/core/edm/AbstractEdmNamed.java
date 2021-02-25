/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmNamed;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;

public abstract class AbstractEdmNamed extends AbstractEdmAnnotatable implements EdmNamed {

  private final String name;

  public AbstractEdmNamed(Edm edm, String name, CsdlAnnotatable annotatable) {
    super(edm, annotatable);
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

}
