/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Locale;

import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataServerError;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException;
import nl.buildforce.olingo.server.api.etag.PreconditionException;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.core.uri.parser.UriParserException;
import nl.buildforce.olingo.server.core.uri.parser.UriParserSemanticException;
import nl.buildforce.olingo.server.core.uri.validator.UriValidationException;
import org.junit.Test;

public class ExceptionHelperTest {

  private static final String DEV_MSG = "devMsg";

  @Test
  public void withRuntimeException() {
    Exception e = new NullPointerException();
    ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e);
    assertEquals(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), serverError.getStatusCode());
    assertEquals("OData Library: An exception without message text was thrown.", serverError.getMessage());
    assertEquals(e, serverError.getException());
  }

  @Test
  public void withRuntimeExceptionAndText() {
    Exception e = new NullPointerException("Text");
    ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e);
    assertEquals(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), serverError.getStatusCode());
    assertEquals("Text", serverError.getMessage());
    assertEquals(e, serverError.getException());
  }

  @Test
  public void uriValidatorExceptionMustLeadToBadRequest() {
    for (ODataLibraryException.MessageKey key : UriValidationException.MessageKeys.values()) {
      UriValidationException e = new UriValidationException(DEV_MSG, key);
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      checkStatusCode(serverError, HttpStatusCode.BAD_REQUEST, e);
    }
  }

  @Test
  public void deserializerExceptionMustLeadToBadRequest() {
    for (ODataLibraryException.MessageKey key : DeserializerException.MessageKeys.values()) {
      DeserializerException e = new DeserializerException(DEV_MSG, key);
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      checkStatusCode(serverError, HttpStatusCode.BAD_REQUEST, e);
    }
  }

  @Test
  public void serializerExceptionMustLeadToBadRequest() {
    for (ODataLibraryException.MessageKey key : SerializerException.MessageKeys.values()) {
      SerializerException e = new SerializerException(DEV_MSG, key);
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      checkStatusCode(serverError, HttpStatusCode.BAD_REQUEST, e);
    }
  }
  
  @Test
  public void libraryExceptionLeadToBadRequest() {
      ODataLibraryException e = new SerializerException(DEV_MSG, SerializerException.MessageKeys.MISSING_PROPERTY);
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      checkStatusCode(serverError, HttpStatusCode.BAD_REQUEST, e);
      e = new SerializerException(DEV_MSG, DeserializerException.MessageKeys.DUPLICATE_PROPERTY);
      serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      checkStatusCode(serverError, HttpStatusCode.BAD_REQUEST, e);
  }

  @Test
  public void contentNegotiatorExceptionMustLeadToNotAcceptable() {
    for (ODataLibraryException.MessageKey key : ContentNegotiatorException.MessageKeys.values()) {
      ContentNegotiatorException e = new ContentNegotiatorException(DEV_MSG, key);
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      checkStatusCode(serverError, HttpStatusCode.NOT_ACCEPTABLE, e);
    }
  }

  @Test
  public void preconditionRequiredTesting() {
    for (ODataLibraryException.MessageKey key : PreconditionException.MessageKeys.values()) {
      PreconditionException e = new PreconditionException(DEV_MSG, key);
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      if (e.getMessageKey().equals(PreconditionException.MessageKeys.FAILED)) {
        checkStatusCode(serverError, HttpStatusCode.PRECONDITION_FAILED, e);
      } else if (e.getMessageKey().equals(PreconditionException.MessageKeys.MISSING_HEADER)) {
        checkStatusCode(serverError, HttpStatusCode.PRECONDITION_REQUIRED, e);
      } else if (e.getMessageKey().equals(PreconditionException.MessageKeys.INVALID_URI)) {
        checkStatusCode(serverError, HttpStatusCode.INTERNAL_SERVER_ERROR, e);
      } else {
        fail("Unexpected message key for: " + e.getClass().getName());
      }
    }
  }

  @Test
  public void httpHandlerExceptions() {
    for (ODataLibraryException.MessageKey key : ODataHandlerException.MessageKeys.values()) {
      ODataHandlerException e = new ODataHandlerException(DEV_MSG, key);
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);

      if (key.equals(ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED)
          || key.equals(ODataHandlerException.MessageKeys.PROCESSOR_NOT_IMPLEMENTED)) {
        checkStatusCode(serverError, HttpStatusCode.NOT_IMPLEMENTED, e);
      } else if (key.equals(ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED)) {
        checkStatusCode(serverError, HttpStatusCode.METHOD_NOT_ALLOWED, e);
      } else {
        checkStatusCode(serverError, HttpStatusCode.BAD_REQUEST, e);
      }
    }
  }

  @Test
  public void withNotImplementedException() {
    UriParserSemanticException  e = new UriParserSemanticException("Exception",
        UriParserSemanticException.MessageKeys.NOT_IMPLEMENTED, "Method");
    ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, Locale.ENGLISH);
    assertEquals(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), serverError.getStatusCode());
    assertEquals("'Method' is not implemented!", serverError.getMessage());
    assertEquals(e, serverError.getException());
  }
  
  @Test
  public void uriParserException() {
    UriParserException  e = new UriParserSemanticException("Exception",
        UriParserSemanticException.MessageKeys.NOT_IMPLEMENTED, "Method");
    ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, Locale.ENGLISH);
    assertEquals(HttpStatusCode.BAD_REQUEST.getStatusCode(), serverError.getStatusCode());
    assertEquals("'Method' is not implemented!", serverError.getMessage());
    assertEquals(e, serverError.getException());
  }
  
  @Test
  public void acceptHeaderException() {
    AcceptHeaderContentNegotiatorException   e = new AcceptHeaderContentNegotiatorException ("Exception",
        UriParserSemanticException.MessageKeys.INVALID_KEY_VALUE, "Method");
    ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, Locale.ENGLISH);
    assertEquals(HttpStatusCode.BAD_REQUEST.getStatusCode(), serverError.getStatusCode());
    assertEquals("Missing message for key 'INVALID_KEY_VALUE'!", serverError.getMessage());
    assertEquals(e, serverError.getException());
  }
  
  private void checkStatusCode(ODataServerError serverError, HttpStatusCode statusCode,
                               ODataLibraryException exception) {
    assertEquals("FailedKey: " + exception.getMessageKey().getKey(),
        statusCode.getStatusCode(), serverError.getStatusCode());
  }
}
