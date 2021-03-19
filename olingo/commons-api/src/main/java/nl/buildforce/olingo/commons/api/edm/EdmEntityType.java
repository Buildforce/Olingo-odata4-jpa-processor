/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.List;

/**
 * A CSDL EntityType element.
 */
public interface EdmEntityType extends EdmStructuredType {

  /**
   * Gets all key predicate names. In case an alias is defined for a key predicate this will be returned.
   *
   * @return collection of key property names of type List&lt;String&gt;
   */
  List<String> getKeyPredicateNames();

  /**
   * Get all key properties references as list of {@link EdmKeyPropertyRef}.
   *
   * @return collection of key properties of type List&lt;EdmKeyPropertyRef&gt;
   */
  List<EdmKeyPropertyRef> getKeyPropertyRefs();

  /**
   * Get a key property ref by its name.
   *
   * @param keyPredicateName name of key property
   * @return {@link EdmKeyPropertyRef} for given name
   */
  EdmKeyPropertyRef getKeyPropertyRef(String keyPredicateName);

  /**
   * Indicates if the entity type is treated as Media Link Entry with associated Media Resource.
   *
   * @return <code>true</code> if the entity type is a Media Link Entry
   */
  boolean hasStream();

  /**
   *
   * @see EdmStructuredType#getBaseType()
   *
   * @return
   */
  @Override
  EdmEntityType getBaseType();

}