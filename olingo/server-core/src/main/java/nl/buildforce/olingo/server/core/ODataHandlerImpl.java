/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import java.util.LinkedList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.constants.ODataServiceVersion;
import nl.buildforce.olingo.commons.api.ex.ODataRuntimeException;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataHandler;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ODataServerError;
import nl.buildforce.olingo.server.api.OlingoExtension;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException;
import nl.buildforce.olingo.server.api.etag.CustomETagSupport;
import nl.buildforce.olingo.server.api.etag.PreconditionException;
import nl.buildforce.olingo.server.api.processor.DefaultProcessor;
import nl.buildforce.olingo.server.api.processor.ErrorProcessor;
import nl.buildforce.olingo.server.api.processor.Processor;
import nl.buildforce.olingo.server.api.serializer.CustomContentTypeSupport;
import nl.buildforce.olingo.server.api.serializer.RepresentationType;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.queryoption.FormatOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;
import nl.buildforce.olingo.server.core.uri.parser.Parser;
import nl.buildforce.olingo.server.core.uri.parser.UriParserException;
import nl.buildforce.olingo.server.core.uri.parser.UriParserSemanticException;
import nl.buildforce.olingo.server.core.uri.parser.UriParserSyntaxException;
import nl.buildforce.olingo.server.core.uri.queryoption.FormatOptionImpl;
import nl.buildforce.olingo.server.core.uri.validator.UriValidationException;
import nl.buildforce.olingo.server.core.uri.validator.UriValidator;

import static nl.buildforce.olingo.commons.api.http.HttpHeader.ODATA_MAX_VERSION;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.ODATA_VERSION;

public class ODataHandlerImpl implements ODataHandler {

  private final OData odata;
  private final List<Processor> processors = new LinkedList<>();
  private final ServiceMetadata serviceMetadata;

  private CustomContentTypeSupport customContentTypeSupport;
  private CustomETagSupport customETagSupport;

  private UriInfo uriInfo;

  public ODataHandlerImpl(OData odata, ServiceMetadata serviceMetadata) {
    this.odata = odata;
    this.serviceMetadata = serviceMetadata;
//    this.debugger = debugger;

    register(new DefaultRedirectProcessor());
    register(new DefaultProcessor());
  }

