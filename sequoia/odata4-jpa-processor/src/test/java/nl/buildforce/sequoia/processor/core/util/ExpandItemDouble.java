package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.UriResourceNavigation;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;
import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandItem;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FilterOption;
import nl.buildforce.olingo.server.api.uri.queryoption.LevelsExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipOption;
import nl.buildforce.olingo.server.api.uri.queryoption.TopOption;

import static org.junit.jupiter.api.Assertions.fail;

public class ExpandItemDouble implements ExpandItem {
  private final UriResourceNavigation target;

  public ExpandItemDouble(final EdmEntityType naviTargetEntity) {
    target = new UriResourceNavigationDouble(naviTargetEntity, new EdmNavigationPropertyDouble(
        naviTargetEntity.getName()));
  }

  @Override
  public LevelsExpandOption getLevelsOption() {
    return null;
  }

  @Override
  public FilterOption getFilterOption() {
    fail();
    return null;
  }

  @Override
  public SearchOption getSearchOption() {
    fail();
    return null;
  }

  @Override
  public OrderByOption getOrderByOption() {
    fail();
    return null;
  }

  @Override
  public SkipOption getSkipOption() {
    fail();
    return null;
  }

  @Override
  public TopOption getTopOption() {
    fail();
    return null;
  }

  @Override
  public CountOption getCountOption() {
    fail();
    return null;
  }

  @Override
  public SelectOption getSelectOption() {
    fail();
    return null;
  }

  @Override
  public ExpandOption getExpandOption() {
    fail();
    return null;
  }

  @Override
  public UriInfoResource getResourcePath() {
    return new UriInfoResourceDouble(target);
  }

  @Override
  public boolean isStar() {
    return false;
  }

  @Override
  public boolean isRef() {
    fail();
    return false;
  }

  @Override
  public EdmType getStartTypeFilter() {
    fail();
    return null;
  }

  @Override
  public boolean hasCountPath() {
    fail();
    return false;
  }

  @Override
  public ApplyOption getApplyOption() {
    return null;
  }

}