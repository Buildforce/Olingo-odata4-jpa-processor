/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.SkipOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class SkipOptionImpl extends SystemQueryOptionImpl implements SkipOption {
  private int value;

  public SkipOptionImpl() {
    setKind(SystemQueryOptionKind.SKIP);
  }

  @Override
  public int getValue() {
    return value;
  }

  public SkipOptionImpl setValue(int value) {
    this.value = value;
    return this;
  }

}
