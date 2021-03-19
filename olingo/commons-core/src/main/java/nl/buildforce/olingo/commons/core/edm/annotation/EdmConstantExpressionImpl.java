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
      EdmPrimitiveTypeKind kind = switch (csdlExp.getType()) {
          case Binary -> EdmPrimitiveTypeKind.Binary;
          case Bool -> EdmPrimitiveTypeKind.Boolean;
          case Date -> EdmPrimitiveTypeKind.Date;
          case DateTimeOffset -> EdmPrimitiveTypeKind.DateTimeOffset;
          case Decimal -> EdmPrimitiveTypeKind.Decimal;
          case Duration -> EdmPrimitiveTypeKind.Duration;
          case Float -> EdmPrimitiveTypeKind.Single;
          case Guid -> EdmPrimitiveTypeKind.Guid;
          case Int -> EdmPrimitiveTypeKind.Int32;
          case TimeOfDay -> EdmPrimitiveTypeKind.TimeOfDay;
          default -> EdmPrimitiveTypeKind.String;
      };
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
      return switch (csdlExp.getType()) {
          case Binary -> EdmExpressionType.Binary;
          case Bool -> EdmExpressionType.Bool;
          case Date -> EdmExpressionType.Date;
          case DateTimeOffset -> EdmExpressionType.DateTimeOffset;
          case Decimal -> EdmExpressionType.Decimal;
          case Duration -> EdmExpressionType.Duration;
          case EnumMember -> EdmExpressionType.EnumMember;
          case Float -> EdmExpressionType.Float;
          case Guid -> EdmExpressionType.Guid;
          case Int -> EdmExpressionType.Int;
          case String -> EdmExpressionType.String;
          case TimeOfDay -> EdmExpressionType.TimeOfDay;
          // default -> throw new EdmException("Invalid Expressiontype for constant expression: " + csdlExp.getType());
      };
  }

/*
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
*/

}