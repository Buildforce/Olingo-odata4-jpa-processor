/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.UUID;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmEnumType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.EdmTypeDefinition;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;

public class EdmTypeInfo {

  public static class Builder {

    private String typeExpression;
    private Edm edm;
	private boolean includeAnnotations;

    public Builder setTypeExpression(String typeExpression) {
      this.typeExpression = typeExpression;
      return this;
    }

    public Builder setEdm(Edm edm) {
      this.edm = edm;
      return this;
    }

	public Builder setIncludeAnnotations(boolean includeAnnotations) {
      this.includeAnnotations = includeAnnotations;
      return this;
    }
	
    public EdmTypeInfo build() {
      return new EdmTypeInfo(edm, typeExpression, includeAnnotations);
    }
  }

  private final boolean collection;
  private final FullQualifiedName fullQualifiedName;
  private final EdmPrimitiveTypeKind primitiveType;
  private EdmTypeDefinition typeDefinition;
  private EdmEnumType enumType;
  private EdmComplexType complexType;
  private EdmEntityType entityType;

  private EdmTypeInfo(Edm edm, String typeExpression, boolean includeAnnotations) {
    String baseType;
    int collStartIdx = typeExpression.indexOf("Collection(");
    int collEndIdx = typeExpression.lastIndexOf(')');
    if (collStartIdx == -1) {
      baseType = typeExpression;
      collection = false;
    } else {
      if (collEndIdx == -1) {
        throw new IllegalArgumentException("Malformed type: " + typeExpression);
      }

      collection = true;
      baseType = typeExpression.substring(collStartIdx + 11, collEndIdx);
    }

    if (baseType.startsWith("#")) {
      baseType = baseType.substring(1);
    }

    String typeName;
    String namespace;

    int lastDotIdx = baseType.lastIndexOf('.');
    if (lastDotIdx == -1) {
      namespace = EdmPrimitiveType.EDM_NAMESPACE;
      typeName = baseType;
    } else {
      namespace = baseType.substring(0, lastDotIdx);
      typeName = baseType.substring(lastDotIdx + 1);
    }

    if (typeName.isEmpty()) {
      throw new IllegalArgumentException("Null or empty type name in " + typeExpression);
    }

    fullQualifiedName = new FullQualifiedName(namespace, typeName);

    primitiveType = EdmPrimitiveTypeKind.getByName(typeName);

    if (primitiveType == null && edm != null) {
      typeDefinition = edm.getTypeDefinition(fullQualifiedName);
      if (typeDefinition == null) {
        enumType = edm.getEnumType(fullQualifiedName);
        if (enumType == null) {
          if (includeAnnotations) {
            complexType = ((AbstractEdm)edm).
                getComplexTypeWithAnnotations(fullQualifiedName, true);
          } else {
            complexType = edm.getComplexType(fullQualifiedName);
          }
          if (complexType == null) {
            entityType = edm.getEntityType(fullQualifiedName);
          }
        }
      }
    }
  }

  public String internal() {
    return serialize(false);
  }

  public String external() {
    return serialize(true);
  }

  private String serialize(boolean external) {
    StringBuilder serialize = new StringBuilder();

    if (external && (!isPrimitiveType() || isCollection())) {
      serialize.append('#');
    }

    if (isCollection()) {
      serialize.append("Collection(");
    }

    serialize.append(external && isPrimitiveType() ?
        getFullQualifiedName().getName() :
        getFullQualifiedName().getFullQualifiedNameAsString());

    if (isCollection()) {
      serialize.append(')');
    }

    return serialize.toString();
  }

  public boolean isCollection() {
    return collection;
  }

  public FullQualifiedName getFullQualifiedName() {
    return fullQualifiedName;
  }

  public boolean isPrimitiveType() {
    return primitiveType != null;
  }

  public EdmPrimitiveTypeKind getPrimitiveTypeKind() {
    return primitiveType;
  }

  public boolean isTypeDefinition() {
    return typeDefinition != null;
  }

  public EdmTypeDefinition getTypeDefinition() {
    return typeDefinition;
  }

  public boolean isEnumType() {
    return enumType != null;
  }

  public EdmEnumType getEnumType() {
    return enumType;
  }

  public boolean isComplexType() {
    return complexType != null;
  }

  public EdmComplexType getComplexType() {
    return complexType;
  }

  public boolean isEntityType() {
    return entityType != null;
  }

  public EdmEntityType getEntityType() {
    return entityType;
  }

  public EdmType getType() {
    return isPrimitiveType() ? EdmPrimitiveTypeFactory.getInstance(getPrimitiveTypeKind()) :
        isTypeDefinition() ? getTypeDefinition() :
        isEnumType() ? getEnumType() :
        isComplexType() ? getComplexType() :
        isEntityType() ? getEntityType() :
        null;
  }

  public static EdmPrimitiveTypeKind determineTypeKind(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Boolean) {
      return EdmPrimitiveTypeKind.Boolean;
    } else if (value instanceof String) {
      return EdmPrimitiveTypeKind.String;
    } else if (value instanceof UUID) {
      return EdmPrimitiveTypeKind.Guid;
    } else if (value instanceof Long || value instanceof BigInteger) {
      return EdmPrimitiveTypeKind.Int64;
    } else if (value instanceof Integer) {
      return EdmPrimitiveTypeKind.Int32;
    } else if (value instanceof Short) {
      return EdmPrimitiveTypeKind.Int16;
    } else if (value instanceof Byte) {
      return EdmPrimitiveTypeKind.SByte;
    } else if (value instanceof BigDecimal) {
      return EdmPrimitiveTypeKind.Decimal;
    } else if (value instanceof Double) {
      return EdmPrimitiveTypeKind.Double;
    } else if (value instanceof Float) {
      return EdmPrimitiveTypeKind.Single;
    } else if (value instanceof java.sql.Date) {
      return EdmPrimitiveTypeKind.Date;
    } else if (value instanceof java.sql.Time) {
      return EdmPrimitiveTypeKind.TimeOfDay;
    } else if ((value instanceof Calendar) ||
            (value instanceof OffsetDateTime) ||
            (value instanceof java.util.Date)) { // Which also covers Timestamp
      return EdmPrimitiveTypeKind.DateTimeOffset;

    } else if (value instanceof byte[] || value instanceof Byte[]) {
      return EdmPrimitiveTypeKind.Binary;
    }
    return null;
  }
}