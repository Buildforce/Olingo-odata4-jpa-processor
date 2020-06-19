package nl.buildforce.sequoia.processor.core.processor;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;

public interface JPARequestProcessor {

  <K extends Comparable<K>> void retrieveData(ODataRequest request, ODataResponse response,
                                              ContentType responseFormat) throws ODataJPAException, ODataApplicationException, ODataLibraryException;
}