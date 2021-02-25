/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

/**
 * A CSDL parameter element
 */
public interface EdmParameter extends EdmElement, EdmMappable, EdmAnnotatable {

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
   * @return a non-negative integer or the special value <tt>variable</tt>
   */
  // SRID getSrid();
}