package nl.buildforce.sequoia.processor.core.processor;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.olingo.server.api.OData;

abstract class JPAAbstractGetRequestProcessor extends JPAAbstractRequestProcessor implements JPARequestProcessor {

  JPAAbstractGetRequestProcessor(OData odata, JPAODataCRUDContextAccess context,
      JPAODataRequestContextAccess requestContext) throws ODataJPAException {
    super(odata, context, requestContext);
  }
}