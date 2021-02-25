/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.EdmNavigationPropertyBinding;

public class EdmNavigationPropertyBindingImpl implements EdmNavigationPropertyBinding {

  private final String path;
  private final String target;

  public EdmNavigationPropertyBindingImpl(String path, String target) {
    this.path = path;
    this.target = target;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public String getTarget() {
    return target;
  }

}
