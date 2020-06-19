package nl.buildforce.sequoia.processor.core.database;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPADataBaseFunction;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.UriResource;

import jakarta.persistence.EntityManager;
import java.util.List;

public interface JPAODataDatabaseTableFunction {

  <T> List<T> executeFunctionQuery(final List<UriResource> uriResourceParts, final JPADataBaseFunction jpaFunction,
      final EntityManager em) throws ODataApplicationException;

}