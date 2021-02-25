/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.apply.BottomTop;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents a transformation with one of the pre-defined methods
 * <code>bottomcount</code>, <code>bottompercent</code>, <code>bottomsum</code>,
 * <code>topcount</code>, <code>toppercent</code>, <code>topsum</code>.
 */
public class BottomTopImpl implements BottomTop {

  private Method method;
  private Expression number;
  private Expression value;

  @Override
  public Kind getKind() {
    return Kind.BOTTOM_TOP;
  }

  @Override
  public Method getMethod() {
    return method;
  }

  public BottomTopImpl setMethod(Method method) {
    this.method = method;
    return this;
  }

  @Override
  public Expression getNumber() {
    return number;
  }

  public BottomTopImpl setNumber(Expression number) {
    this.number = number;
    return this;
  }

  @Override
  public Expression getValue() {
    return value;
  }

  public BottomTopImpl setValue(Expression value) {
    this.value = value;
    return this;
  }
}
