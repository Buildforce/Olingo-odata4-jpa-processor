package nl.buildforce.sequoia.jpa.processor.core.database;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPADataBaseFunction;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriResource;

import jakarta.persistence.EntityManager;
import java.util.List;

public interface JPAODataDatabaseTableFunction {

  <T> List<T> executeFunctionQuery(final List<UriResource> uriResourceParts, final JPADataBaseFunction jpaFunction,
      final EntityManager em) throws ODataApplicationException;

}