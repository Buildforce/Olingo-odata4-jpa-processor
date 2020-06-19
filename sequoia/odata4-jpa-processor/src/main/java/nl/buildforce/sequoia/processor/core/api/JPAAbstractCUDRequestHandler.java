package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.processor.core.modify.JPAUpdateResult;
import nl.buildforce.sequoia.processor.core.processor.JPARequestEntity;
import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;

import jakarta.persistence.EntityManager;

public abstract class JPAAbstractCUDRequestHandler implements JPACUDRequestHandler {

  @Override
  public void deleteEntity(final JPARequestEntity requestEntity, final EntityManager em)
      throws ODataJPAProcessException {

    throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_DELETE,
        HttpStatusCode.NOT_IMPLEMENTED);
  }

  @Override
  public Object createEntity(final JPARequestEntity requestEntity, final EntityManager em)
      throws ODataJPAProcessException {

    throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_CREATE,
        HttpStatusCode.NOT_IMPLEMENTED);

  }

  @Override
  public JPAUpdateResult updateEntity(final JPARequestEntity requestEntity, final EntityManager em,
                                      final HttpMethod httpMethod) throws ODataJPAProcessException {

    throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_UPDATE,
        HttpStatusCode.NOT_IMPLEMENTED);
  }

  @Override
  public void validateChanges(final EntityManager em) throws ODataJPAProcessException {
    // Do nothing. If needed override method.
  }

}