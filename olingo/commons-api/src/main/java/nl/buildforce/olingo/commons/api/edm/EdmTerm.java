/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.List;

/**
 * An {@link EdmTerm} defines a term in a vocabulary.
 */
public interface EdmTerm extends EdmNamed, EdmAnnotatable {

  /**
   * @return type of value returned by the expression contained in an annotation using this term
   */
  EdmType getType();

  /**
   * @return the fully qualified name of this term
   */
  FullQualifiedName getFullQualifiedName();

  /**
   * When applying a term with a base term,the base term MUST also be applied with the same qualifier, and so on until a
   * term without a base term is reached.
   *
   * @return the base term if found or null otherwise
   */
  EdmTerm getBaseTerm();

  /**
   * @return list of CSDL element that this term can be applied to; if no value is supplied, the term is not restricted
   * in its application.
   */
  List<TargetType> getAppliesTo();

  /**
   * @return true if nullable
   */
  boolean isNullable();

  /**
   * @return the maximum length as an Integer or null if not specified
   */
  Integer getMaxLength();

  /**
   * @return the precision as an Integer or null if not specified
   */
  Integer getPrecision();

  /**
   * @return the scale as an Integer or null if not specified
   */
  Integer getScale();

  /*
    @return a non-negative integer or the special value <tt>variable</tt>
   */
  //SRID getSrid();

  /**
   * @return the default value as a String or null if not specified
   */
  String getDefaultValue();

}