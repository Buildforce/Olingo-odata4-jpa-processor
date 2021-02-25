/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * A CSDL EntitySet element.
 * <br/>
 * EdmEntitySet is the container for entity type instances as described in the OData protocol. It can be the target of a
 * navigation property binding.
 */
public interface EdmEntitySet extends EdmBindingTarget {

  /**
   * @return true if entity set must be included in the service document
   */
  boolean isIncludeInServiceDocument();

}
