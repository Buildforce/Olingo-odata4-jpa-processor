/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the value of a complex property.
 */
public class ComplexValue extends Linked {

  private final List<Property> value = new ArrayList<>();
  
  private String typeName;

  /**
   * Get list of all values for this ComplexValue.
   *
   * @return all values for this ComplexValue (can not be null).
   */
  public List<Property> getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o) && value.equals(((ComplexValue) o).value);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + value.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return value.toString();
  }
  
  /**
   * Get string representation of type (can be null if not set).
   * @return string representation of type (can be null if not set)
   */
  public String getTypeName() {
    return typeName;
  }

  /**
   * Set string representation of type.
   * @param typeName string representation of type
   */
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

}