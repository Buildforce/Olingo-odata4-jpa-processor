package nl.buildforce.sequoia.jpa.processor.core.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.fail;

public class HttpServletResponseDouble implements HttpServletResponse {

    private final ServletOutputStream outputStream = new OutPutStream();
    private int setStatus;

    @Override
    public String getHeader(String var1) {
        fail();
        return null;
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        fail();
        return null;
    }

    @Override
    public void setCharacterEncoding(String charset) {
        fail();

    }

    @Override
    public String getContentType() {
        fail();
        return null;
    }

    @Override
    public void setContentType(String type) {
        fail();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return this.outputStream;
    }

    @Override
    public PrintWriter getWriter() {
        fail();
        return null;
    }

    @Override
    public void setContentLength(int len) {
        fail();

    }

    @Override
    public void setContentLengthLong(long len) {
    }

    @Override
    public int getBufferSize() {
        return ((OutPutStream) this.outputStream).getSize();
    }

    @Override
    public void setBufferSize(int size) {
        fail();
    }

    @Override
    public void flushBuffer() {
        fail();
    }

    @Override
    public void resetBuffer() {
        fail();
    }

/*
  @Override
  public Enumeration<?> getHeaderNames() {
    fail();
  }
*/

    @Override
    public boolean isCommitted() {
        fail();
        return false;
    }

    @Override
    public void reset() {
        fail();
    }

    @Override
    public Locale getLocale() {
        fail();
        return null;
    }

    @Override
    public void setLocale(Locale loc) {
        fail();
    }

    @Override
    public void addCookie(Cookie cookie) {
        fail();

    }

    @Override
    public boolean containsHeader(String name) {
        fail();
        return false;
    }

    @Override
    public String encodeURL(String url) {
        fail();
        return null;
    }

    @Override
    public String encodeRedirectURL(String url) {
        fail();
        return null;
    }

    @Override
    public String encodeUrl(String url) {
        fail();
        return null;
    }

    @Override
    public String encodeRedirectUrl(String url) {
        fail();
        return null;
    }

    @Override
    public void sendError(int sc, String msg) {
        fail();

    }

    @Override
    public void sendError(int sc) {
        fail();

    }

    @Override
    public void sendRedirect(String location) {
        fail();

    }

    @Override
    public void setDateHeader(String name, long date) {
        fail();

    }

    @Override
    public void addDateHeader(String name, long date) {
        fail();

    }

    @Override
    public void setHeader(String name, String value) {
        fail();

    }

    @Override
    public void addHeader(String name, String value) {
    }

    @Override
    public void setIntHeader(String name, int value) {
        fail();

    }

    @Override
    public void addIntHeader(String name, int value) {
        fail();

    }

    @Override
    public void setStatus(int sc, String sm) {
        fail();

    }

    public int getStatus() {
        return setStatus;
    }

    @Override
    public void setStatus(int sc) {
        this.setStatus = sc;
    }

    public InputStream getInputStream() {
        return new ResultStream((OutPutStream) this.outputStream);
    }

    static class OutPutStream extends ServletOutputStream {
        final List<Integer> buffer = new ArrayList<>();

        @Override
        public void write(int b) {
            buffer.add(b);
        }

        public Iterator<Integer> getBuffer() {
            return buffer.iterator();
        }

        public int getSize() {
            return buffer.size();
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }

    static class ResultStream extends InputStream {
        private final Iterator<Integer> bufferExcess;

        public ResultStream(OutPutStream buffer) {
            super();
            this.bufferExcess = buffer.getBuffer();
        }

        @Override
        public int read() {
            if (bufferExcess.hasNext()) return bufferExcess.next();
            return -1;
        }
    }

}