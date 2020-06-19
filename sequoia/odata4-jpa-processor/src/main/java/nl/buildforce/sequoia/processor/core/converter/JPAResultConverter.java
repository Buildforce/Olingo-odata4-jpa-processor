package nl.buildforce.sequoia.processor.core.converter;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.olingo.server.api.ODataApplicationException;

import java.util.Collection;

public interface JPAResultConverter {

  Object getResult(final JPAExpandResult jpaResult, final Collection<JPAPath> requestedSelection)
      throws ODataApplicationException;

}