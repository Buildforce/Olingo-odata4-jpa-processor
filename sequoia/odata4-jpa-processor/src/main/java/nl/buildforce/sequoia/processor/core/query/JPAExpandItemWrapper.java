package nl.buildforce.sequoia.processor.core.query;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;
import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;
import nl.buildforce.olingo.server.api.uri.queryoption.CustomQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.DeltaTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandItem;
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

import java.util.Collections;
import java.util.List;

// TODO In case of second level $expand expandItem.getResourcePath() returns an empty UriInfoResource => Bug or
// Feature?
public class JPAExpandItemWrapper implements JPAExpandItem {
  private final ExpandItem item;
  private final JPAEntityType jpaEntityType;

  public JPAExpandItemWrapper(final JPAServiceDocument sd, final ExpandItem item) {
    this.item = item;
    this.jpaEntityType = sd.getEntity(Util.determineTargetEntityType(getUriResourceParts()));
  }

  public JPAExpandItemWrapper(final ExpandItem item, final JPAEntityType jpaEntityType) {
    this.item = item;
    this.jpaEntityType = jpaEntityType;
  }

  @Override
  public List<CustomQueryOption> getCustomQueryOptions() {
    return null;
  }

  @Override
  public ExpandOption getExpandOption() {
    return item.getExpandOption();
  }

  @Override
  public FilterOption getFilterOption() {
    return item.getFilterOption();
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
    return item.getCountOption();
  }

  @Override
  public OrderByOption getOrderByOption() {
    return item.getOrderByOption();
  }

  @Override
  public SearchOption getSearchOption() {
    return item.getSearchOption();
  }

  @Override
  public SelectOption getSelectOption() {
    return item.getSelectOption();
  }

  @Override
  public SkipOption getSkipOption() {
    return item.getSkipOption();
  }

  @Override
  public SkipTokenOption getSkipTokenOption() {
    return null;
  }

  @Override
  public TopOption getTopOption() {
    return item.getTopOption();
  }

  @Override
  public List<UriResource> getUriResourceParts() {
    return item.getResourcePath() != null ? item.getResourcePath().getUriResourceParts() : Collections.emptyList();
  }

  @Override
  public String getValueForAlias(final String alias) {
    return null;
  }

  /*
   * (non-Javadoc)
   *

   * @see nl.buildforce.sequoia.processor.core.query.JPAExpandItem#getEntityType()
   */
  @Override
  public JPAEntityType getEntityType() {
    return jpaEntityType;
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