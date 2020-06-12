package nl.buildforce.sequoia.jpa.processor.core.converter;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import org.apache.olingo.server.api.ODataApplicationException;

import java.util.Collection;

public interface JPAResultConverter {

  Object getResult(final JPAExpandResult jpaResult, final Collection<JPAPath> requestedSelection)
      throws ODataApplicationException;

}