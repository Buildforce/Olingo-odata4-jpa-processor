/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.List;

/**
 * Can be applied to CSDL elements as described in the Conceptual Schema Definition Language.
 */
public interface EdmAnnotatable {

  /**
   * @param term term used for the annotation. MUST NOT be null.
   * @param qualifier for the term. Can be <code>NULL</code>
   * @return annotation according to term
   */
  EdmAnnotation getAnnotation(EdmTerm term, String qualifier);

  /**
   * Get list of all annotations.
   *
   * @return list of all annotations
   */
  List<EdmAnnotation> getAnnotations();
}
