package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.ODataApplicationException;

public interface JPAAggregationOperation extends JPAOperator {
  @Override
  Object get() throws ODataApplicationException;

  JPAFilterAggregationType getAggregation();

}