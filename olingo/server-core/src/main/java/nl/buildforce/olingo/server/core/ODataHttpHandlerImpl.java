/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import nl.buildforce.olingo.commons.api.ex.ODataRuntimeException;
import nl.buildforce.olingo.commons.api.http.HttpHeader;
import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataContent;
import nl.buildforce.olingo.server.api.ODataHttpHandler;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ODataServerError;
import nl.buildforce.olingo.server.api.OlingoExtension;
import nl.buildforce.olingo.server.api.ServiceMetadata;
// import nl.buildforce.olingo.server.api.debug.DebugSupport;
import nl.buildforce.olingo.server.api.deserializer.DeserializerException;
import nl.buildforce.olingo.server.api.etag.CustomETagSupport;
import nl.buildforce.olingo.server.api.processor.Processor;
import nl.buildforce.olingo.server.api.serializer.CustomContentTypeSupport;
// import nl.buildforce.olingo.server.core.debug.ServerCoreDebugger;

public class ODataHttpHandlerImpl implements ODataHttpHandler {

  public static final int COPY_BUFFER_SIZE = 8192;
  private static final String REQUESTMAPPING = "requestMapping";

  private final ODataHandlerImpl handler;
  // private final ServerCoreDebugger debugger;

  private int split;

  // Used by AkkaHttp-Sequoia-Greentrak03
  public ODataHttpHandlerImpl(OData odata, ServiceMetadata serviceMetadata) {
    // debugger = new ServerCoreDebugger(odata);
    handler = new ODataHandlerImpl(odata, serviceMetadata/*, debugger*/);
  }

