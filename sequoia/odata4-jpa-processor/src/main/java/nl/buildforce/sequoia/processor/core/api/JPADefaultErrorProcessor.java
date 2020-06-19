package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ODataServerError;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.processor.DefaultProcessor;
import nl.buildforce.olingo.server.api.processor.ErrorProcessor;

public final class JPADefaultErrorProcessor implements ErrorProcessor {
  private final ErrorProcessor defaultProcessor;

  JPADefaultErrorProcessor() { defaultProcessor = new DefaultProcessor(); }

  @Override
  public void init(OData odata, ServiceMetadata serviceMetadata) { defaultProcessor.init(odata, serviceMetadata); }

  @Override
  public void processError(ODataRequest request, ODataResponse response, ODataServerError serverError,
      ContentType responseFormat) { defaultProcessor.processError(request, response, serverError, responseFormat); }

}