/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class ODataRequestTest {

  @Test
  public void testHeader() {
    ODataRequest r = new ODataRequest();

    r.addHeader("aa", Collections.singletonList("cc"));

    assertEquals("cc", r.getHeaders("aa").get(0));
    assertEquals("cc", r.getHeaders("aA").get(0));
    assertEquals("cc", r.getHeaders("AA").get(0));

    assertEquals("cc", r.getHeader("aa"));
    assertEquals("cc", r.getHeader("aA"));
    assertEquals("cc", r.getHeader("AA"));

  }

  @Test
  public void testHeader2() {
    ODataRequest r = new ODataRequest();
    r.addHeader("AA", Collections.singletonList("dd"));

    assertEquals("dd", r.getHeaders("aa").get(0));
    assertEquals("dd", r.getHeaders("aA").get(0));
    assertEquals("dd", r.getHeaders("AA").get(0));
  }

  @Test
  public void testMultiValueHeader() {
    ODataRequest r = new ODataRequest();

    r.addHeader("aa", Arrays.asList("a", "b"));

    assertEquals("a", r.getHeaders("aa").get(0));
    assertEquals("b", r.getHeaders("aA").get(1));

    r.addHeader("Aa", Collections.singletonList("c"));

    assertEquals("a", r.getHeaders("aa").get(0));
    assertEquals("b", r.getHeaders("aA").get(1));
    assertEquals("c", r.getHeaders("aA").get(2));
  }
}
