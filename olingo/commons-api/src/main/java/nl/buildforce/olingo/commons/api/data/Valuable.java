/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.util.List;
import java.util.Objects;

/**
 * Defines a value with an according type.
 */
public abstract class Valuable extends Annotatable {

  private ValueType valueType;
  private Object value;
  private String type;

  /**
   * Check if according value is <code>null</code>.
   * @return <code>true</code> if value is <code>null</code>, otherwise <code>false</code>
   */
  public boolean isNull() {
    return value == null;
  }

  /**
   * Get string representation of type (can be null if not set).
   * @return string representation of type (can be null if not set)
   */
  public String getType() {
    return type;
  }

  /**
   * Set string representation of type.
   * @param type string representation of type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Check if Valuable contains a PRIMITIVE or COLLECTION_PRIMITIVE ValueType
   *
   * @return true if ValueType is a PRIMITIVE or COLLECTION_PRIMITIVE, otherwise false
   */
  public boolean isPrimitive() {
    return valueType == ValueType.PRIMITIVE || valueType == ValueType.COLLECTION_PRIMITIVE;
  }

  /**
   * Check if Valuable contains a ENUM or COLLECTION_ENUM ValueType
   *
   * @return true if ValueType is a ENUM or COLLECTION_ENUM, otherwise false
   */
  public boolean isEnum() {
    return valueType == ValueType.ENUM || valueType == ValueType.COLLECTION_ENUM;
  }

  /**
   * Check if Valuable contains a COMPLEX or COLLECTION_COMPLEX ValueType
   *
   * @return true if ValueType is a COMPLEX or COLLECTION_COMPLEX, otherwise false
   */
  public boolean isComplex() {
    return valueType == ValueType.COMPLEX || valueType == ValueType.COLLECTION_COMPLEX;
  }

  /**
   * Check if Valuable contains a COLLECTION_* ValueType
   *
   * @return true if ValueType is a COLLECTION_*, otherwise false
   */
  public boolean isCollection() {
    return valueType != null && valueType != valueType.getBaseType();
  }

  /**
   * Get the value in its primitive representation or null if it is not based on a primitive ValueType
   *
   * @return primitive representation or null if it is not based on a primitive ValueType
   */
  public Object asPrimitive() {
    return isPrimitive() && !isCollection() ? value : null;
  }

  /**
   * Get the value in its enum representation or null if it is not based on a enum ValueType
   *
   * @return enum representation or null if it is not based on a enum ValueType
   */
  public Object asEnum() {
    return isEnum() && !isCollection() ? value : null;
  }

  /**
   * Get the value in its complex representation or null if it is not based on a complex ValueType
   *
   * @return primitive complex or null if it is not based on a complex ValueType
   */
  public ComplexValue asComplex() {
    return isComplex() && !isCollection() ? (ComplexValue) value : null;
  }

  /**
   * Get the value as collection or null if it is not a collection ValueType
   *
   * @return collection or null if it is not a collection ValueType
   */
  public List<?> asCollection() {
    return isCollection() ? (List<?>) value : null;
  }

  /**
   * Get the value
   *
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  /**
   * Set value and value type.
   * @param valueType value type
   * @param value value
   */
  public void setValue(ValueType valueType, Object value) {
    this.valueType = valueType;
    this.value = value;
  }

  /**
   * Get value type for this valuable.
   * @return value type for this valuable
   */
  public ValueType getValueType() {
    return valueType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Valuable other = (Valuable) o;
    return getAnnotations().equals(other.getAnnotations())
        && (Objects.equals(valueType, other.valueType))
        && (Objects.equals(value, other.value))
        && (Objects.equals(type, other.type));
  }

  @Override
  public int hashCode() {
    int result = getAnnotations().hashCode();
    result = 31 * result + (valueType == null ? 0 : valueType.hashCode());
    result = 31 * result + (value == null ? 0 : value.hashCode());
    result = 31 * result + (type == null ? 0 : type.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return value == null ? "null" : value.toString();
  }

}