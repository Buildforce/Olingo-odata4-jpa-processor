/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.expression;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Literal;

public class LiteralImpl implements Literal {

  private final String text;
  private final EdmType type;

  public LiteralImpl(String text, EdmType type) {
    this.text = text;
    this.type = type;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public EdmType getType() {
    return type;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
    return visitor.visitLiteral(this);
  }

  @Override
  public String toString() {
    return text == null ? "" : text;
  }
}
