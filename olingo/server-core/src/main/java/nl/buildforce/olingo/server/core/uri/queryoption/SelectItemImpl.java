/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectItem;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;

public class SelectItemImpl implements SelectItem {

  private UriInfoResource path;

  private boolean isStar;
//  private FullQualifiedName addOperationsInSchemaNameSpace;

  private EdmType startTypeFilter;

  @Override
  public UriInfoResource getResourcePath() {

    return path;
  }

  public SelectItemImpl setResourcePath(UriInfoResource path) {
    this.path = path;
    return this;
  }

  @Override
  public boolean isStar() {
    return isStar;
  }

  public SelectItemImpl setStar(boolean isStar) {
    this.isStar = isStar;
    return this;
  }

  /*  @Override
    public boolean isAllOperationsInSchema() {
      return addOperationsInSchemaNameSpace != null;
    }

  @Override
  public FullQualifiedName getAllOperationsInSchemaNameSpace() {
    return addOperationsInSchemaNameSpace;
  }

  public void addAllOperationsInSchema(FullQualifiedName addOperationsInSchemaNameSpace) {
    this.addOperationsInSchemaNameSpace = addOperationsInSchemaNameSpace;
  }
*/
  @Override
  public EdmType getStartTypeFilter() {
    return startTypeFilter;
  }

  public SelectItemImpl setTypeFilter(EdmType startTypeFilter) {
    this.startTypeFilter = startTypeFilter;
    return this;
  }

}