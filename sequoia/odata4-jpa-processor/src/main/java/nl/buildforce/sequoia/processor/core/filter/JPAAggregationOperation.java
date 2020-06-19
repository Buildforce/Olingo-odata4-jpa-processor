package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.ODataApplicationException;

public interface JPAAggregationOperation extends JPAOperator {
  @Override
  Object get() throws ODataApplicationException;

  JPAFilterAggregationType getAggregation();

}