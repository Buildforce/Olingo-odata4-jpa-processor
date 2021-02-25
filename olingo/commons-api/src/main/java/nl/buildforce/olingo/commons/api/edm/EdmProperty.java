/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * A CSDL Property element.
 * <br/>
 * EdmProperty defines a simple type or a complex type.
 */
public interface EdmProperty extends EdmElement, EdmMappable, EdmAnnotatable {

  /**
   * Gets the related MIME type for the property.
   *
   * @return MIME type as String
   */
  String getMimeType();

  /**
   * Gets the info if the property is a primitive property.
   *
   * @return true, if it is a primitive property
   */
  boolean isPrimitive();

  /**
   * @return true if nullable or not specified
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
  // SRID getSrid();

  /**
   * @return true if unicode or not specified
   */
  boolean isUnicode();

  /**
   * @return the default value as a String or null if not specified
   */
  String getDefaultValue();
  
  /*
   * See {@link EdmType} for more information about possible types.
   *
   * @return {@link EdmType}
   */
  // EdmType getTypeWithAnnotations();
}