/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.LevelsExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class LevelsOptionImpl extends SystemQueryOptionImpl implements LevelsExpandOption {
  private boolean isMax;
  private int value;

  public LevelsOptionImpl() {
    setKind(SystemQueryOptionKind.LEVELS);
  }

  public LevelsOptionImpl setValue(int value) {
    this.value = value;
    return this;
  }

  @Override
  public boolean isMax() {
    return isMax;
  }

  public LevelsOptionImpl setMax() {
    isMax = true;
    return this;

  }

  @Override
  public int getValue() {
    return value;
  }

}
