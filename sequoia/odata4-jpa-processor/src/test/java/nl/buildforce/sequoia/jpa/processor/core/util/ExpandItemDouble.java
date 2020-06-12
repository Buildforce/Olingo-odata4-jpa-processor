package nl.buildforce.sequoia.jpa.processor.core.util;

import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.queryoption.*;

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