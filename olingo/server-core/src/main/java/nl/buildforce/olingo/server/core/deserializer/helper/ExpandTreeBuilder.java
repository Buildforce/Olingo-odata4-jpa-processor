/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.helper;

import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.server.core.uri.UriInfoImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceNavigationPropertyImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.ExpandItemImpl;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;

public abstract class ExpandTreeBuilder {
  
  public abstract ExpandTreeBuilder expand(EdmNavigationProperty edmNavigationProperty);

  public abstract ExpandOption build();
  
  protected ExpandItemImpl buildExpandItem(EdmNavigationProperty edmNavigationProperty) {
    return new ExpandItemImpl()
        .setResourcePath(new UriInfoImpl()
            .addResourcePart(new UriResourceNavigationPropertyImpl(edmNavigationProperty)));
  }
}
