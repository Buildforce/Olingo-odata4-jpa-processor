/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.GroupByItem;

/**
 * Represents a grouping property.
 */
public class GroupByItemImpl implements GroupByItem {

  private UriInfo path;
  private boolean isRollupAll;
  private final List<GroupByItem> rollup = new ArrayList<>();

  @Override
  public List<UriResource> getPath() {
    return path == null ? Collections.emptyList() : path.getUriResourceParts();
  }

  public GroupByItemImpl setPath(UriInfo uriInfo) {
    path = uriInfo;
    return this;
  }

  @Override
  public List<GroupByItem> getRollup() {
    return rollup;
  }

  public GroupByItemImpl addRollupItem(GroupByItem groupByItem) {
    rollup.add(groupByItem);
    return this;
  }

  @Override
  public boolean isRollupAll() {
    return isRollupAll;
  }

  public GroupByItemImpl setIsRollupAll() {
      isRollupAll = true;
    return this;
  }
}