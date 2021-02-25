/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ODataResponseTest {

  @Test
  public void testResponse() {
    ODataResponse  r = new ODataResponse ();
    assertNotNull(r);
    r.addHeader("header", "value");
    List<String> list = new ArrayList<String>();
    r.addHeader("headerList", list );
    assertNotNull(r.getAllHeaders());
  }
  
  @Test
  public void testError() {
    ODataServerError  r = new ODataServerError ();
    assertNotNull(r);
    assertNull(r.getLocale());
    Map<String, String> map = new HashMap<String, String>();
    r.setInnerError(map);
    assertNotNull(r.getInnerError());
  }
}
