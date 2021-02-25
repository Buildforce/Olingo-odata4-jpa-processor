/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;
import nl.buildforce.olingo.server.api.uri.queryoption.TopOption;

public class TopOptionImpl extends SystemQueryOptionImpl implements TopOption {
  private int value;

  public TopOptionImpl() {
    setKind(SystemQueryOptionKind.TOP);
  }

  @Override
  public int getValue() {
    return value;
  }

  public TopOptionImpl setValue(int value) {
    this.value = value;
    return this;
  }

}
