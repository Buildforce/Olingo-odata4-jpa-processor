/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

public class UriParameterImpl implements UriParameter {
  private String name;
  private String text;
  private String alias;
  private Expression expression;
  private String referencedProperty;

  @Override
  public String getName() {
    return name;
  }

  public UriParameterImpl setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public String getAlias() {
    return alias;
  }

  public UriParameterImpl setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  @Override
  public String getText() {
    return text;
  }

  public UriParameterImpl setText(String text) {
    this.text = text;
    return this;
  }

  @Override
  public Expression getExpression() {
    return expression;
  }

  public UriParameterImpl setExpression(Expression expression) {
    this.expression = expression;
    return this;
  }

  @Override
  public String getReferencedProperty() {
    return referencedProperty;
  }

  public UriParameterImpl setReferencedProperty(String referencedProperty) {
    this.referencedProperty = referencedProperty;
    return this;
  }

  @Override
  public String toString() {
    return name;
  }
}
