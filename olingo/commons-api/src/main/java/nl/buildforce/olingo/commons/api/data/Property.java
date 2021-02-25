/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data representation for a property.
 */
public class Property extends Valuable {

  private String name;
  private final List<Operation> operations = new ArrayList<>();
  
  /**
   * Creates a new property
   */
  public Property() {}
  
  /**
   * Creates a new property
   * 
   * @param type  String representation of type (can be null)
   * @param name  Name of the property
   */
  public Property(String type, String name) {
    this.name = name;
    setType(type);
  }
  
  /**
   * Creates a new property
   * 
   * @param type        String representation of type (can be null)
   * @param name        Name of the property
   * @param valueType   Kind of the property e.g. primitive property, complex property
   * @param value       Value of the property.
   */
  public Property(String type, String name, ValueType valueType, Object value) {
    this(type, name);
    setValue(valueType, value);
  }

  /**
   * Get name of property.
   * @return name of property
   */
  public String getName() {
    return name;
  }

  /**
   * Set name of property.
   * @param name name of property
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Check if this property is <code>null</code> (value == null) or the type is <code>"Edm.Null"</code>.
   * @return <code>true</code> if this property is <code>null</code> (value == null)
   *          or the type is <code>"Edm.Null"</code>. Otherwise <code>false</code>.
   */
  @Override
  public boolean isNull() {
    return getValue() == null || "Edm.Null".equals(getType());
  }
  
  /**
   * Gets operations.
   *
   * @return operations.
   */
  public List<Operation> getOperations() {
    return operations;
  }  

  @Override
  public boolean equals(Object o) {
    return super.equals(o)
        && (Objects.equals(name, ((Property) o).name));
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
