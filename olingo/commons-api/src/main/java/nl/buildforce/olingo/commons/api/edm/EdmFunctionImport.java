/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.List;

/**
 * A CSDL FunctionImport element
 */
public interface EdmFunctionImport extends EdmOperationImport {

  /**
   * Gets unbound functions.
   *
   * @return unbound functions
   */
  List<EdmFunction> getUnboundFunctions();

  /**
   * Gets unbound function with given parameter names.
   *
   * @param parameterNames parameter names
   * @return unbound function with given parameter names
   */
  EdmFunction getUnboundFunction(List<String> parameterNames);

  /**
   * @return the Full qualified name for the function as specified in the metadata
   */
  FullQualifiedName getFunctionFqn();

  /**
   * Returns a human readable title or null if not set.
   * @return a human readable title or null
   */
  String getTitle();
  
  /**
   * @return true if the function import must be included in the service document
   */
  boolean isIncludeInServiceDocument();

}
