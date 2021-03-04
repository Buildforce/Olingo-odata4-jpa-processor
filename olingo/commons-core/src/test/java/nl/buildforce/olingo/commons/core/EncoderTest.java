/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
 */
package nl.buildforce.olingo.commons.core;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for percent-encoding.
 *
 */
public class EncoderTest {

  private final static String RFC3986_UNRESERVED = "-._~"; // + ALPHA + DIGIT
  private final static String RFC3986_GEN_DELIMS = ":/?#[]@";
  private final static String RFC3986_SUB_DELIMS = "!$&'()*+,;=";
  private final static String RFC3986_RESERVED = RFC3986_GEN_DELIMS + RFC3986_SUB_DELIMS;

  @Test
  public void asciiCharacters() {
    final String s = "azAZ019";
    assertEquals(s, UrlEncoder.encode(s));
    assertEquals(s, UrlEncoder.encode(s));
  }

  @Test
  public void asciiControl() {
    assertEquals("%08%09%0A%0D", UrlEncoder.encode("\b\t\n\r"));
  }

  @Test
  public void unsafe() {
    assertEquals("%3C%3E%25%26", UrlEncoder.encode("<>%&"));
    assertEquals("%22%5C%60%7B%7D%7C", UrlEncoder.encode("\"\\`{}|"));
  }

  @Test
  public void rfc3986Unreserved() {
    assertEquals(RFC3986_UNRESERVED.replaceAll("~","%7E"), UrlEncoder.encode(RFC3986_UNRESERVED));
  }

  @Test
  public void rfc3986GenDelims() {
    assertEquals("%3A%2F%3F%23%5B%5D%40", UrlEncoder.encode(RFC3986_GEN_DELIMS));
  }

  @Test
  public void rfc3986SubDelims() {
    assertEquals("%21%24%26'%28%29*%2B%2C%3B%3D", UrlEncoder.encode(RFC3986_SUB_DELIMS));
    //                              %27      %2A
  }

  @Test
  public void rfc3986Reserved() {
    assertEquals("%3A%2F%3F%23%5B%5D%40%21%24%26'%28%29*%2B%2C%3B%3D", UrlEncoder.encode(RFC3986_RESERVED));    //                                          '
    //                                          '
  }

  @Test
  public void unicodeCharacters() {
    assertEquals("%E2%82%AC", UrlEncoder.encode("€"));
    assertEquals("%EF%B7%BC", UrlEncoder.encode("\uFDFC")); // RIAL SIGN
  }

  @Test
  public void charactersOutsideBmp() {
    // Unicode characters outside the Basic Multilingual Plane are stored
    // in a Java String in two surrogate characters.
    String s = String.valueOf(Character.toChars(0x1F603));
    assertEquals("%F0%9F%98%83", UrlEncoder.encode(s));
  }

  @Test
  public void uriDecoding() throws URISyntaxException {
    String decodedValue = RFC3986_UNRESERVED + RFC3986_RESERVED + "0..1..a..z..A..Z..@"
            + "\u2323\uFDFC" + String.valueOf(Character.toChars(0x1F603));

    String encodedPath = UrlEncoder.encode(decodedValue) + "/" + UrlEncoder.encode(decodedValue);
    String encodedQuery = UrlEncoder.encode(decodedValue);
    URI uri = new URI("http://host:80/" + encodedPath + "?" + encodedQuery + "=" + encodedQuery);

    assertEquals(uri.getPath(), "/" + decodedValue + "/" + decodedValue);
    assertEquals(uri.getQuery(), decodedValue + "=" + decodedValue);

    assertEquals(uri.getRawPath(), "/" + encodedPath);
    assertEquals(uri.getRawQuery(), encodedQuery + "=" + encodedQuery);
  }

}