/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import java.util.List;

/**
 * An EdmEnumType represents a set of related values.
 */
public interface EdmEnumType extends EdmPrimitiveType {

  /**
   * Get member according to given name
   *
   * @param name name of member
   * @return {@link EdmMember} for the given name
   */
  EdmMember getMember(String name);

  /**
   * @return member names as a list
   */
  List<String> getMemberNames();

  /**
   * @return the {@link EdmPrimitiveType} this {@link EdmEnumType} is based upon
   */
  EdmPrimitiveType getUnderlyingType();

  /**
   * @return true if flags is set
   */
  boolean isFlags();
}
