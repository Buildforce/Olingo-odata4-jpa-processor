/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.SelectItem;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class SelectOptionImpl extends SystemQueryOptionImpl implements SelectOption {

  private List<SelectItem> selectItems;

  public SelectOptionImpl() {
    setKind(SystemQueryOptionKind.SELECT);
  }

  public SelectOptionImpl setSelectItems(List<SelectItem> selectItems) {
    this.selectItems = selectItems;
    return this;
  }

  @Override
  public List<SelectItem> getSelectItems() {
    return selectItems == null ? Collections.emptyList() : Collections.unmodifiableList(selectItems);
  }

}
