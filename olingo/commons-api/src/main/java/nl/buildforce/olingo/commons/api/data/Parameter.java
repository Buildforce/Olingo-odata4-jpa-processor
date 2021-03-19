/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.util.Objects;

/**
 * Data representation for a parameter.
 */
public class Parameter extends Valuable {

  private String name;

  /**
   * Gets the name of the parameter.
   * @return name of the parameter
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the parameter.
   * @param name of the parameter
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Checks whether this parameter value is of the value type ENTITY or COLLECTION_ENTITY.
   * @return true if the value type is ENTITY or COLLECTION_ENTITY, otherwise false
   */
/*
  public boolean isEntity() {
    return getValueType() == ValueType.ENTITY || getValueType() == ValueType.COLLECTION_ENTITY;
  }
*/

// --Commented out by Inspection START (''21-03-09 22:31):
//  /**
//   * Gets the value in its entity representation or null if it is not based on an entity value type.
//   * @return entity representation or null if it is not based on an entity value type
//   */
//  public Entity asEntity() {
//    return isEntity() && !isCollection() ? (Entity) getValue() : null;
//  }
// --Commented out by Inspection STOP (''21-03-09 22:31)

  @Override
  public boolean equals(Object o) {
    return super.equals(o)
        && (Objects.equals(name, ((Parameter) o).name));
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (name == null ? 0 : name.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return (name == null ? "null" : name) + '=' + (getValue() == null ? "null" : getValue());
  }

}