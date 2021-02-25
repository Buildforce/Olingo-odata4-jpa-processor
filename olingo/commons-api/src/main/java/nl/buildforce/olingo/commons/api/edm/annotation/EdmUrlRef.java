/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.annotation;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;

/**
 * The edm:UrlRef expression enables a value to be obtained by sending a GET request to the value of 
 * the UrlRef expression. 
 */
public interface EdmUrlRef extends EdmDynamicExpression, EdmAnnotatable {
  
  /**
   * Returns a expression of type Edm.String
   * @return expression of type Edm.String
   */
  EdmExpression getValue();
}
