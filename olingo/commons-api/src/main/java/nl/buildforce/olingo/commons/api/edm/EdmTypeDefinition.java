/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * An {@link EdmTypeDefinition} defines a specialization of one of the possible primitive types.
 * <br/>
 * For more information on primitive types refer to {@link EdmPrimitiveType}.
 */
public interface EdmTypeDefinition extends EdmPrimitiveType, EdmAnnotatable {

  /**
   * @return {@link EdmPrimitiveType} this type definition is based upon
   */
  EdmPrimitiveType getUnderlyingType();

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
  // SRID getSrid();

  /**
   * @return true if unicode or null if not specified
   */
  Boolean isUnicode();
}