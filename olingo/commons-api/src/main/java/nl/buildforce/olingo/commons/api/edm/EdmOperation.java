/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.List;

/**
 * A EdmOperation can either be an {@link EdmAction} or an {@link EdmFunction}.
 */
public interface EdmOperation extends EdmType, EdmAnnotatable {

  /**
   * Get parameter for given name
   * @param name name of parameter
   * @return {@link EdmParameter} for this name
   */
  EdmParameter getParameter(String name);

  /**
   * A list of all parameter names. If this is a bound action or function the first parameter name in the list is the
   * binding parameter
   * @return a list of all parameter names
   */
  List<String> getParameterNames();

  /**
   * Get EdmEntitySet for the given binding parameters EntitySet
   *
   * @param bindingParameterEntitySet EntitySet of binding parameter
   * @return {@link EdmEntitySet} for this binding
   */
  EdmEntitySet getReturnedEntitySet(EdmEntitySet bindingParameterEntitySet);

  /**
   * @return {@link EdmReturnType} of this operation
   */
  EdmReturnType getReturnType();

  /**
   * For more information on bound operations please refer to the OData V4 specification.
   *
   * @return true if bound
   */
  boolean isBound();

  /**
   * @return the fullqualified type name of the binding parameter
   */
  FullQualifiedName getBindingParameterTypeFqn();

  /**
   * @return true if binding parameter is of type collection.
   */
  Boolean isBindingParameterTypeCollection();

  /**
   * @return the entity set path as a String or null if not present
   */
  String getEntitySetPath();

}
