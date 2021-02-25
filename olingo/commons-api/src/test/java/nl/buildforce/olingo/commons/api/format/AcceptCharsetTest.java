/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

import org.junit.Test;

public class AcceptCharsetTest {
  @Test
  public void wildcard() {
    List<AcceptCharset> charsets = AcceptCharset.create("*");
    assertEquals("*", charsets.get(0).getCharset());
  }
  
  @Test
  public void illegalCharset() {
    expectCreateError("abc");
  }
  
  @Test
  public void illegalCharsetWithDelimiters() {
    expectCreateError("utf<8");
    expectCreateError(",,,,");
    expectCreateError(", , , ");
    expectCreateError("utf 8");
    expectCreateError("utf=8");
    expectCreateError("utf-8;<");
    expectCreateError("utf-8;q<");
    expectCreateError("utf-8;q=<");
    expectCreateError("utf-8;q=1<");
    expectCreateError("utf-8;abc=xyz");
    expectCreateError("utf-8;");
    expectCreateError("utf-8;q='");
    expectCreateError("utf-8;q=0.1, utf8;q=0.8, iso-8859-1, abc, xyz<");
    expectCreateError("utf-8;q=0.1, utf8;q=0.8<, iso-8859-1, abc");
    expectCreateError("utf-8;abc=xyz");
    expectCreateError("utf-8;q=0.8;abc=xyz");
    expectCreateError("utf-8;q=xyz");
  }
  
  @Test
  public void unsupportedCharset() {
    expectCreateError("iso-8859-1");
  }
  
  @Test
  public void correctCharset() {
    List<AcceptCharset> charsets = AcceptCharset.create("utf-8");
    assertEquals("utf-8", charsets.get(0).getCharset());
  }
  
  @Test
  public void correctCharsetWithQParams() {
    List<AcceptCharset> charsets = AcceptCharset.create("utf-8;q=0.8");
    assertEquals("utf-8", charsets.get(0).getCharset());
    assertEquals(1, charsets.get(0).getParameters().size());
    assertEquals("0.8", charsets.get(0).getParameter(TypeUtil.PARAMETER_Q));
    assertEquals(Float.parseFloat("0.8"), 
        charsets.get(0).getQuality().floatValue(), Float.parseFloat("0.8"));
  }
  
  @Test
  public void multipleCharsetsWithQParams() {
    List<AcceptCharset> charsets = AcceptCharset.create("utf-8;q=0.1, utf8;q=0.8");
    assertEquals("utf8", charsets.get(0).getCharset());
    assertEquals("utf-8", charsets.get(1).getCharset());
    assertEquals(1, charsets.get(0).getParameters().size());
    assertEquals(1, charsets.get(1).getParameters().size());
    assertEquals("0.8", charsets.get(0).getParameter(TypeUtil.PARAMETER_Q));
    assertEquals("0.1", charsets.get(1).getParameter(TypeUtil.PARAMETER_Q));
  }
  
  @Test
  public void multipleCharsetsWithQParamsAndUnsupportedCharsets() {
    List<AcceptCharset> charsets = AcceptCharset.create("utf-8;q=0.1, utf8;q=0.8, iso-8859-1, abc");
    assertEquals("utf8", charsets.get(0).getCharset());
    assertEquals("utf-8", charsets.get(1).getCharset());
    assertEquals(1, charsets.get(0).getParameters().size());
    assertEquals(1, charsets.get(1).getParameters().size());
    assertEquals("0.8", charsets.get(0).getParameter(TypeUtil.PARAMETER_Q));
    assertEquals("0.1", charsets.get(1).getParameter(TypeUtil.PARAMETER_Q));
    assertEquals("utf8;q=0.8", charsets.get(0).toString());
  }
  
  @Test
  public void multipleCharsetsWithSameQParams() {
    List<AcceptCharset> charsets = AcceptCharset.create("utf-8;q=0.1, utf8;q=0.1");
    assertEquals("utf-8", charsets.get(0).getCharset());
    assertEquals("utf8", charsets.get(1).getCharset());
    assertEquals(1, charsets.get(0).getParameters().size());
    assertEquals(1, charsets.get(1).getParameters().size());
    assertEquals("0.1", charsets.get(0).getParameter(TypeUtil.PARAMETER_Q));
    assertEquals("0.1", charsets.get(1).getParameter(TypeUtil.PARAMETER_Q));
    assertEquals("utf-8;q=0.1", charsets.get(0).toString());
  }
  
  @Test
  public void multipleCharsetsFail() {
    expectCreateError("iso-8859-5, unicode-1-1;q=0.8");
  }
  
  private void expectCreateError(String value) {
    try {
      AcceptCharset.create(value);
      fail("Expected exception not thrown.");
    } catch (UnsupportedCharsetException e) {
      assertNotNull(e);
    } catch (IllegalArgumentException e) {
      assertNotNull(e);
    }
  }
  
  @Test
  public void illegalQParam() {
    expectCreateError("utf-8;q=12");
  }
  
  @Test
  public void emptyCharset() {
    expectCreateError("");
  }
  
  @Test
  public void nullCharset() {
    expectCreateError(null);
  }
  
  @Test
  public void trailingSemicolon() {
    expectCreateError("utf-8;");
  }
}
