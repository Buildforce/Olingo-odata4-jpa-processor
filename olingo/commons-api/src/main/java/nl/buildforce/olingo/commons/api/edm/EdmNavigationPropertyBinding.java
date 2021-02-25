/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * A navigation property binding which binds entity sets or singletons with each other.
 */
public interface EdmNavigationPropertyBinding {

  /**
   * A path contains the full qualified name of the type it is referring to as a first segment. If it is a type
   * nested inside another type the path is separated by forward slashes.
   * @return path which leads to the target.
   */
  String getPath();

  /**
   * @return the entity set or singleton this binding refers to.
   */
  String getTarget();

}
