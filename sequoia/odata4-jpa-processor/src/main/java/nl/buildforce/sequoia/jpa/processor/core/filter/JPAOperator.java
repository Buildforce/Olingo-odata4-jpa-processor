package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.ODataApplicationException;

public interface JPAOperator {
  Object get() throws ODataApplicationException;

  String getName();
}