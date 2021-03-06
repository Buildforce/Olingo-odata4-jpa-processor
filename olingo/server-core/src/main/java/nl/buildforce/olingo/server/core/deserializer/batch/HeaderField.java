/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import java.util.ArrayList;
import java.util.List;

public class HeaderField implements Cloneable {
  private final String fieldName;
  private final int lineNumber;
  private List<String> values;

  public HeaderField(String fieldName, int lineNumber) {
    this(fieldName, new ArrayList<>(), lineNumber);
  }

  public HeaderField(String fieldName, List<String> values, int lineNumber) {
    this.fieldName = fieldName;
    this.values = values;
    this.lineNumber = lineNumber;
  }

  public String getFieldName() {
    return fieldName;
  }

  public List<String> getValues() {
    return values;
  }

  public String getValue() {
    StringBuilder result = new StringBuilder();

    for (String value : values) {
      result.append(value);
      result.append(", ");
    }

    if (result.length() > 0) {
      result.delete(result.length() - 2, result.length());
    }

    return result.toString();
  }

  @Override
  public HeaderField clone() throws CloneNotSupportedException{
    HeaderField clone = (HeaderField) super.clone();
    clone.values = new ArrayList<>(values.size());
    clone.values.addAll(values);
    return clone;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
    result = prime * result + lineNumber;
    result = prime * result + ((values == null) ? 0 : values.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    HeaderField other = (HeaderField) obj;
    if (fieldName == null) {
      if (other.fieldName != null) {
        return false;
      }
    } else if (!fieldName.equals(other.fieldName)) {
      return false;
    }
    if (lineNumber != other.lineNumber) {
      return false;
    }
    if (values == null) {
      return other.values == null;
    } else return values.equals(other.values);
  }
}
