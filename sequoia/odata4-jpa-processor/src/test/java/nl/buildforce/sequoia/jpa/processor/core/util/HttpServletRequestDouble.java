package nl.buildforce.sequoia.jpa.processor.core.util;

import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.server.api.debug.DebugSupport;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import java.io.BufferedReader;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static nl.buildforce.sequoia.jpa.processor.core.api.JPAODataGetHandler.REQUESTMAPPING_ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.fail;

public class HttpServletRequestDouble implements HttpServletRequest {
    private final HttpRequestHeaderDouble reqHeader;
    private final String queryString;
    private final StringBuffer url;
    private final StringBuffer input;
    private final Map<String, Object> attributes;
    private String debugFormat;

/*
    public HttpServletRequestDouble(final String uri, final StringBuffer body) {
        this(uri, body, null);
    }
*/

  public HttpServletRequestDouble(
            final String uri,
            final StringBuffer body,
            final Map<String, List<String>> headers) {
        reqHeader = new HttpRequestHeaderDouble();
        String[] uriParts = uri.split("\\?");
        this.url = new StringBuffer(uriParts[0]);
        queryString = (uriParts.length == 2) ? uriParts[1] : null;
        this.input = body;
        if (uri.contains("$batch")) {
            reqHeader.setBatchRequest();
        }
        reqHeader.setHeaders(headers);
        attributes = new HashMap<>();
    }

    @Override
    public Collection<Part> getParts() {
        fail();
        return null;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        fail();
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) {
        return false;
    }

    @Override
    public void login(String s, String s1) {

    }

    @Override
    public void logout() {

    }

    public Part getPart(String var1) {
        fail();
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) {
        return null;
    }

    @Override
    public Object getAttribute(final String name) {
        if (!REQUESTMAPPING_ATTRIBUTE.equals(name))
            fail();
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        fail();
        return null;
    }

    @Override
    public void setCharacterEncoding(final String env) {
        fail();

    }

    @Override
    public int getContentLength() {
        fail();
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        fail();
        return null;
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStreamDouble(input);
    }

    @Override
    public String getParameter(final String name) {
        if (DebugSupport.ODATA_DEBUG_QUERY_PARAMETER.equals(name))
            return debugFormat;
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        fail();
        return null;
    }

    @Override
    public String[] getParameterValues(final String name) {
        fail();
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        fail();
        return null;
    }

    @Override
    public String getProtocol() {
        return "HTTP/1.1";
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() {
        fail();
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(final String name, final Object o) {
        attributes.put(name, o);
    }

    @Override
    public void removeAttribute(final String name) {
        fail();

    }

    @Override
    public Locale getLocale() {
        fail();
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        fail();
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(final String path) {
        fail();
        return null;
    }

    @Override
    public String getRealPath(final String path) {
        fail();
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        fail();
        return null;
    }

    @Override
    public long getDateHeader(final String name) {
        fail();
        return 0;
    }

    @Override
    public String getHeader(final String name) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaders(final String name) {

        return reqHeader.get(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return reqHeader.getEnumerator();
    }

    @Override
    public int getIntHeader(final String name) {
        fail();
        return 0;
    }

    @Override
    public String getMethod() {
        if (url.toString().contains("$batch"))
            return HttpMethod.POST.toString();
        else
            return HttpMethod.GET.toString();
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        fail();
        return null;
    }

    @Override
    public String getQueryString() {
        // $orderby=Roles/$count%20desc,Address/Region%20asc&$select=ID,Name1
        return queryString;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(final String role) {
        fail();
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        fail();
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        fail();
        return null;
    }

    @Override
    public String getRequestURI() {
        fail();
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        // http://localhost:8090/BuPa/BuPa.svc/AdministrativeDivisions(DivisionCode='BE252',CodeID='3',CodePublisher='NUTS')/Parent/Parent
        // http://localhost:8090/BuPa/BuPa.svc/Organizations
        return url;
    }

    @Override
    public String getServletPath() {
        return "/Olingo.svc";
    }

    @Override
    public HttpSession getSession(final boolean create) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        fail();
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        fail();
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        fail();
        return false;
    }

}