  @Override
  public ODataResponse process(ODataRequest request) {
    return handler.process(request);
  }

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response) {
    ODataRequest odRequest = new ODataRequest();
    ODataResponse odResponse;
    // debugger.resolveDebugMode(request);

    // int processMethodHandle = debugger.startRuntimeMeasurement("ODataHttpHandlerImpl", "process");
    try {
      fillODataRequest(odRequest, request, split);

      odResponse = process(odRequest);
      // ALL future methods after process must not throw exceptions!
    } catch (Exception e) {
      // exception = e;
      odResponse = handleException(odRequest, e);
    }
    // debugger.stopRuntimeMeasurement(processMethodHandle);

/*    if (debugger.isDebugMode()) {
      Map<String, String> serverEnvironmentVariables = createEnvironmentVariablesMap(request);
      if (exception == null) {
        // This is to ensure that we have access to the thrown OData Exception
        exception = handler.getLastThrownException();
      }
      odResponse =
          debugger.createDebugResponse(odRequest, odResponse, exception, handler.getUriInfo(),
              serverEnvironmentVariables);
    }*/

    convertToHttp(response, odResponse);
  }

  private Map<String, String> createEnvironmentVariablesMap(HttpServletRequest request) {
    Map<String, String> environment = new LinkedHashMap<>();
    environment.put("authType", request.getAuthType());
    environment.put("localAddr", request.getLocalAddr());
    environment.put("localName", request.getLocalName());
    environment.put("localPort", getIntAsString(request.getLocalPort()));
    environment.put("pathInfo", request.getPathInfo());
    environment.put("pathTranslated", request.getPathTranslated());
    environment.put("remoteAddr", request.getRemoteAddr());
    environment.put("remoteHost", request.getRemoteHost());
    environment.put("remotePort", getIntAsString(request.getRemotePort()));
    environment.put("remoteUser", request.getRemoteUser());
    environment.put("scheme", request.getScheme());
    environment.put("serverName", request.getServerName());
    environment.put("serverPort", getIntAsString(request.getServerPort()));
    environment.put("servletPath", request.getServletPath());
    return environment;
  }
  
  private String getIntAsString(int number) {
    return number == 0 ? "unknown" : Integer.toString(number);
  }

  @Override
  public void setSplit(int split) {
    this.split = split;
  }

  private ODataResponse handleException(ODataRequest odRequest, Exception e) {
    ODataResponse resp = new ODataResponse();
    ODataServerError serverError;
    if (e instanceof ODataHandlerException) {
      serverError = ODataExceptionHelper.createServerErrorObject((ODataHandlerException) e, null);
    } else if (e instanceof ODataLibraryException) {
      serverError = ODataExceptionHelper.createServerErrorObject((ODataLibraryException) e, null);
    } else {
      serverError = ODataExceptionHelper.createServerErrorObject(e);
    }
    handler.handleException(odRequest, resp, serverError, e);
    return resp;
  }

  static void convertToHttp(HttpServletResponse response, ODataResponse odResponse) {
    response.setStatus(odResponse.getStatusCode());

    for (Entry<String, List<String>> entry : odResponse.getAllHeaders().entrySet()) {
      for (String headerValue : entry.getValue()) {
        response.addHeader(entry.getKey(), headerValue);
      }
    }

    if (odResponse.getContent() != null) {
      copyContent(odResponse.getContent(), response);
    } else if (odResponse.getODataContent() != null) {
      writeContent(odResponse, response);
    }
  }
  
  static void writeContent(ODataResponse odataResponse, HttpServletResponse servletResponse) {
    try {
      ODataContent res = odataResponse.getODataContent();
      res.write(Channels.newChannel(servletResponse.getOutputStream()));
    } catch (IOException e) {
      throw new ODataRuntimeException("Error on reading request content", e);
    }
  }

  static void copyContent(InputStream inputStream, HttpServletResponse servletResponse) {
    copyContent(Channels.newChannel(inputStream), servletResponse);
  }

  static void copyContent(ReadableByteChannel input, HttpServletResponse servletResponse) {
    try (WritableByteChannel output = Channels.newChannel(servletResponse.getOutputStream())) {
      ByteBuffer inBuffer = ByteBuffer.allocate(COPY_BUFFER_SIZE);
      while (input.read(inBuffer) > 0) {
        inBuffer.flip();
        output.write(inBuffer);
        inBuffer.clear();
      }
    } catch (IOException e) {
      throw new ODataRuntimeException("Error on reading request content", e);
    } finally {
      closeStream(input);
    }
  }
  
  private static void closeStream(Channel closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        // ignore
      }
    }
  }
  
  private ODataRequest fillODataRequest(ODataRequest odRequest, HttpServletRequest httpRequest,
                                        int split) throws ODataLibraryException {
    // int requestHandle = debugger.startRuntimeMeasurement("ODataHttpHandlerImpl", "fillODataRequest");
    try {
      odRequest.setBody(httpRequest.getInputStream());
      odRequest.setProtocol(httpRequest.getProtocol());
      odRequest.setMethod(extractMethod(httpRequest));
      // int innerHandle = debugger.startRuntimeMeasurement("ODataHttpHandlerImpl", "copyHeaders");
      copyHeaders(odRequest, httpRequest);
      // debugger.stopRuntimeMeasurement(innerHandle);
      // innerHandle = debugger.startRuntimeMeasurement("ODataHttpHandlerImpl", "fillUriInformation");
      fillUriInformation(odRequest, httpRequest, split);
      // debugger.stopRuntimeMeasurement(innerHandle);

      return odRequest;
    } catch (IOException e) {
      throw new DeserializerException("An I/O exception occurred.", e,
          DeserializerException.MessageKeys.IO_EXCEPTION);
    }
  }
  
  static HttpMethod extractMethod(HttpServletRequest httpRequest) throws ODataLibraryException {
	    HttpMethod httpRequestMethod;
	    try {
	      httpRequestMethod = HttpMethod.valueOf(httpRequest.getMethod());
	    } catch (IllegalArgumentException e) {
	      throw new ODataHandlerException("HTTP method not allowed" + httpRequest.getMethod(), e,
	          ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, httpRequest.getMethod());
	    }
	    try {
	      if (httpRequestMethod == HttpMethod.POST) {
	        String xHttpMethod = httpRequest.getHeader(HttpHeader.X_HTTP_METHOD);
	        String xHttpMethodOverride = httpRequest.getHeader(HttpHeader.X_HTTP_METHOD_OVERRIDE);

	        if (xHttpMethod == null && xHttpMethodOverride == null) {
	          return httpRequestMethod;
	        } else if (xHttpMethod == null) {
	          return HttpMethod.valueOf(xHttpMethodOverride);
	        } else if (xHttpMethodOverride == null) {
	          return HttpMethod.valueOf(xHttpMethod);
	        } else {
	          if (!xHttpMethod.equalsIgnoreCase(xHttpMethodOverride)) {
	            throw new ODataHandlerException("Ambiguous X-HTTP-Methods",
	                ODataHandlerException.MessageKeys.AMBIGUOUS_XHTTP_METHOD, xHttpMethod, xHttpMethodOverride);
	          }
	          return HttpMethod.valueOf(xHttpMethod);
	        }
	      } else {
	        return httpRequestMethod;
	      }
	    } catch (IllegalArgumentException e) {
	      throw new ODataHandlerException("Invalid HTTP method" + httpRequest.getMethod(), e,
	          ODataHandlerException.MessageKeys.INVALID_HTTP_METHOD, httpRequest.getMethod());
	    }
	  }
  
  static void fillUriInformation(ODataRequest odRequest,
                                 HttpServletRequest httpRequest, int split) {
    String rawRequestUri = httpRequest.getRequestURL().toString();
    
    String rawServiceResolutionUri = null;
    String rawODataPath;
    //Application need to set the request mapping attribute if the request is coming from a spring based application
    if(httpRequest.getAttribute(REQUESTMAPPING)!=null){
      String requestMapping = httpRequest.getAttribute(REQUESTMAPPING).toString();
      rawServiceResolutionUri = requestMapping;
      int beginIndex = rawRequestUri.indexOf(requestMapping) + requestMapping.length();
      rawODataPath = rawRequestUri.substring(beginIndex);
    }else if(!"".equals(httpRequest.getServletPath())) {
      int beginIndex = rawRequestUri.indexOf(httpRequest.getServletPath()) + 
    		  httpRequest.getServletPath().length();
      rawODataPath = rawRequestUri.substring(beginIndex);
    } else if (!"".equals(httpRequest.getContextPath())) {
      int beginIndex = rawRequestUri.indexOf(httpRequest.getContextPath()) + 
    		  httpRequest.getContextPath().length();
      rawODataPath = rawRequestUri.substring(beginIndex);
    } else {
      rawODataPath = httpRequest.getRequestURI();
    }

    if (split > 0) {
      rawServiceResolutionUri = rawODataPath;
      for (int i = 0; i < split; i++) {
        int index = rawODataPath.indexOf('/', 1);
        if (-1 == index) {
          rawODataPath = "";
          break;
        } else {
          rawODataPath = rawODataPath.substring(index);
        }
      }
      int end = rawServiceResolutionUri.length() - rawODataPath.length();
      rawServiceResolutionUri = rawServiceResolutionUri.substring(0, end);
    }

    String rawBaseUri = rawRequestUri.substring(0, rawRequestUri.length() - rawODataPath.length());

    odRequest.setRawQueryPath(httpRequest.getQueryString());
    odRequest.setRawRequestUri(rawRequestUri
        + (httpRequest.getQueryString() == null ? "" : "?" + httpRequest.getQueryString()));
    odRequest.setRawODataPath(rawODataPath);
    odRequest.setRawBaseUri(rawBaseUri);
    odRequest.setRawServiceResolutionUri(rawServiceResolutionUri);
  }

  static void copyHeaders(ODataRequest odRequest, HttpServletRequest req) {
	  for (Enumeration<?> headerNames = req.getHeaderNames(); headerNames.hasMoreElements();) {
	      String headerName = (String) headerNames.nextElement();
	      // getHeaders() says it returns an Enumeration of String.
          List<String> headerValues = Collections.list(req.getHeaders(headerName));
	      odRequest.addHeader(headerName, headerValues);
	    }
  }

  @Override
  public void register(Processor processor) {
    handler.register(processor);
  }

  @Override
  public void register(OlingoExtension extension) {
    handler.register(extension);
  }

  @Override
  public void register(CustomContentTypeSupport customContentTypeSupport) {
    handler.register(customContentTypeSupport);
  }

  @Override
  public void register(CustomETagSupport customConcurrencyControlSupport) {
    handler.register(customConcurrencyControlSupport);
  }

  /*@Override
  public void register(DebugSupport debugSupport) {
    debugger.setDebugSupportProcessor(debugSupport);
  }*/

}