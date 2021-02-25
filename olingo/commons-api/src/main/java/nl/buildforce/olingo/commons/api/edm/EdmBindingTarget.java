/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.List;

/**
 * Entity Sets or Singletons can be bound to each other using a navigation property binding so an
 * {@link EdmBindingTarget} can either be an {@link EdmEntitySet} or an {@link EdmSingleton}.
 */
public interface EdmBindingTarget extends EdmNamed, EdmAnnotatable, EdmMappable {

  /**
   * Returns a human readable title or null if not set.
   * @return a human readable title or null
   */
  String getTitle();
  
  /**
   * Returns the target for a given path.
   *
   * @param path path for which the target is returned
   * @return {@link EdmBindingTarget}
   */
  EdmBindingTarget getRelatedBindingTarget(String path);

  /**
   * @return all navigation property bindings
   */
  List<EdmNavigationPropertyBinding> getNavigationPropertyBindings();

  /**
   * Returns the entity container this target is contained in.
   *
   * @return {@link EdmEntityContainer}
   */
  EdmEntityContainer getEntityContainer();

  /**
   * Get the entity type.
   *
   * @return {@link EdmEntityType}
   */
  EdmEntityType getEntityType();
  
  /**
   * Get the entity type with annotations defined in external file.
   *
   * @return {@link EdmEntityType}
   */
  EdmEntityType getEntityTypeWithAnnotations();
}
