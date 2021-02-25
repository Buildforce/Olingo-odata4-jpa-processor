/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.expression;

import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmEnumType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Enumeration;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;

public class EnumerationImpl implements Enumeration {

  private final EdmEnumType type;
  private final List<String> values;

  public EnumerationImpl(EdmEnumType type, List<String> values) {
    this.type = type;
    this.values = values;
  }

  @Override
  public EdmEnumType getType() {
    return type;
  }

  @Override
  public List<String> getValues() {
    return values == null ?
        Collections.emptyList() :
        Collections.unmodifiableList(values);
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
    return visitor.visitEnum(type, values);
  }

  @Override
  public String toString() {
    return type == null ? "NULL" :
      type.getFullQualifiedName().getFullQualifiedNameAsString() + getValues();
  }
}
