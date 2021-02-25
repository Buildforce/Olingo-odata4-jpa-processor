/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public abstract class SystemQueryOptionImpl extends QueryOptionImpl implements SystemQueryOption {

  private SystemQueryOptionKind kind;

  @Override
  public SystemQueryOptionKind getKind() {
    return kind;
  }

  protected void setKind(SystemQueryOptionKind kind) {
    this.kind = kind;
  }

  @Override
  public String getName() {
    return kind.toString();
  }

}
