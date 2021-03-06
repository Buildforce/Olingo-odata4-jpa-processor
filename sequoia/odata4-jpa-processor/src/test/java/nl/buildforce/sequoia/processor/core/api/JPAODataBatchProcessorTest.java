package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.sequoia.processor.core.api.JPAODataTransactionFactory.JPAODataTransaction;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPATransactionException;
// import nl.buildforce.sequoia.processor.core.processor.JPAEmptyDebugger;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.batch.BatchFacade;
import nl.buildforce.olingo.server.api.deserializer.batch.ODataResponsePart;
import nl.buildforce.olingo.server.api.serializer.BatchSerializerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.RollbackException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JPAODataBatchProcessorTest {
  private JPAODataBatchProcessor cut;

  @Mock
  private EntityManager em;
  @Mock
  private JPAODataTransaction transaction;
  @Mock
  private OData odata;
  @Mock
  private ServiceMetadata serviceMetadata;
  @Mock
  private BatchFacade facade;
  @Mock
  private ODataRequest request;
  @Mock
  private ODataResponse response;
  @Mock
  private RollbackException e;
  @Mock
  private JPAODataCRUDContextAccess context;
  @Mock
  private JPACUDRequestHandler cudHandler;
  @Mock
  private JPAODataRequestContextAccess requestContext;
  @Mock
  private JPAODataTransactionFactory factory;

  private List<ODataRequest> requests;

  @BeforeEach
  public void setup() throws ODataJPATransactionException {
    MockitoAnnotations.initMocks(this);
    when(requestContext.getEntityManager()).thenReturn(em);
    when(requestContext.getCUDRequestHandler()).thenReturn(cudHandler);
    when(requestContext.getTransactionFactory()).thenReturn(factory);
    when(factory.createTransaction()).thenReturn(transaction);
    cut = new JPAODataBatchProcessor(requestContext);
    cut.init(odata, serviceMetadata);
    requests = new ArrayList<>();
    requests.add(request);
    // when(requestContext.getDebugger()).thenReturn(new JPAEmptyDebugger());
  }

  @Test
  public void whenNotOptimisticLockRollBackExceptionThenThrowODataJPAProcessorExceptionWithHttpCode500()
      throws ODataApplicationException, ODataLibraryException {
    when(response.getStatusCode()).thenReturn(HttpStatusCode.OK.getStatusCode());
    when(facade.handleODataRequest(request)).thenReturn(response);
    doThrow(e).when(transaction).commit();

    final ODataJPAProcessorException act = assertThrows(ODataJPAProcessorException.class,
        () -> cut.processChangeSet(facade, requests));
    assertEquals(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), act.getStatusCode());
  }

  @Test
  public void whenOptimisticLockRollBackExceptionThenThrowODataJPAProcessorExceptionWithHttpCode412()
      throws ODataApplicationException, ODataLibraryException {
    when(response.getStatusCode()).thenReturn(HttpStatusCode.OK.getStatusCode());
    when(facade.handleODataRequest(request)).thenReturn(response);
    doThrow(e).when(transaction).commit();
    when(e.getCause()).thenReturn(new OptimisticLockException());

    final ODataJPAProcessorException act = assertThrows(ODataJPAProcessorException.class,
        () -> cut.processChangeSet(facade, requests));
    assertEquals(HttpStatusCode.PRECONDITION_FAILED.getStatusCode(), act.getStatusCode());

  }

  @Test
  public void whenSuccessfulThenCallValidateChanges() throws ODataApplicationException,
      ODataLibraryException {
    cut = new JPAODataBatchProcessor(requestContext);

    when(response.getStatusCode()).thenReturn(HttpStatusCode.OK.getStatusCode());
    when(facade.handleODataRequest(request)).thenReturn(response);

    cut.processChangeSet(facade, requests);
    verify(cudHandler, times(1)).validateChanges(em);
  }

  @Test
  public void whenValidateChangesThrowsThenRollbackAndThrow() throws ODataApplicationException,
      ODataLibraryException {
    cut = new JPAODataBatchProcessor(requestContext);
    ODataJPAProcessException error = new ODataJPAProcessorException(
        ODataJPAProcessorException.MessageKeys.GETTER_NOT_FOUND, HttpStatusCode.BAD_REQUEST);
    when(response.getStatusCode()).thenReturn(HttpStatusCode.OK.getStatusCode());
    when(facade.handleODataRequest(request)).thenReturn(response);
    doThrow(error).when(cudHandler).validateChanges(em);
    assertThrows(ODataJPAProcessorException.class, () -> cut.processChangeSet(facade, requests));
    verify(transaction, never()).commit();
    // FvdB 2021-03--04 verify(transaction, times(1)).rollback();
  }
//ODataLibraryException

  @Test
  public void whenODataLibraryExceptionThrowsThenRollbackAndThrow() throws ODataApplicationException,
      ODataLibraryException {
    cut = new JPAODataBatchProcessor(requestContext);
    ODataLibraryException error = new BatchSerializerException("",
        BatchSerializerException.MessageKeys.MISSING_CONTENT_ID, "");
    when(response.getStatusCode()).thenReturn(HttpStatusCode.OK.getStatusCode());
    when(facade.handleODataRequest(request)).thenThrow(error);
    assertThrows(ODataLibraryException.class, () -> cut.processChangeSet(facade, requests));
    verify(transaction, never()).commit();
    verify(transaction, times(1)).rollback();
  }

  @Test
  public void whenNoExceptionOccurredThenCommit() throws ODataApplicationException, ODataLibraryException {

    when(response.getStatusCode()).thenReturn(HttpStatusCode.OK.getStatusCode());
    when(facade.handleODataRequest(request)).thenReturn(response);

    final ODataResponsePart act = cut.processChangeSet(facade, requests);
    verify(transaction, times(1)).commit();
    assertTrue(act.isChangeSet());
  }

  @Test
  public void whenProcessChangeSetReturnsUnsuccessfulCallThenRollback() throws ODataApplicationException,
      ODataLibraryException {
    cut = new JPAODataBatchProcessor(requestContext);

    when(response.getStatusCode()).thenReturn(HttpStatusCode.BAD_REQUEST.getStatusCode());
    when(facade.handleODataRequest(request)).thenReturn(response);

    final ODataResponsePart act = cut.processChangeSet(facade, requests);
    verify(cudHandler, never()).validateChanges(em);
    verify(transaction, never()).commit();
    verify(transaction, times(1)).rollback();
    assertFalse(act.isChangeSet());
  }

  @Test
  public void whenNoTransactionCloudNotBeCreatedThenThrowWith501() throws ODataApplicationException,
      ODataLibraryException {

    when(factory.createTransaction()).thenThrow(new ODataJPATransactionException(
        ODataJPATransactionException.MessageKeys.CANNOT_CREATE_NEW_TRANSACTION));
    when(response.getStatusCode()).thenReturn(HttpStatusCode.OK.getStatusCode());

    final ODataJPAProcessorException act = assertThrows(ODataJPAProcessorException.class, () -> cut.processChangeSet(
        facade, requests));
    assertEquals(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), act.getStatusCode());
    verify(facade, never()).handleODataRequest(any());
  }

}