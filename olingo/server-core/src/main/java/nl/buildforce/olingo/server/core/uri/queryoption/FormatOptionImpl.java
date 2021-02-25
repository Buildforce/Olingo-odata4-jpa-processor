/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.FormatOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class FormatOptionImpl extends SystemQueryOptionImpl implements FormatOption {

  public FormatOptionImpl() {
    setKind(SystemQueryOptionKind.FORMAT);
  }

  public FormatOptionImpl setFormat(String value) {
    setText(value);
    return this;
  }

  @Override
  public String getFormat() {
    return getText();
  }

}
