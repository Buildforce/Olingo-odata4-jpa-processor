/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.ex.ODataRuntimeException;
import nl.buildforce.olingo.server.api.uri.UriInfoEntityId;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriInfoBatch;
import nl.buildforce.olingo.server.api.uri.UriInfoCrossjoin;
import nl.buildforce.olingo.server.api.uri.UriInfoKind;
import nl.buildforce.olingo.server.api.uri.UriInfoMetadata;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.UriInfoService;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.queryoption.AliasQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;
import nl.buildforce.olingo.server.api.uri.queryoption.CountOption;
import nl.buildforce.olingo.server.api.uri.queryoption.DeltaTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.CustomQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FilterOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FormatOption;
import nl.buildforce.olingo.server.api.uri.queryoption.IdOption;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByOption;
import nl.buildforce.olingo.server.api.uri.queryoption.QueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SkipTokenOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;
import nl.buildforce.olingo.server.api.uri.queryoption.TopOption;

public class UriInfoImpl implements UriInfo {

  private UriInfoKind kind;

  // for $entity
  private final List<String> entitySetNames = new ArrayList<>();
  // for $entity
  private EdmEntityType entityTypeCast;

  private UriResource lastResourcePart;
  private final List<UriResource> pathParts = new ArrayList<>();

  private final Map<SystemQueryOptionKind, SystemQueryOption> systemQueryOptions =
      new EnumMap<>(SystemQueryOptionKind.class);
  private final Map<String, AliasQueryOption> aliases = new HashMap<>();
  private final List<CustomQueryOption> customQueryOptions = new ArrayList<>();

  private String fragment;

  public UriInfoImpl setKind(UriInfoKind kind) {
    this.kind = kind;
    return this;
  }

  @Override
  public UriInfoKind getKind() {
    return kind;
  }

/*
  @Override
  public UriInfoAll asUriInfoAll() {
    return this;
  }
*/

  @Override
  public UriInfoBatch asUriInfoBatch() {
    return this;
  }

  @Override
  public UriInfoCrossjoin asUriInfoCrossjoin() {
    return this;
  }

  @Override
  public UriInfoEntityId asUriInfoEntityId() {
    return this;
  }

  @Override
  public UriInfoService asUriInfoService() {
    return this;
  }

  @Override
  public UriInfoMetadata asUriInfoMetadata() {
    return this;
  }

  @Override
  public UriInfoResource asUriInfoResource() {
    return this;
  }

  public UriInfoImpl addEntitySetName(String entitySet) {
    entitySetNames.add(entitySet);
    return this;
  }

  @Override
  public List<String> getEntitySetNames() {
    return Collections.unmodifiableList(entitySetNames);
  }

  public UriInfoImpl setEntityTypeCast(EdmEntityType type) {
    entityTypeCast = type;
    return this;
  }

  @Override
  public EdmEntityType getEntityTypeCast() {
    return entityTypeCast;
  }

  public UriInfoImpl addResourcePart(UriResource uriPathInfo) {
    pathParts.add(uriPathInfo);
    lastResourcePart = uriPathInfo;
    return this;
  }

  public UriResource getLastResourcePart() {
    return lastResourcePart;
  }

  @Override
  public List<UriResource> getUriResourceParts() {
    return Collections.unmodifiableList(pathParts);
  }

  public UriInfoImpl setQueryOption(QueryOption option) {
    if (option instanceof SystemQueryOption) {
      setSystemQueryOption((SystemQueryOption) option);
    } else if (option instanceof AliasQueryOption) {
      addAlias((AliasQueryOption) option);
    } else if (option instanceof CustomQueryOption) {
      addCustomQueryOption((CustomQueryOption) option);
    }
    return this;
  }

