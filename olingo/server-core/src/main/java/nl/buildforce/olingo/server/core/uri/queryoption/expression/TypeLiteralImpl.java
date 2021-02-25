/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.expression;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.TypeLiteral;

public class TypeLiteralImpl implements TypeLiteral {

  private final EdmType type;

  public TypeLiteralImpl(EdmType type) {
    this.type = type;
  }

  @Override
  public EdmType getType() {
    return type;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
    return visitor.visitTypeLiteral(type);
  }

  @Override
  public String toString() {
    return type == null ? "NULL" : type.getFullQualifiedName().getFullQualifiedNameAsString();
  }
}
