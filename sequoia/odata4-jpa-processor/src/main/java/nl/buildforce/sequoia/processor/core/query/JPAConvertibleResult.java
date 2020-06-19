package nl.buildforce.sequoia.processor.core.query;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.processor.core.converter.JPAExpandResult;
import nl.buildforce.sequoia.processor.core.converter.JPATupleChildConverter;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAQueryException;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.server.api.ODataApplicationException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JPAConvertibleResult {
  /**
   *

   * @param converter
   * @return
   * @throws ODataApplicationException
   */
  Map<String, EntityCollection> asEntityCollection(final JPATupleChildConverter converter)
      throws ODataApplicationException;

  void putChildren(final Map<JPAAssociationPath, JPAExpandResult> childResults) throws ODataApplicationException;

  /**
   * Returns the entity collection of a given key. This method may internally perform <code>asEntityCollection</code>
   * @param key
   * @return
   * @throws ODataApplicationException
   */
  EntityCollection getEntityCollection(final String key) throws ODataApplicationException;

  /**
   * Returns a key pair if the query had $top and/or $skip and the key of the entity implements {@link Comparable}.
   * @param requestContext
   * @param hops
   * @return
   * @throws ODataJPAQueryException
   */
  default Optional<JPAKeyBoundary> getKeyBoundary(JPAODataRequestContextAccess requestContext,
      final List<JPANavigationPropertyInfo> hops) throws ODataJPAQueryException {
    return Optional.empty();
  }
}