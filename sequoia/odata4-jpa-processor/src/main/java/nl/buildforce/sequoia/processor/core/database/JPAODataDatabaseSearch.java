package nl.buildforce.sequoia.processor.core.database;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;

public interface JPAODataDatabaseSearch {
  /**
   * Search implemented differently in various databases, so a database specific implementation needs to be provided.
   * For details about search at OData see:<p>
   * <a href=
   * "http://docs.oasis-open.org/odata/odata/v4.0/os/part1-protocol/odata-v4.0-os-part1-protocol.html#_Toc372793700">
   * OData Version 4.0 Part 1 - 11.2.5.6 System Query Option $search</a><p>
   * <a href=
   * "http://docs.oasis-open.org/odata/odata/v4.0/os/part2-url-conventions/odata-v4.0-os-part2-url-conventions.html#_Toc372793865">
   * OData Version 4.0 Part 2 - 5.1.7 System Query Option $search</a>
   * @return
   * @throws ODataApplicationException
   */
  Expression<Boolean> createSearchWhereClause()
      throws ODataApplicationException;

}