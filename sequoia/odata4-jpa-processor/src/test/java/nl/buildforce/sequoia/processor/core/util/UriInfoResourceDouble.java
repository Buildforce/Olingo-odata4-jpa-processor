package nl.buildforce.sequoia.processor.core.util;

import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceNavigation;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;
import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;
import nl.buildforce.olingo.server.api.uri.queryoption.CustomQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.DeltaTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FilterOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FormatOption;
import nl.buildforce.olingo.server.api.uri.queryoption.IdOption;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.TopOption;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class UriInfoResourceDouble implements UriInfoResource {
  private final List<UriResource> resources;

  public UriInfoResourceDouble(UriResourceNavigation target) {
    resources = new ArrayList<>();
    resources.add(target);
  }

  @Override
  public List<CustomQueryOption> getCustomQueryOptions() {
    fail();
    return null;
  }

  @Override
  public ExpandOption getExpandOption() {
    fail();
    return null;
  }

  @Override
  public FilterOption getFilterOption() {
    return null;
  }

  @Override
  public FormatOption getFormatOption() {
    fail();
    return null;
  }

  @Override
  public IdOption getIdOption() {
    fail();
    return null;
  }

  @Override
  public CountOption getCountOption() {
    fail();
    return null;
  }

  @Override
  public OrderByOption getOrderByOption() {
    fail();
    return null;
  }

  @Override
  public SearchOption getSearchOption() {
    fail();
    return null;
  }

  @Override
  public SelectOption getSelectOption() {
    fail();
    return null;
  }

  @Override
  public SkipOption getSkipOption() {
    fail();
    return null;
  }

  @Override
  public SkipTokenOption getSkipTokenOption() {
    fail();
    return null;
  }

  @Override
  public TopOption getTopOption() {
    fail();
    return null;
  }

  @Override
  public List<UriResource> getUriResourceParts() {
    return resources;
  }

  @Override
  public String getValueForAlias(String alias) {
    fail();
    return null;
  }

  @Override
  public ApplyOption getApplyOption() {
    return null;
  }

  @Override
  public DeltaTokenOption getDeltaTokenOption() {
    return null;
  }

}