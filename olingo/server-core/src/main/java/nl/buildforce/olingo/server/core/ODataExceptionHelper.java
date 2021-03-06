/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import java.util.Locale;

import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataServerError;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException;
import nl.buildforce.olingo.server.api.etag.PreconditionException;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.core.uri.parser.UriParserException;
import nl.buildforce.olingo.server.core.uri.parser.UriParserSemanticException;
import nl.buildforce.olingo.server.core.uri.parser.UriParserSyntaxException;
import nl.buildforce.olingo.server.core.uri.validator.UriValidationException;

public class ODataExceptionHelper {

  private ODataExceptionHelper() {
    // Private Constructor
  }

  public static ODataServerError createServerErrorObject(UriValidationException e,
                                                         Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    return serverError;
  }

  public static ODataServerError createServerErrorObject(UriParserSemanticException e,
                                                         Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    if (UriParserSemanticException.MessageKeys.RESOURCE_NOT_FOUND.equals(e.getMessageKey())
        || UriParserSemanticException.MessageKeys.PROPERTY_NOT_IN_TYPE.equals(e.getMessageKey())) {
      serverError.setStatusCode(HttpStatusCode.NOT_FOUND.getStatusCode());
    } else if (UriParserSemanticException.MessageKeys.NOT_IMPLEMENTED.equals(e.getMessageKey())) {
      serverError.setStatusCode(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode());
    } else {
      serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    }
    return serverError;
  }

  public static ODataServerError createServerErrorObject(UriParserSyntaxException e,
                                                         Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(
        UriParserSyntaxException.MessageKeys.WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION_FORMAT.equals(e.getMessageKey()) ?
            HttpStatusCode.NOT_ACCEPTABLE.getStatusCode() :
              HttpStatusCode.BAD_REQUEST.getStatusCode());
    return serverError;
  }

  public static ODataServerError createServerErrorObject(UriParserException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    return serverError;
  }

  public static ODataServerError createServerErrorObject(ContentNegotiatorException e,
                                                         Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(HttpStatusCode.NOT_ACCEPTABLE.getStatusCode());
    return serverError;
  }

  public static ODataServerError createServerErrorObject(AcceptHeaderContentNegotiatorException e,
                                                         Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    return serverError;
  }
  
  public static ODataServerError createServerErrorObject(ODataHandlerException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    if (ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED.equals(e.getMessageKey())
        || ODataHandlerException.MessageKeys.PROCESSOR_NOT_IMPLEMENTED.equals(e.getMessageKey())) {
      serverError.setStatusCode(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode());
    } else if (ODataHandlerException.MessageKeys.ODATA_VERSION_NOT_SUPPORTED.equals(e.getMessageKey())
        || ODataHandlerException.MessageKeys.INVALID_HTTP_METHOD.equals(e.getMessageKey())
        || ODataHandlerException.MessageKeys.AMBIGUOUS_XHTTP_METHOD.equals(e.getMessageKey())
        || ODataHandlerException.MessageKeys.MISSING_CONTENT_TYPE.equals(e.getMessageKey())
        || ODataHandlerException.MessageKeys.INVALID_CONTENT_TYPE.equals(e.getMessageKey())
        || ODataHandlerException.MessageKeys.UNSUPPORTED_CONTENT_TYPE.equals(e.getMessageKey())
        || ODataHandlerException.MessageKeys.INVALID_PREFER_HEADER.equals(e.getMessageKey())
        || ODataHandlerException.MessageKeys.INVALID_PAYLOAD.equals(e.getMessageKey())) {
      serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    } else if (ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED.equals(e.getMessageKey())) {
      serverError.setStatusCode(HttpStatusCode.METHOD_NOT_ALLOWED.getStatusCode());
    }

    return serverError;
  }

  public static ODataServerError createServerErrorObject(SerializerException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    return serverError;
  }

  public static ODataServerError createServerErrorObject(DeserializerException e, Locale requestedLocale) {
    return basicTranslatedError(e, requestedLocale)
        .setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
  }

  public static ODataServerError createServerErrorObject(PreconditionException e,
                                                         Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    if (PreconditionException.MessageKeys.MISSING_HEADER == e.getMessageKey()) {
      serverError.setStatusCode(HttpStatusCode.PRECONDITION_REQUIRED.getStatusCode());
    } else if (PreconditionException.MessageKeys.FAILED == e.getMessageKey()) {
      serverError.setStatusCode(HttpStatusCode.PRECONDITION_FAILED.getStatusCode());
    }
    return serverError;
  }

  public static ODataServerError createServerErrorObject(ODataLibraryException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    if(e instanceof SerializerException || e instanceof DeserializerException){
      serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    }
    return serverError;
  }

  public static ODataServerError createServerErrorObject(ODataApplicationException e) {
    ODataServerError serverError = basicServerError(e);
    serverError.setStatusCode(e.getStatusCode());
    serverError.setLocale(e.getLocale());
    serverError.setCode(e.getODataErrorCode());
    serverError.setMessage(e.getLocalizedMessage());
    return serverError;
  }

  public static ODataServerError createServerErrorObject(Exception e) {
    ODataServerError serverError = basicServerError(e);
    serverError.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
    serverError.setLocale(Locale.ENGLISH);
    return serverError;
  }

  private static ODataServerError basicServerError(Exception e) {
    ODataServerError serverError = new ODataServerError().setException(e).setMessage(e.getMessage());
    if (serverError.getMessage() == null) {
      serverError.setMessage("OData Library: An exception without message text was thrown.");
    }
    return serverError;
  }

  private static ODataServerError basicTranslatedError(ODataLibraryException e,
                                                       Locale requestedLocale) {
    ODataServerError serverError = basicServerError(e);
    ODataLibraryException.ODataErrorMessage translatedMessage = e.getTranslatedMessage(requestedLocale);
    serverError.setMessage(translatedMessage.getMessage());
    serverError.setLocale(translatedMessage.getLocale());
    serverError.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
    return serverError;
  }
}
