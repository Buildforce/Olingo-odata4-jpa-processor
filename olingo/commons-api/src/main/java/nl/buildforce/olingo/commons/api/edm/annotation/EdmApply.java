/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

import java.util.List;

import nl.buildforce.olingo.commons.api.Constants;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;

/**
 * Represents an Edm:Apply expression
 */
public interface EdmApply extends EdmDynamicExpression, EdmAnnotatable {

  /**
   * A QualifiedName specifying the name of the client-side function to apply.
   * <br/>
   * OData defines three canonical functions. Services MAY support additional functions that MUST be qualified with a
   * namespace or alias other than odata. Function names qualified with odata are reserved for this specification and
   * its future versions.
   *
   * @see Constants#CANONICAL_FUNCTION_CONCAT
   * @see Constants#CANONICAL_FUNCTION_FILLURITEMPLATE
   * @see Constants#CANONICAL_FUNCTION_URIENCODE
   * @return function full qualified name
   */
  String getFunction();
  
  /**
   * Returns the expressions applied to the parameters of the function
   * @return List of expression
   */
  List<EdmExpression> getParameters();
}
