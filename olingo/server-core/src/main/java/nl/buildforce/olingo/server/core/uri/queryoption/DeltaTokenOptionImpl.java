/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.DeltaTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class DeltaTokenOptionImpl extends SystemQueryOptionImpl implements DeltaTokenOption {
  
  public DeltaTokenOptionImpl() {
    setKind(SystemQueryOptionKind.DELTATOKEN);
  }
  
  @Override
  public String getValue() {
    return getText();
  }

  public DeltaTokenOptionImpl setValue(String deltaTokenValue) {
    setText(deltaTokenValue);
    return this;
  }
}
