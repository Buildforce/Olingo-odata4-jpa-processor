/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * An Edm target element. It contains a target as a String name as well as the {@link FullQualifiedName} of the entity
 * container it is contained in.
 */
public class Target {

  private final String targetName;
  private final FullQualifiedName entityContainer;

  public Target(String target, EdmEntityContainer defaultContainer) {
    String[] bindingTargetParts = target.split("/");
    if (bindingTargetParts.length == 1) {
      entityContainer = defaultContainer.getFullQualifiedName();
      targetName = bindingTargetParts[0];
    } else {
      entityContainer = new FullQualifiedName(bindingTargetParts[0]);
      targetName = bindingTargetParts[1];
    }
  }

  /**
   * @return name of the target as a String
   */
  public String getTargetName() {
    return targetName;
  }

  /**
   * @return {@link FullQualifiedName} of the entity container this target is contained in.
   */
  public FullQualifiedName getEntityContainer() {
    return entityContainer;
  }

  @Override
  public String toString() {
    if (entityContainer == null) {
      return targetName;
    }
    return entityContainer.getFullQualifiedNameAsString() + "/" + targetName;
  }

}