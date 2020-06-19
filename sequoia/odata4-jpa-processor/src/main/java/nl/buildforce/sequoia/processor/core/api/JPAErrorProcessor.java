package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataServerError;

public interface JPAErrorProcessor { void processError(final ODataRequest request, final ODataServerError serverError);}