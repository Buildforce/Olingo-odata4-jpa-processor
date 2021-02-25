/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.FilterOption;
import nl.buildforce.olingo.server.api.uri.queryoption.apply.Filter;

/**
 * Represents the filter transformation.
 */
public class FilterImpl implements Filter {

  private FilterOption filterOption;

  @Override
  public Kind getKind() {
    return Kind.FILTER;
  }

  @Override
  public FilterOption getFilterOption() {
    return filterOption;
  }

  public FilterImpl setFilterOption(FilterOption filterOption) {
    this.filterOption = filterOption;
    return this;
  }
}
