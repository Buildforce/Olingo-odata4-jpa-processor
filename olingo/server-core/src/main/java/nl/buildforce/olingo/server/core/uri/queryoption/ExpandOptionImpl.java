/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.ExpandItem;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class ExpandOptionImpl extends SystemQueryOptionImpl implements ExpandOption {

  private final List<ExpandItem> expandItems = new ArrayList<>();

  public ExpandOptionImpl() {
    setKind(SystemQueryOptionKind.EXPAND);
  }

  public ExpandOptionImpl addExpandItem(ExpandItem expandItem) {
    expandItems.add(expandItem);
    return this;
  }

  @Override
  public List<ExpandItem> getExpandItems() {
    return Collections.unmodifiableList(expandItems);
  }
}
