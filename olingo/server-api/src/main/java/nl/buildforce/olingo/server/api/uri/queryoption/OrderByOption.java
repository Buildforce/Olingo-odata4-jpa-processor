/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

import java.util.List;

/**
 * Represents the system query option $orderby
 * For example: http://.../Employees?$orderby=Name, Age desc
 */
public interface OrderByOption extends SystemQueryOption {

  /**
   * @return List of single orders used in $orderby
   */
  List<OrderByItem> getOrders();

}
