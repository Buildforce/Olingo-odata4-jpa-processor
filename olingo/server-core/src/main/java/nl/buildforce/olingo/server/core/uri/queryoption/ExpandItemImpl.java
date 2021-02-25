/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.ex.ODataRuntimeException;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandItem;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FilterOption;
import nl.buildforce.olingo.server.api.uri.queryoption.LevelsExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.TopOption;

public class ExpandItemImpl implements ExpandItem {
  private LevelsExpandOption levelsExpandOption;
  private FilterOption filterOption;
  private SearchOption searchOption;
  private OrderByOption orderByOption;
  private SkipOption skipOption;
  private TopOption topOption;
  private CountOption inlineCountOption;
  private SelectOption selectOption;
  private ExpandOption expandOption;
  private ApplyOption applyOption;

  private UriInfoResource resourceInfo;

  private boolean isStar;

  private boolean isRef;
  private boolean hasCountPath;
  private EdmType startTypeFilter;

  public ExpandItemImpl setSystemQueryOption(SystemQueryOption sysItem) {

    if (sysItem instanceof ApplyOption) {
      validateDoubleSystemQueryOption(applyOption, sysItem);
      applyOption = (ApplyOption) sysItem;
    } else if (sysItem instanceof ExpandOption) {
      validateDoubleSystemQueryOption(expandOption, sysItem);
      expandOption = (ExpandOption) sysItem;
    } else if (sysItem instanceof FilterOption) {
      validateDoubleSystemQueryOption(filterOption, sysItem);
      filterOption = (FilterOption) sysItem;
    } else if (sysItem instanceof CountOption) {
      validateDoubleSystemQueryOption(inlineCountOption, sysItem);
      inlineCountOption = (CountOption) sysItem;
    } else if (sysItem instanceof OrderByOption) {
      validateDoubleSystemQueryOption(orderByOption, sysItem);
      orderByOption = (OrderByOption) sysItem;
    } else if (sysItem instanceof SearchOption) {
      validateDoubleSystemQueryOption(searchOption, sysItem);
      searchOption = (SearchOption) sysItem;
    } else if (sysItem instanceof SelectOption) {
      validateDoubleSystemQueryOption(selectOption, sysItem);
      selectOption = (SelectOption) sysItem;
    } else if (sysItem instanceof SkipOption) {
      validateDoubleSystemQueryOption(skipOption, sysItem);
      skipOption = (SkipOption) sysItem;
    } else if (sysItem instanceof TopOption) {
      validateDoubleSystemQueryOption(topOption, sysItem);
      topOption = (TopOption) sysItem;
    } else if (sysItem instanceof LevelsExpandOption) {
      if (levelsExpandOption != null) {
        throw new ODataRuntimeException("$levels");
      }
      levelsExpandOption = (LevelsExpandOption) sysItem;
    }
    return this;
  }

  private void validateDoubleSystemQueryOption(SystemQueryOption oldOption, SystemQueryOption newOption) {
    if (oldOption != null) {
      throw new ODataRuntimeException(newOption.getName());
    }
  }

  public ExpandItemImpl setSystemQueryOptions(List<SystemQueryOption> list) {
    for (SystemQueryOption item : list) {
      setSystemQueryOption(item);
    }
    return this;
  }

  @Override
  public LevelsExpandOption getLevelsOption() {
    return levelsExpandOption;
  }

  @Override
  public FilterOption getFilterOption() {
    return filterOption;
  }

  @Override
  public SearchOption getSearchOption() {
    return searchOption;
  }

  @Override
  public OrderByOption getOrderByOption() {
    return orderByOption;
  }

  @Override
  public SkipOption getSkipOption() {
    return skipOption;
  }

  @Override
  public TopOption getTopOption() {
    return topOption;
  }

  @Override
  public CountOption getCountOption() {
    return inlineCountOption;
  }

  @Override
  public SelectOption getSelectOption() {

    return selectOption;
  }

  @Override
  public ExpandOption getExpandOption() {
    return expandOption;
  }

  @Override
  public ApplyOption getApplyOption() {
    return applyOption;
  }

  public ExpandItemImpl setResourcePath(UriInfoResource resourceInfo) {
    this.resourceInfo = resourceInfo;
    return this;
  }

  @Override
  public UriInfoResource getResourcePath() {

    return resourceInfo;
  }

  @Override
  public boolean isStar() {
    return isStar;
  }

  public ExpandItemImpl setIsStar(boolean isStar) {
    this.isStar = isStar;
    return this;
  }

  @Override
  public boolean isRef() {
    return isRef;
  }

  public ExpandItemImpl setIsRef(boolean isRef) {
    this.isRef = isRef;
    return this;
  }

  @Override
  public EdmType getStartTypeFilter() {
    return startTypeFilter;
  }

  public ExpandItemImpl setTypeFilter(EdmType startTypeFilter) {
    this.startTypeFilter = startTypeFilter;
    return this;
  }

  @Override
  public boolean hasCountPath() {
    return hasCountPath;
  }
  
  public void setCountPath(boolean value) {
      hasCountPath = value;
  }
}
