/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * A key property reference element.
 */
public interface EdmKeyPropertyRef {

  /**
   * @return name of the key predicate. Can be a path in case the alias is set.
   */
  String getName();

  /**
   * @return alias of this reference or null if not set
   */
  String getAlias();

  /**
   * @return the property this property ref points to
   */
  EdmProperty getProperty();

}
