/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.helper;

import java.util.HashMap;
import java.util.Map;

import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.server.core.uri.queryoption.ExpandItemImpl;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.core.uri.queryoption.ExpandOptionImpl;

public class ExpandTreeBuilderImpl extends ExpandTreeBuilder {

  private final Map<String, ExpandTreeBuilder> childBuilderCache = new HashMap<>();
  private final ExpandItemImpl parentItem;
  private ExpandOptionImpl expandOption;

  private ExpandTreeBuilderImpl(ExpandItemImpl parentItem) {
    this.parentItem = parentItem;
  }
  
  
  @Override
  public ExpandTreeBuilder expand(EdmNavigationProperty edmNavigationProperty) {
    if (expandOption == null) {
      expandOption = new ExpandOptionImpl();
      if(parentItem != null && parentItem.getExpandOption() == null){
        parentItem.setSystemQueryOption(expandOption);
      }
    }
    
    ExpandTreeBuilder builder = childBuilderCache.get(edmNavigationProperty.getName());
    if(builder == null){
      ExpandItemImpl expandItem = buildExpandItem(edmNavigationProperty);
      expandOption.addExpandItem(expandItem);
      builder = new ExpandTreeBuilderImpl(expandItem);
      childBuilderCache.put(edmNavigationProperty.getName(), builder);
    }
    
    return builder;
  }

  @Override
  public ExpandOption build() {
    return expandOption;
  }
  
  public static ExpandTreeBuilder create(){
    return new ExpandTreeBuilderImpl(null);
  }
}