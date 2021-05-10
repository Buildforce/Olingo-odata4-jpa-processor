package nl.buildforce.sequoia.processor.core.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

import static com.google.common.net.HttpHeaders.ACCEPT;
import static com.google.common.net.HttpHeaders.ACCEPT_ENCODING;
import static com.google.common.net.HttpHeaders.ACCEPT_LANGUAGE;
import static com.google.common.net.HttpHeaders.CACHE_CONTROL;
import static com.google.common.net.HttpHeaders.CONNECTION;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.HttpHeaders.HOST;

public class HttpRequestHeaderDouble {
  private final HashMap<String, List<String>> headers = new HashMap<>();

  private void addHeader(String name, String value) {
    headers.put(name, new ArrayList<>() {{ add(value);}});
  }

  public HttpRequestHeaderDouble() {
    addHeader(ACCEPT,"text/html,application/json,application/xml;q=0.9,image/webp,*/*;q=0.8");
    addHeader(ACCEPT_ENCODING,"gzip, deflate, sdch");
    addHeader(ACCEPT_LANGUAGE, "de-DE,de;q=0.8,en-US;q=0.6,en;q=0.4");
    addHeader(CACHE_CONTROL, "max-age=0");
    addHeader(CONNECTION, "keep-alive");
    addHeader(HOST,"localhost:8090");
  }

  public Enumeration<String> get(String headerName) {
    return new headerItem(headers.get(headerName));
  }

  public Enumeration<String> getEnumerator() {
    return new HeaderEnumerator(headers.keySet());
  }

  public void setBatchRequest() {
    List<String> headerValue = new ArrayList<>();
    headerValue.add("multipart/mixed;boundary=abc123");
    headers.put(CONTENT_TYPE, headerValue);
  }

  public void setHeaders(Map<String, List<String>> additionalHeaders) {
    if (additionalHeaders != null)
      for (Entry<String, List<String>> header : additionalHeaders.entrySet())
      headers.put(header.getKey(), header.getValue());
  }

  static class HeaderEnumerator implements Enumeration<String> {

    private final Iterator<String> keys;

    HeaderEnumerator(Set<String> keySet) {
      keys = keySet.iterator();
    }

    @Override
    public boolean hasMoreElements() {
      return keys.hasNext();
    }

    @Override
    public String nextElement() {
      return keys.next();
    }
  }

  static class headerItem implements Enumeration<String> {
    private final Iterator<String> keys;

    public headerItem(List<String> header) {
      keys = header.iterator();
    }

    @Override
    public boolean hasMoreElements() {
      return keys.hasNext();
    }

    @Override
    public String nextElement() {
      return keys.next();
    }
  }

}