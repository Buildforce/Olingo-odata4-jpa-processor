/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmConstantExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;

public class EdmConstantExpressionImpl extends AbstractEdmExpression implements EdmConstantExpression {

  private final CsdlConstantExpression csdlExp;

  private boolean built;
  private Object primitive;
  private String enumTypeName;
  private List<String> enumMembers;

  public EdmConstantExpressionImpl(Edm edm, CsdlConstantExpression constExprConstruct) {
    super(edm, constExprConstruct.getType().toString());
      csdlExp = constExprConstruct;
  }

  @Override
  public String getValueAsString() {
    return csdlExp.getValue();
  }

  private void build() {
    if (csdlExp.getType() == CsdlConstantExpression.ConstantExpressionType.EnumMember) {
      if (csdlExp.getValue() == null) {
        throw new EdmException("Expression value must not be null");
      }
      List<String> localEnumValues = new ArrayList<>();
      for (String split : csdlExp.getValue().split(" ")) {
        String[] enumSplit = split.split("/");
        if (enumSplit.length != 2) {
          throw new EdmException("Enum expression value must consist of enumTypeName/EnumMember.");
        }
        enumTypeName = enumSplit[0];
        localEnumValues.add(enumSplit[1]);
      }
      enumMembers = Collections.unmodifiableList(localEnumValues);
    } else {
      EdmPrimitiveTypeKind kind;
      switch (csdlExp.getType()) {
      case Binary:
        kind = EdmPrimitiveTypeKind.Binary;
        break;
      case Bool:
        kind = EdmPrimitiveTypeKind.Boolean;
        break;
      case Date:
        kind = EdmPrimitiveTypeKind.Date;
        break;
      case DateTimeOffset:
        kind = EdmPrimitiveTypeKind.DateTimeOffset;
        break;
      case Decimal:
        kind = EdmPrimitiveTypeKind.Decimal;
        break;
      case Duration:
        kind = EdmPrimitiveTypeKind.Duration;
        break;
      case Float:
        kind = EdmPrimitiveTypeKind.Single;
        break;
      case Guid:
        kind = EdmPrimitiveTypeKind.Guid;
        break;
      case Int:
        kind = EdmPrimitiveTypeKind.Int32;
        break;
      case TimeOfDay:
        kind = EdmPrimitiveTypeKind.TimeOfDay;
        break;
      case String:
      default:
        kind = EdmPrimitiveTypeKind.String;
      }
      EdmPrimitiveType type = EdmPrimitiveTypeFactory.getInstance(kind);
      try {
        primitive = type.valueOfString(csdlExp.getValue(), null, null, null, null, null, type.getDefaultType());
      } catch (EdmPrimitiveTypeException e) {
        throw new IllegalArgumentException(e);
      }
    }
    built = true;
  }

  @Override
  public EdmExpressionType getExpressionType() {
    switch (csdlExp.getType()) {
    case Binary:
      return EdmExpressionType.Binary;
    case Bool:
      return EdmExpressionType.Bool;
    case Date:
      return EdmExpressionType.Date;
    case DateTimeOffset:
      return EdmExpressionType.DateTimeOffset;
    case Decimal:
      return EdmExpressionType.Decimal;
    case Duration:
      return EdmExpressionType.Duration;
    case EnumMember:
      return EdmExpressionType.EnumMember;
    case Float:
      return EdmExpressionType.Float;
    case Guid:
      return EdmExpressionType.Guid;
    case Int:
      return EdmExpressionType.Int;
    case String:
      return EdmExpressionType.String;
    case TimeOfDay:
      return EdmExpressionType.TimeOfDay;
    default:
      throw new EdmException("Invalid Expressiontype for constant expression: " + csdlExp.getType());
    }
  }

  @Override
  public Object asPrimitive() {
    if (!built) {
      build();
    }
    return primitive;
  }

  @Override
  public List<String> asEnumMembers() {
    if (!built) {
      build();
    }
    return enumMembers;
  }

  @Override
  public String getEnumTypeName() {
    if (!built) {
      build();
    }
    return enumTypeName;
  }

}