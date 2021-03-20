/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.core.ODataImpl;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Method;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.MethodKind;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.TypeLiteral;

public class MethodImpl implements Method {

  private final MethodKind method;
  private final List<Expression> parameters;

  public MethodImpl(MethodKind method, List<Expression> parameters) {
    this.method = method;
    this.parameters = parameters;
  }

/*
  @Override
  public MethodKind getMethod() {
    return method;
  }

  @Override
  public List<Expression> getParameters() {
    return parameters == null ?
        Collections.emptyList() :
        Collections.unmodifiableList(parameters);
  }
*/

  public EdmType getType() {
    EdmPrimitiveTypeKind kind = null;
    switch (method) {
    case CONTAINS:
    case STARTSWITH:
    case ENDSWITH:
    case SUBSTRINGOF:
      case ISOF:
      case GEOINTERSECTS:
        kind = EdmPrimitiveTypeKind.Boolean;
      break;
    case LENGTH:
    case INDEXOF:
      case TOTALOFFSETMINUTES:
      case YEAR:
      case MONTH:
      case DAY:
      case HOUR:
      case MINUTE:
      case SECOND:
        kind = EdmPrimitiveTypeKind.Int32;
      break;
    case SUBSTRING:
    case TOLOWER:
    case TOUPPER:
    case TRIM:
    case CONCAT:
      kind = EdmPrimitiveTypeKind.String;
      break;
      case FRACTIONALSECONDS:
    case TOTALSECONDS:
      kind = EdmPrimitiveTypeKind.Decimal;
      break;
    case DATE:
      kind = EdmPrimitiveTypeKind.Date;
      break;
    case TIME:
      kind = EdmPrimitiveTypeKind.TimeOfDay;
      break;
      case MINDATETIME:
    case MAXDATETIME:
    case NOW:
      kind = EdmPrimitiveTypeKind.DateTimeOffset;
      break;
    case ROUND:
    case FLOOR:
    case CEILING:
      case GEODISTANCE:
      case GEOLENGTH:
        // Needs to be refined if Decimal must be distinguished from Double.
      kind = EdmPrimitiveTypeKind.Double;
      break;
      case CAST:
      return ((TypeLiteral) parameters.get(parameters.size() - 1)).getType();
    }
    return new ODataImpl().createPrimitiveTypeInstance(kind);
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
    List<T> userParameters = new ArrayList<>();
    if (parameters != null) {
      for (Expression parameter : parameters) {
        userParameters.add(parameter.accept(visitor));
      }
    }
    return visitor.visitMethodCall(method, userParameters);
  }

  @Override
  public String toString() {
    return "{" + method + " " + parameters + "}";
  }

}