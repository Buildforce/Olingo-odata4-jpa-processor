/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.buildforce.olingo.server.core.debug;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpHeader;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.debug.DebugInformation;
import nl.buildforce.olingo.server.api.debug.DebugSupport;
import nl.buildforce.olingo.server.api.debug.RuntimeMeasurement;
import nl.buildforce.olingo.server.api.uri.UriInfo;

public class ServerCoreDebugger {

  private static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");
  private final List<RuntimeMeasurement> runtimeInformation = new ArrayList<>();
  private final OData odata;

  private boolean isDebugMode = false;
  private DebugSupport debugSupport;
  private String debugFormat;

  public ServerCoreDebugger(OData odata) {
    this.odata = odata;
  }

  public void resolveDebugMode(HttpServletRequest request) {
    if (debugSupport != null) {
      // Should we read the parameter from the servlet here and ignore multiple parameters?
      debugFormat = request.getParameter(DebugSupport.ODATA_DEBUG_QUERY_PARAMETER);
      if (debugFormat != null) {
        debugSupport.init(odata);
        isDebugMode = debugSupport.isUserAuthorized();
      }
    }
  }
  
  public ODataResponse createDebugResponse(ODataRequest request, ODataResponse response,
                                           Exception exception, UriInfo uriInfo, Map<String, String> serverEnvironmentVariables) {
    // Failsafe so we do not generate unauthorized debug messages
    if (!isDebugMode) {
      return response;
    }

    try {
      DebugInformation debugInfo =
          createDebugInformation(request, response, exception, uriInfo, serverEnvironmentVariables);

      return debugSupport.createDebugResponse(debugFormat, debugInfo);
    } catch (Exception e) {
      return createFailResponse();
    }
  }

  private ODataResponse createFailResponse() {
    ODataResponse odResponse = new ODataResponse();
    odResponse.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
    odResponse.setHeader(HttpHeader.CONTENT_TYPE, ContentType.TEXT_PLAIN.toContentTypeString());
    InputStream content = new ByteArrayInputStream("ODataLibrary: Could not assemble debug response."
        .getBytes(DEFAULT_ENCODING));
    odResponse.setContent(content);
    return odResponse;
  }

  private DebugInformation createDebugInformation(ODataRequest request, ODataResponse response,
                                                  Exception exception, UriInfo uriInfo, Map<String, String> serverEnvironmentVariables) {
    DebugInformation debugInfo = new DebugInformation();
    debugInfo.setRequest(request);
    debugInfo.setApplicationResponse(response);

    debugInfo.setException(exception);

    debugInfo.setServerEnvironmentVariables(serverEnvironmentVariables);

    debugInfo.setUriInfo(uriInfo);

    debugInfo.setRuntimeInformation(runtimeInformation);
    return debugInfo;
  }

  public int startRuntimeMeasurement(String className, String methodName) {
    if (isDebugMode) {
      int handleId = runtimeInformation.size();

      RuntimeMeasurement measurement = new RuntimeMeasurement();
      measurement.setTimeStarted(System.nanoTime());
      measurement.setClassName(className);
      measurement.setMethodName(methodName);

      runtimeInformation.add(measurement);

      return handleId;
    } else {
      return 0;
    }
  }

  public void stopRuntimeMeasurement(int handle) {
    if (isDebugMode && handle < runtimeInformation.size()) {
      RuntimeMeasurement runtimeMeasurement = runtimeInformation.get(handle);
      if (runtimeMeasurement != null) {
        runtimeMeasurement.setTimeStopped(System.nanoTime());
      }
    }
  }

  public void setDebugSupportProcessor(DebugSupport debugSupport) {
    this.debugSupport = debugSupport;
  }

  public boolean isDebugMode() {
    return isDebugMode;
  }
}