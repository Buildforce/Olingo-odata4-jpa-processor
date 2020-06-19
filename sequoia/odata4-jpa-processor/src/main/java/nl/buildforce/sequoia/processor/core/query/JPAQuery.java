package nl.buildforce.sequoia.processor.core.query;

import nl.buildforce.olingo.server.api.ODataApplicationException;

public interface JPAQuery {
  JPAConvertibleResult execute() throws ODataApplicationException;
}
