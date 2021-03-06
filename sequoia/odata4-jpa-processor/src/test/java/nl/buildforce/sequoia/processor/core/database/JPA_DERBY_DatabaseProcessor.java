package nl.buildforce.sequoia.processor.core.database;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPADataBaseFunction;
import nl.buildforce.sequoia.processor.core.exception.ODataJPADBAdaptorException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Expression;

import java.util.ArrayList;
import java.util.List;

import static nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_FUNC_WITH_NAVI;

class JPA_DERBY_DatabaseProcessor extends JPAAbstractDatabaseProcessor { // NOSONAR
  private static final String SELECT_BASE_PATTERN = "SELECT * FROM TABLE ($FUNCTIONNAME$($PARAMETER$))";
  private static final String SELECT_COUNT_PATTERN = "SELECT COUNT(*) FROM TABLE ($FUNCTIONNAME$($PARAMETER$))";

  @Override
  public Expression<Boolean> createSearchWhereClause()
      throws ODataApplicationException {
    throw new ODataJPADBAdaptorException(ODataJPADBAdaptorException.MessageKeys.NOT_SUPPORTED_SEARCH,
        HttpStatusCode.NOT_IMPLEMENTED);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> java.util.List<T> executeFunctionQuery(final List<UriResource> uriResourceParts,
      final JPADataBaseFunction jpaFunction, final EntityManager em) throws ODataApplicationException {
    /*
     * See https://db.apache.org/derby/docs/10.15/ref/rrefsqljtfinvoke.html
     */
    final UriResource last = uriResourceParts.get(uriResourceParts.size() - 1);

    if (last.getKind() == UriResourceKind.count) {
      final List<Long> countResult = new ArrayList<>();
      countResult.add(executeCountQuery(uriResourceParts, jpaFunction, em, SELECT_COUNT_PATTERN));
      return (List<T>) countResult;
    }
    if (last.getKind() == UriResourceKind.function)
      return executeQuery(uriResourceParts, jpaFunction, em, SELECT_BASE_PATTERN);
    throw new ODataJPAProcessorException(NOT_SUPPORTED_FUNC_WITH_NAVI, HttpStatusCode.NOT_IMPLEMENTED);

  }

}