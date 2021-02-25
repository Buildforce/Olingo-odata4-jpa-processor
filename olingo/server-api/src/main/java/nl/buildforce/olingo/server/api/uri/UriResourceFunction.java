/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmFunctionImport;
import nl.buildforce.olingo.commons.api.edm.EdmType;

/**
 * Used to describe an function import or bound function used within an resource path
 * For example: http://.../serviceroot/functionImport(P1=1,P2='A')
 */
public interface UriResourceFunction extends UriResourcePartTyped {

  /**
   * If the resource path specifies a function import this method will deliver the unbound function for the function
   * import.
   * @return Function used in the resource path or function import
   */
  EdmFunction getFunction();

  /**
   * Convenience method which returns the {@link EdmFunctionImport} which was used in
   * the resource path to define the {@link EdmFunction}.
   * @return Function Import used in the resource path
   */
  EdmFunctionImport getFunctionImport();

  /**
   * @return Key predicates if used, otherwise an empty list
   */
  List<UriParameter> getKeyPredicates();

  /**
   * @return List of function parameters
   */
  List<UriParameter> getParameters();

  /**
   * @return Type filter before key predicates if used, otherwise null
   */
  EdmType getTypeFilterOnCollection();

  /**
   * @return Type filter behind key predicates if used, otherwise null
   */
  EdmType getTypeFilterOnEntry();

}
