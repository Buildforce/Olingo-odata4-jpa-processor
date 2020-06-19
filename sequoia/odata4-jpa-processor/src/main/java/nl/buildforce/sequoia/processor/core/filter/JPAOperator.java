package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.ODataApplicationException;

public interface JPAOperator {
  Object get() throws ODataApplicationException;

  String getName();
}