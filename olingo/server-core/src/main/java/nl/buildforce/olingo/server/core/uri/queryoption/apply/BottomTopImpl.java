/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.apply.BottomTop;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents a transformation with one of the pre-defined methods
 * <code>bottomcount, bottompercent, bottomsum, topcount, toppercent, topsum</code>.
 */
public class BottomTopImpl implements BottomTop {

  @Override
  public Kind getKind() {
    return Kind.BOTTOM_TOP;
  }

  public BottomTopImpl setMethod(Method method) {
    return this;
  }

/*
  @Override
  public Method getMethod() {
    return method;
  }

  @Override
  public Expression getNumber() {
    return number;
  }

  @Override
  public Expression getValue() {
    return value;
  }
*/

  public BottomTopImpl setNumber(Expression number) {
    return this;
  }

  public BottomTopImpl setValue(Expression value) {
    return this;
  }

}