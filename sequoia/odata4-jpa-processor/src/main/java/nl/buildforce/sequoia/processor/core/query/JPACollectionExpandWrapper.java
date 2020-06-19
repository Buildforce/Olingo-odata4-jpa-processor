package nl.buildforce.sequoia.processor.core.query;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.UriResource;
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

public class JPACollectionExpandWrapper implements JPAExpandItem {
  private final JPAEntityType jpaEntityType;
  private final UriInfoResource uriInfo;

  JPACollectionExpandWrapper(final JPAEntityType jpaEntityType, final UriInfoResource uriInfo) {
    this.jpaEntityType = jpaEntityType;
    this.uriInfo = uriInfo;
  }

  @Override
  public List<CustomQueryOption> getCustomQueryOptions() {
    return new ArrayList<>(0);
  }

  @Override
  public ExpandOption getExpandOption() {
    return null;
  }

  @Override
  public FilterOption getFilterOption() {
    return uriInfo.getFilterOption();
  }

  @Override
  public FormatOption getFormatOption() {
    return null;
  }

  @Override
  public IdOption getIdOption() {
    return null;
  }

  @Override
  public CountOption getCountOption() {
    return uriInfo.getCountOption();
  }

  @Override
  public DeltaTokenOption getDeltaTokenOption() {
    return null;
  }

  @Override
  public OrderByOption getOrderByOption() {
    return null;
  }

  @Override
  public SearchOption getSearchOption() {
    return null;
  }

  @Override
  public SelectOption getSelectOption() {
    return uriInfo.getSelectOption();
  }

  @Override
  public SkipOption getSkipOption() {
    return null;
  }

  @Override
  public SkipTokenOption getSkipTokenOption() {
    return null;
  }

  @Override
  public TopOption getTopOption() {
    return null;
  }

  @Override
  public ApplyOption getApplyOption() {
    return null;
  }

  @Override
  public List<UriResource> getUriResourceParts() {
    return uriInfo.getUriResourceParts();
  }

  @Override
  public String getValueForAlias(String alias) {
    return null;
  }

  @Override
  public JPAEntityType getEntityType() {
    return jpaEntityType;
  }

}