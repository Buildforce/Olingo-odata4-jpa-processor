/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents a single orderby information
 * For example: http://.../Employees?$orderby=Name
 */
public interface OrderByItem {

  /**
   * Returns the sort order of the orderby item
   * @return if false (default) the sort order is ascending, if true the sort order is descending
   */
  boolean isDescending();

  /**
   * @return Expression which is used to order the items
   */
  Expression getExpression();

}
