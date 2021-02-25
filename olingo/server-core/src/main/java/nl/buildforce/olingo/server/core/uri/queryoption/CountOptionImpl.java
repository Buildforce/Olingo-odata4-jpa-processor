/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class CountOptionImpl extends SystemQueryOptionImpl implements CountOption {

  private boolean count;

  public CountOptionImpl() {
    setKind(SystemQueryOptionKind.COUNT);
  }

  @Override
  public boolean getValue() {
    return count;
  }

  public CountOptionImpl setValue(boolean count) {
    this.count = count;
    return this;
  }

}
