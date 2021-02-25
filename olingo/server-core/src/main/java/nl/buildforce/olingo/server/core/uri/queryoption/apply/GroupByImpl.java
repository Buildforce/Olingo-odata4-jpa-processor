/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.GroupBy;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.GroupByItem;

/**
 * Represents the grouping transformation.
 */
public class GroupByImpl implements GroupBy {

  private ApplyOption applyOption;
  private final List<GroupByItem> groupByItems = new ArrayList<>();

  @Override
  public Kind getKind() {
    return Kind.GROUP_BY;
  }

  @Override
  public ApplyOption getApplyOption() {
    return applyOption;
  }

  public GroupByImpl setApplyOption(ApplyOption applyOption) {
    this.applyOption = applyOption;
    return this;
  }

  @Override
  public List<GroupByItem> getGroupByItems() {
    return groupByItems;
  }

  public GroupByImpl addGroupByItem(GroupByItem groupByItem) {
    groupByItems.add(groupByItem);
    return this;
  }
}