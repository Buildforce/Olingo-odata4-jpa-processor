/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;

import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import org.junit.Test;

public class ODataHttpHandlerImplTest {

  @Test
  public void extractMethod() throws Exception {
    String[][] mm = {
        { "GET", null, null, "GET" },
        { "GET", "xxx", "yyy", "GET" },
        { "PUT", "xxx", "yyy", "PUT" },
        { "DELETE", "xxx", "yyy", "DELETE" },
        { "PATCH", "xxx", "yyy", "PATCH" },

        { "POST", null, null, "POST" },
        { "POST", null, "GET", "GET" },
        { "POST", null, "PATCH", "PATCH" },

        { "POST", "GET", null, "GET" },
        { "POST", "PATCH", null, "PATCH" },

        { "POST", "GET", "GET", "GET" },
        { "HEAD", null, null, "HEAD" }
    };

    for (String[] m : mm) {

      HttpServletRequest hr = mock(HttpServletRequest.class);

      when(hr.getMethod()).thenReturn(m[0]);
      when(hr.getHeader("X-HTTP-Method")).thenReturn(m[1]);
      when(hr.getHeader("X-HTTP-Method-Override")).thenReturn(m[2]);

      assertEquals(HttpMethod.valueOf(m[3]), ODataHttpHandlerImpl.extractMethod(hr));
    }
  }

  @Test
  public void extractMethodFail() {
    String[][] mm = {
        { "POST", "bla", null },
        { "POST", "PUT", "PATCH" },
        { "OPTIONS", null, null }
    };

    for (String[] m : mm) {

      HttpServletRequest hr = mock(HttpServletRequest.class);

      when(hr.getMethod()).thenReturn(m[0]);
      when(hr.getHeader("X-HTTP-Method")).thenReturn(m[1]);
      when(hr.getHeader("X-HTTP-Method-Override")).thenReturn(m[2]);

      try {
        ODataHttpHandlerImpl.extractMethod(hr);
        fail();
      } catch (ODataLibraryException e) {
        // expected
      }
    }
  }

  @Test
  public void extractUri() {

    //@formatter:off (Eclipse formatter)
    //CHECKSTYLE:OFF (Maven checkstyle)
    String [][] uris = {
        /* 0: host                    1: cp         2: sp       3: sr          4: od       5: qp        6: spl  */
        {  "http://localhost",          "",           "",         "",          "",          "",         "0"},
        {  "http://localhost",          "",           "",         "",          "/",         "",         "0"},
        {  "http://localhost",          "",           "",         "",          "/od",       "",         "0"},
        {  "http://localhost",          "",           "",         "",          "/od/",      "",         "0"},

        {  "http://localhost",          "/cp",        "",         "",          "",          "",         "0"},
        {  "http://localhost",          "/cp",        "",         "",          "/",         "",         "0"},
        {  "http://localhost",          "/cp",        "",         "",          "/od",       "",         "0"},
        {  "http://localhost",          "",           "/sp",      "",          "",          "",         "0"},
        {  "http://localhost",          "",           "/sp",      "",          "/",         "",         "0"},
        {  "http://localhost",          "",           "/sp",      "",          "/od",       "",         "0"},
/*
        {  "http://localhost",          "",           "",         "/sr",       "",          "",         "1"},
        {  "http://localhost",          "",           "",         "/sr",       "/",         "",         "1"},
        {  "http://localhost",          "",           "",         "/sr",       "/od",       "",         "1"},
        {  "http://localhost",          "",           "",         "/sr/sr",    "",          "",         "2"},
        {  "http://localhost",          "",           "",         "/sr/sr",    "/",         "",         "2"},
        {  "http://localhost",          "",           "",         "/sr/sr",    "/od",       "",         "2"},
*/

        {  "http://localhost",          "/cp",        "/sp",      "",          "",          "",         "0"},
        {  "http://localhost",          "/cp",        "/sp",      "",          "/",         "",         "0"},
        {  "http://localhost",          "/cp",        "/sp",      "",          "/od",       "",         "0"},
/*
        {  "http://localhost",          "/cp",        "",         "/sr",       "/",         "",         "1"},
        {  "http://localhost",          "/cp",        "",         "/sr",       "/od",       "",         "1"},
        {  "http://localhost",          "",           "/sp",      "/sr",       "",          "",         "1"},
        {  "http://localhost",          "",           "/sp",      "/sr",       "/",         "",         "1"},
        {  "http://localhost",          "",           "/sp",      "/sr",       "/od",       "",         "1"},
        {  "http://localhost",          "/cp",        "/sp",      "/sr",       "",          "",         "1"},
        {  "http://localhost",          "/cp",        "/sp",      "/sr",       "/",         "",         "1"},
        {  "http://localhost",          "/cp",        "/sp",      "/sr",       "/od",       "",         "1"},
*/

        {  "http://localhost",          "",           "",         "",          "",          "qp",       "0"},
        {  "http://localhost",          "",           "",         "",          "/",         "qp",       "0"}/*,
        {  "http://localhost",          "/cp",        "/sp",      "/sr",       "/od",       "qp",       "1"},

        {  "http://localhost:8080",     "/c%20p",     "/s%20p",   "/s%20r",    "/o%20d",    "p+q",      "1"},*/
    };
    //@formatter:on
    // CHECKSTYLE:on

    for (String[] p : uris) {
      HttpServletRequest hr = mock(HttpServletRequest.class);

      String requestUrl = p[0] + p[1] + p[2] + p[3] + p[4];
      String requestUri = p[1] + p[2] + p[3] + p[4];
      String queryString = p[5].isEmpty() ? null : p[5];

      when(hr.getRequestURL()).thenReturn(new StringBuffer(requestUrl));
      when(hr.getRequestURI()).thenReturn(requestUri);
      when(hr.getQueryString()).thenReturn(queryString);
      when(hr.getContextPath()).thenReturn(p[1]);
      when(hr.getServletPath()).thenReturn(p[2]);

      ODataRequest odr = new ODataRequest();
      ODataHttpHandlerImpl.fillUriInformation(odr, hr/*, Integer.parseInt(p[6])*/);

      String rawBaseUri = p[0] + p[1] + p[2] + p[3];
      String rawODataPath = p[4];
      String rawQueryPath = "".equals(p[5]) ? null : p[5];
      String rawRequestUri = requestUrl + (queryString == null ? "" : "?" + queryString);
      String rawServiceResolutionUri = "".equals(p[3]) ? null : p[3];

      assertEquals(rawBaseUri, odr.getRawBaseUri());
      assertEquals(rawODataPath, odr.getRawODataPath());
      assertEquals(rawQueryPath, odr.getRawQueryPath());
      assertEquals(rawRequestUri, odr.getRawRequestUri());
      assertEquals(rawServiceResolutionUri, odr.getRawServiceResolutionUri());
    }
  }
  
