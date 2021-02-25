/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents the system query option $filter
 * For example: http://.../entitySet?$filter=name eq 'tester'
 */
public interface FilterOption extends SystemQueryOption {

  /**
   * @return Expression tree created from the filter value (see {@link Expression})
   */
  Expression getExpression();
}
