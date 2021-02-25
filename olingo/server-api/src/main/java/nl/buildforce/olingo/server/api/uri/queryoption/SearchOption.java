/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchExpression;

/**
 * Represents the system query option $search
 * For example: http://.../entitySet?$search=SearchString
 */
public interface SearchOption extends SystemQueryOption {

  /**
   * @return Search expression tree created from the $search value (see {@link SearchExpression})
   */
  SearchExpression getSearchExpression();

}
