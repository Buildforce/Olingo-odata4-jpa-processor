/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.Expand;

/**
 * Represents the expand transformation.
 */
public class ExpandImpl implements Expand {

  private ExpandOption expandOption;

  @Override
  public Kind getKind() {
    return Kind.EXPAND;
  }

  @Override
  public ExpandOption getExpandOption() {
    return expandOption;
  }

  public ExpandImpl setExpandOption(ExpandOption expandOption) {
    this.expandOption = expandOption;
    return this;
  }
}
