package nl.buildforce.sequoia.jpa.processor.core.api;

import nl.buildforce.sequoia.jpa.processor.core.query.JPACountQuery;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfo;

import jakarta.persistence.EntityManager;

/**
 *
 * @author Oliver Grande
 *
 */
public interface JPAODataPagingProvider {
  /**
   * Returns the page related to a given skiptoken.
   * If the skiptoken is not known the method returns null.
   * @param skiptoken
   * @return
   */
  JPAODataPage getNextPage(final String skiptoken);

  /**
   * Based on the query the provider decides if a paging is required and return the first page.
   * @param uriInfo
   * @return
   * @throws ODataApplicationException
   */
  JPAODataPage getFirstPage(final UriInfo uriInfo, final Integer preferredPageSize, final JPACountQuery countQuery,
      final EntityManager em) throws ODataApplicationException;

}