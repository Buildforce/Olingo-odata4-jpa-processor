/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

import java.util.List;

/**
 * Represents the system query option $expand
 * For example: http://.../entitySet?$expand=Products,Customers
 */
public interface ExpandOption extends SystemQueryOption {

  /**
   * @return A list of resource paths which should be expanded
   */
  List<ExpandItem> getExpandItems();

}