  public ODataResponse process(ODataRequest request) {
    ODataResponse response = new ODataResponse();
    // int responseHandle = debugger.startRuntimeMeasurement("ODataHandler", "process");
    try {
      processInternal(request, response);
    } catch (UriValidationException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError, e);
    } catch (UriParserSemanticException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError, e);
    } catch (UriParserSyntaxException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError, e);
    } catch (UriParserException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError, e);
    } catch (AcceptHeaderContentNegotiatorException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError, e);
    } catch (ContentNegotiatorException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError, e);
    } catch (SerializerException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError, e);
    } catch (DeserializerException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError, e);
    } catch (PreconditionException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError, e);
    } catch (ODataHandlerException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError, e);
    } catch (ODataApplicationException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e);
      handleException(request, response, serverError, e);
    } catch (Exception e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e);
      handleException(request, response, serverError, e);
    }
    // // debugger.stopRuntimeMeasurement(responseHandle);
    return response;
  }

  private void processInternal(ODataRequest request, ODataResponse response)
      throws ODataApplicationException, ODataLibraryException {
    // int measurementHandle = debugger.startRuntimeMeasurement("ODataHandler", "processInternal");

    response.setHeader(ODATA_VERSION, ODataServiceVersion.V40.toString());

    validateODataVersion(request);

    // int measurementUriParser = debugger.startRuntimeMeasurement("Parser", "parseUri");
    uriInfo = new Parser(serviceMetadata.getEdm(), odata)
        .parseUri(request.getRawODataPath(), request.getRawQueryPath(), request.getRawBaseUri());
    // debugger.stopRuntimeMeasurement(measurementUriParser);

    // int measurementUriValidator = debugger.startRuntimeMeasurement("UriValidator", "validate");
    HttpMethod method = request.getMethod();
    new UriValidator().validate(uriInfo, method);
    // debugger.stopRuntimeMeasurement(measurementUriValidator);

    // int measurementDispatcher = debugger.startRuntimeMeasurement("ODataDispatcher", "dispatch");
    new ODataDispatcher(uriInfo, this).dispatch(request, response);
  }

  public void handleException(ODataRequest request, ODataResponse response,
                              ODataServerError serverError, Exception exception) {
    // int measurementHandle = debugger.startRuntimeMeasurement("ODataHandler", "handleException");
    ErrorProcessor exceptionProcessor;
    try {
      exceptionProcessor = selectProcessor(ErrorProcessor.class);
    } catch (ODataHandlerException e) {
      // This cannot happen since there is always an ExceptionProcessor registered.
      exceptionProcessor = new DefaultProcessor();
    }
    ContentType requestedContentType;
    try {
      FormatOption formatOption = getFormatOption(request, uriInfo);
      requestedContentType = ContentNegotiator.doContentNegotiation(formatOption, request,
          getCustomContentTypeSupport(), RepresentationType.ERROR);
    } catch (ContentNegotiatorException e) {
      requestedContentType = ContentType.CT_JSON;
    }
    // int measurementError = debugger.startRuntimeMeasurement("ErrorProcessor", "processError");
    exceptionProcessor.processError(request, response, serverError, requestedContentType);
    // debugger.stopRuntimeMeasurement(measurementError);
    // debugger.stopRuntimeMeasurement(measurementHandle);
  }

  /**
   * Extract format option from either <code>uriInfo</code> (if not <code>NULL</code>)
   * or query from <code>request</code> (if not <code>NULL</code>).
   * If both options are <code>NULL</code>, <code>NULL</code> is returned.
   *
   * @param request request which is checked
   * @param uriInfo uriInfo which is checked
   * @return the evaluated format option or <code>NULL</code>.
   */
  private FormatOption getFormatOption(ODataRequest request, UriInfo uriInfo) {
    if(uriInfo == null) {
      String query = request.getRawQueryPath();
      if(query == null) {
        return null;
      }

      String formatOption = SystemQueryOptionKind.FORMAT.toString();
      int index = query.indexOf(formatOption);
      int endIndex = query.indexOf('&', index);
      if(endIndex == -1) {
        endIndex = query.length();
      }
      String format = "";
      if (index + formatOption.length() < endIndex) {
         format = query.substring(index + formatOption.length(), endIndex);
      }
      return new FormatOptionImpl().setFormat(format);
    }
    return uriInfo.getFormatOption();
  }

  private void validateODataVersion(ODataRequest request) throws ODataHandlerException {
    String odataVersion = request.getHeader(ODATA_VERSION);
   if (odataVersion != null && !ODataServiceVersion.isValidODataVersion(odataVersion)) {
      throw new ODataHandlerException("ODataVersion not supported: " + odataVersion,
          ODataHandlerException.MessageKeys.ODATA_VERSION_NOT_SUPPORTED, odataVersion);
    }
    
    String maxVersion = request.getHeader(ODATA_MAX_VERSION);
    if (maxVersion != null && !ODataServiceVersion.isValidMaxODataVersion(maxVersion)) {
        throw new ODataHandlerException("ODataVersion not supported: " + maxVersion,
            ODataHandlerException.MessageKeys.ODATA_VERSION_NOT_SUPPORTED, maxVersion);
      }
  }

  <T extends Processor> T selectProcessor(Class<T> cls) throws ODataHandlerException {
    for (Processor processor : processors) {
      if (cls.isAssignableFrom(processor.getClass())) {
        processor.init(odata, serviceMetadata);
        return cls.cast(processor);
      }
    }
    throw new ODataHandlerException("Processor: " + cls.getSimpleName() + " not registered.",
        ODataHandlerException.MessageKeys.PROCESSOR_NOT_IMPLEMENTED, cls.getSimpleName());
  }

  public void register(Processor processor) {
    processors.add(0, processor);
  }

  @Override
  public void register(OlingoExtension extension) {
    if(extension instanceof CustomContentTypeSupport) {
      customContentTypeSupport = (CustomContentTypeSupport) extension;
    } else if(extension instanceof CustomETagSupport) {
      customETagSupport = (CustomETagSupport) extension;
    } else {
      throw new ODataRuntimeException("Got not supported exception with class name " +
          extension.getClass().getSimpleName());
    }
  }

  public CustomContentTypeSupport getCustomContentTypeSupport() {
    return customContentTypeSupport;
  }

  public CustomETagSupport getCustomETagSupport() {
    return customETagSupport;
  }

/*
  public Exception getLastThrownException() {
    return lastThrownException;
  }


  public UriInfo getUriInfo() {
    return uriInfo;
  }
*/

}