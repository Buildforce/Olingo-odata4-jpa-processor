/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.SkipTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class SkipTokenOptionImpl extends SystemQueryOptionImpl implements SkipTokenOption {

  public SkipTokenOptionImpl() {
    setKind(SystemQueryOptionKind.SKIPTOKEN);
  }

  @Override
  public String getValue() {
    return getText();
  }

  public SkipTokenOptionImpl setValue(String skipTokenValue) {
    setText(skipTokenValue);
    return this;
  }

}
