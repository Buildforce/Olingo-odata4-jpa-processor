/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.QueryOption;

public abstract class QueryOptionImpl implements QueryOption {
  private String name;
  private String text;

  @Override
  public String getName() {
    return name;
  }

  public QueryOptionImpl setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public String getText() {
    return text;
  }

  public QueryOptionImpl setText(String value) {
    text = value;
    return this;
  }

}