  @Test
  public void extractUriForController() {

    //@formatter:off (Eclipse formatter)
    //CHECKSTYLE:OFF (Maven checkstyle)
    String [][] uris = {
        /* 0: host                    1: cp         2: sp       3: sr          4: od       5: qp        6: spl  */
        
        {  "http://localhost",          "",           "/sp",      "",          "",          "",         "0"},
        {  "http://localhost",          "",           "/sp",      "",          "/",         "",         "0"},
        {  "http://localhost",          "",           "/sp",      "",          "/od",       "",         "0"},
       
        {  "http://localhost",          "/cp",        "/sp",      "",          "",          "",         "0"},
        {  "http://localhost",          "/cp",        "/sp",      "",          "/",         "",         "0"},
        {  "http://localhost",          "/cp",        "/sp",      "",          "/od",       "",         "0"}/*,
       
        {  "http://localhost",          "/cp",        "/sp",      "/sr",       "",          "",         "1"},
        {  "http://localhost",          "/cp",        "/sp",      "/sr",       "/",         "",         "1"},
        {  "http://localhost",          "/cp",        "/sp",      "/sr",       "/od",       "",         "1"},

     
        {  "http://localhost",          "/cp",        "/sp",      "/sr",       "/od",       "qp",       "1"},

        {  "http://localhost:8080",     "/c%20p",     "/s%20p",   "/s%20r",    "/o%20d",    "p+q",      "1"},*/
    };
    //@formatter:on
    // CHECKSTYLE:on

    for (String[] p : uris) {
      HttpServletRequest hr = mock(HttpServletRequest.class);

      String requestUrl = p[0] + p[1] + p[2] + p[3] + p[4];
      String requestUri = p[1] + p[2] + p[3] + p[4];
      String queryString = p[5].isEmpty() ? null : p[5];

      when(hr.getRequestURL()).thenReturn(new StringBuffer(requestUrl));
      when(hr.getRequestURI()).thenReturn(requestUri);
      when(hr.getQueryString()).thenReturn(queryString);
      when(hr.getContextPath()).thenReturn(p[1]);
      when(hr.getServletPath()).thenReturn(p[2]);

      ODataRequest odr = new ODataRequest();

      String rawBaseUri = p[0] + p[1] + p[2] + p[3];
      String rawODataPath = p[4];
      String rawQueryPath = "".equals(p[5]) ? null : p[5];
      String rawRequestUri = requestUrl + (queryString == null ? "" : "?" + queryString);
      String rawServiceResolutionUri = ("0".equals(p[6])) ? p[2] : p[3];

      when(hr.getAttribute("requestMapping")).thenReturn(p[2]);
      ODataHttpHandlerImpl.fillUriInformation(odr, hr);
      assertEquals(rawBaseUri, odr.getRawBaseUri());
      assertEquals(rawODataPath, odr.getRawODataPath());
      assertEquals(rawQueryPath, odr.getRawQueryPath());
      assertEquals(rawRequestUri, odr.getRawRequestUri());
      assertEquals(rawServiceResolutionUri, odr.getRawServiceResolutionUri());
    
    }
  }

}