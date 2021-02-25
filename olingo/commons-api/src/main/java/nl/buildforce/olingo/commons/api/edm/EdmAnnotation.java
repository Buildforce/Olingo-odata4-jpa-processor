/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;

/**
 * This class models an OData Annotation which can be applied to a target. 
 */
public interface EdmAnnotation extends EdmAnnotatable {

  /**
   * @return the term of this annotation
   */
  EdmTerm getTerm();

  /**
   * @return the qualifier for this annotation. Might be <code>NULL</code>
   */
  String getQualifier();

  EdmExpression getExpression();
}
