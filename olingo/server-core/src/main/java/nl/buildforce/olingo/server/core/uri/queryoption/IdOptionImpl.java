/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.IdOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class IdOptionImpl extends SystemQueryOptionImpl implements IdOption {

  public IdOptionImpl() {
    setKind(SystemQueryOptionKind.ID);
  }

  public IdOptionImpl setValue(String value) {
    setText(value);
    return this;
  }

  @Override
  public String getValue() {
    return getText();
  }
}