  /**
   * Adds system query option.
   * @param systemOption the option to be added
   * @return this object for method chaining
   * @throws ODataRuntimeException if an unsupported option is provided
   * or an option of this kind has been added before
   */
  public UriInfoImpl setSystemQueryOption(SystemQueryOption systemOption) {
    SystemQueryOptionKind systemQueryOptionKind = systemOption.getKind();
    if (systemQueryOptions.containsKey(systemQueryOptionKind)) {
      throw new ODataRuntimeException("Double System Query Option: " + systemOption.getName());
    }

    switch (systemQueryOptionKind) {
      case EXPAND, FILTER, FORMAT, ID, COUNT, ORDERBY, SEARCH, SELECT, SKIP, SKIPTOKEN, DELTATOKEN, TOP, LEVELS, APPLY -> systemQueryOptions.put(systemQueryOptionKind, systemOption);
      default -> throw new ODataRuntimeException("Unsupported System Query Option: " + systemOption.getName());
    }
    return this;
  }

  @Override
  public ExpandOption getExpandOption() {
    return (ExpandOption) systemQueryOptions.get(SystemQueryOptionKind.EXPAND);
  }

  @Override
  public FilterOption getFilterOption() {
    return (FilterOption) systemQueryOptions.get(SystemQueryOptionKind.FILTER);
  }

  @Override
  public FormatOption getFormatOption() {
    return (FormatOption) systemQueryOptions.get(SystemQueryOptionKind.FORMAT);
  }

  @Override
  public IdOption getIdOption() {
    return (IdOption) systemQueryOptions.get(SystemQueryOptionKind.ID);
  }

  @Override
  public CountOption getCountOption() {
    return (CountOption) systemQueryOptions.get(SystemQueryOptionKind.COUNT);
  }

  @Override
  public OrderByOption getOrderByOption() {
    return (OrderByOption) systemQueryOptions.get(SystemQueryOptionKind.ORDERBY);
  }

  @Override
  public SearchOption getSearchOption() {
    return (SearchOption) systemQueryOptions.get(SystemQueryOptionKind.SEARCH);
  }

  @Override
  public SelectOption getSelectOption() {
    return (SelectOption) systemQueryOptions.get(SystemQueryOptionKind.SELECT);
  }

  @Override
  public SkipOption getSkipOption() {
    return (SkipOption) systemQueryOptions.get(SystemQueryOptionKind.SKIP);
  }

  @Override
  public SkipTokenOption getSkipTokenOption() {
    return (SkipTokenOption) systemQueryOptions.get(SystemQueryOptionKind.SKIPTOKEN);
  }

  @Override
  public TopOption getTopOption() {
    return (TopOption) systemQueryOptions.get(SystemQueryOptionKind.TOP);
  }

  @Override
  public ApplyOption getApplyOption() {
    return (ApplyOption) systemQueryOptions.get(SystemQueryOptionKind.APPLY);
  }

  @Override
  public List<SystemQueryOption> getSystemQueryOptions() {
    return List.copyOf(systemQueryOptions.values());
  }

  public UriInfoImpl addAlias(AliasQueryOption alias) {
    if (aliases.containsKey(alias.getName())) {
      throw new ODataRuntimeException("Alias " + alias.getName() + " is already there.");
    } else {
      aliases.put(alias.getName(), alias);
    }
    return this;
  }

  @Override
  public String getValueForAlias(String alias) {
    AliasQueryOption aliasQueryOption = aliases.get(alias);
    return aliasQueryOption == null ? null : aliasQueryOption.getText();
  }

  public Map<String, AliasQueryOption> getAliasMap() {
    return Collections.unmodifiableMap(aliases);
  }

  @Override
  public List<AliasQueryOption> getAliases() {
    return List.copyOf(aliases.values());
  }

  public UriInfoImpl addCustomQueryOption(CustomQueryOption option) {
    if (option.getName() != null && !option.getName().isEmpty()) {
      customQueryOptions.add(option);
    }
    return this;
  }

  @Override
  public List<CustomQueryOption> getCustomQueryOptions() {
    return Collections.unmodifiableList(customQueryOptions);
  }

  public UriInfoImpl setFragment(String fragment) {
    this.fragment = fragment;
    return this;
  }

  @Override
  public String getFragment() {
    return fragment;
  }
  
  @Override
  public DeltaTokenOption getDeltaTokenOption() {
    return (DeltaTokenOption) systemQueryOptions.get(SystemQueryOptionKind.DELTATOKEN);
  }

}