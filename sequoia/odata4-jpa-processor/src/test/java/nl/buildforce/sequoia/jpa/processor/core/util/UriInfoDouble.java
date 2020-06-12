package nl.buildforce.sequoia.jpa.processor.core.util;

import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.server.api.uri.*;
import org.apache.olingo.server.api.uri.queryoption.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class UriInfoDouble implements UriInfo {

  private final SelectOption selOpts;
  private ExpandOption expandOpts;
  private List<UriResource> uriResources;

  public UriInfoDouble(final SelectOption selOpts) {
    this.selOpts = selOpts;
    this.uriResources = new ArrayList<>(0);
  }

  public UriInfoDouble(final UriInfoResource resourcePath) {
    this.selOpts = null;
    this.uriResources = resourcePath.getUriResourceParts();
  }

  @Override
  public FormatOption getFormatOption() {
    fail();
    return null;
  }

  @Override
  public String getFragment() {
    fail();
    return null;
  }

  @Override
  public List<CustomQueryOption> getCustomQueryOptions() {
    fail();
    return null;
  }

  @Override
  public ExpandOption getExpandOption() {
    return expandOpts;
  }

  @Override
  public FilterOption getFilterOption() {
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
    return selOpts;
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
    return uriResources;
  }

  @Override
  public String getValueForAlias(String alias) {
    fail();
    return null;
  }

  @Override
  public List<String> getEntitySetNames() {
    fail();
    return null;
  }

  @Override
  public EdmEntityType getEntityTypeCast() {
    fail();
    return null;
  }

  @Override
  public UriInfoKind getKind() {
    fail();
    return null;
  }

  @Override
  public UriInfoService asUriInfoService() {
    fail();
    return null;
  }

  @Override
  public UriInfoAll asUriInfoAll() {
    fail();
    return null;
  }

  @Override
  public UriInfoBatch asUriInfoBatch() {
    fail();
    return null;
  }

  @Override
  public UriInfoCrossjoin asUriInfoCrossjoin() {
    fail();
    return null;
  }

  @Override
  public UriInfoEntityId asUriInfoEntityId() {
    fail();
    return null;
  }

  @Override
  public UriInfoMetadata asUriInfoMetadata() {
    fail();
    return null;
  }

  @Override
  public UriInfoResource asUriInfoResource() {
    fail();
    return null;
  }

  @Override
  public List<SystemQueryOption> getSystemQueryOptions() {
    fail();
    return null;
  }

  @Override
  public List<AliasQueryOption> getAliases() {
    fail();
    return null;
  }

  static class primitiveDouble implements UriResourcePrimitiveProperty {

    @Override
    public EdmProperty getProperty() {
      fail();
      return null;
    }

    @Override
    public EdmType getType() {
      fail();
      return null;
    }

    @Override
    public boolean isCollection() {
      fail();
      return false;
    }

    @Override
    public String getSegmentValue(boolean includeFilters) {
      fail();
      return null;
    }

    @Override
    public String toString(boolean includeFilters) {
      fail();
      return null;
    }

    @Override
    public UriResourceKind getKind() {
      fail();
      return null;
    }

    @Override
    public String getSegmentValue() {
      fail();
      return null;
    }

  }

  static class complexDouble implements UriResourceComplexProperty {

    @Override
    public EdmProperty getProperty() {
      fail();
      return null;
    }

    @Override
    public EdmType getType() {
      fail();
      return null;
    }

    @Override
    public boolean isCollection() {
      fail();
      return false;
    }

    @Override
    public String getSegmentValue(boolean includeFilters) {
      fail();
      return null;
    }

    @Override
    public String toString(boolean includeFilters) {
      fail();
      return null;
    }

    @Override
    public UriResourceKind getKind() {
      fail();
      return null;
    }

    @Override
    public String getSegmentValue() {
      fail();
      return null;
    }

    @Override
    public EdmComplexType getComplexType() {
      fail();
      return null;
    }

    @Override
    public EdmComplexType getComplexTypeFilter() {
      fail();
      return null;
    }
  }

  static class propertyDouble implements UriResourceProperty {

    @Override
    public EdmType getType() {
      fail();
      return null;
    }

    @Override
    public boolean isCollection() {
      fail();
      return false;
    }

    @Override
    public String getSegmentValue(boolean includeFilters) {
      fail();
      return null;
    }

    @Override
    public String toString(boolean includeFilters) {
      fail();
      return null;
    }

    @Override
    public UriResourceKind getKind() {
      fail();
      return null;
    }

    @Override
    public String getSegmentValue() {
      fail();
      return null;
    }

    @Override
    public EdmProperty getProperty() {
      fail();
      return null;
    }
  }

  public void setExpandOpts(ExpandOption expandOpts) {
    this.expandOpts = expandOpts;
  }

  public void setUriResources(List<UriResource> uriResources) {
    this.uriResources = uriResources;
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