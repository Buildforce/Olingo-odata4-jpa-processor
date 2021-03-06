/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;


public class AcceptTypeTest {

  @Test
  public void wildcard() {
    List<AcceptType> atl = AcceptType.create("*/*");

    assertEquals(1, atl.size());
    assertEquals("*/*", atl.get(0).toString());

    assertTrue(atl.get(0).matches(new ContentType("a/a")));
    assertTrue(atl.get(0).matches(new ContentType("b/b")));
  }

  @Test
  public void wildcardSubtype() {
    List<AcceptType> atl = AcceptType.create("a/*");

    assertEquals(1, atl.size());
    assertEquals("a/*", atl.get(0).toString());

    assertTrue(atl.get(0).matches(new ContentType("a/a")));
    assertFalse(atl.get(0).matches(new ContentType("b/b")));
  }

  @Test
  public void singleAcceptType() {
    assertTrue(AcceptType.create("a/a").get(0).matches(new ContentType("a/a")));
    assertTrue(AcceptType.create("a/a;q=0.2").get(0).matches(new ContentType("a/a")));
    assertFalse(AcceptType.create("a/a;x=y;q=0.2").get(0).matches(new ContentType("a/a")));
    assertTrue(AcceptType.create("a/a;x=y;q=0.2").get(0).matches(new ContentType("a/a;x=y")));
    assertTrue(AcceptType.create("a/a; q=0.2").get(0).matches(new ContentType("a/a")));

    assertEquals("a/a;q=0.2;x=y", AcceptType.create("a/a;x=y;q=0.2").get(0).toString());
  }

  @Test
  public void acceptTypes() {
    List<AcceptType> atl;

    atl = AcceptType.create("b/b,*/*,a/a,c/*");
    assertNotNull(atl);
    assertTrue(atl.get(0).matches(new ContentType("b/b")));
    assertTrue(atl.get(1).matches(new ContentType("a/a")));
    assertEquals("c", atl.get(2).getType());
    assertEquals(TypeUtil.MEDIA_TYPE_WILDCARD, atl.get(2).getSubtype());
    assertEquals(TypeUtil.MEDIA_TYPE_WILDCARD, atl.get(3).getType());
    assertEquals(TypeUtil.MEDIA_TYPE_WILDCARD, atl.get(3).getSubtype());

    atl = AcceptType.create("a/a;q=0.3,*/*;q=0.1,b/b;q=0.2");
    assertNotNull(atl);
    assertTrue(atl.get(0).matches(new ContentType("a/a")));
    assertTrue(atl.get(1).matches(new ContentType("b/b")));
    assertEquals(TypeUtil.MEDIA_TYPE_WILDCARD, atl.get(2).getType());
    assertEquals(TypeUtil.MEDIA_TYPE_WILDCARD, atl.get(2).getSubtype());

    atl = AcceptType.create("a/a;q=0.3,*/*;q=0.3");
    assertNotNull(atl);
    assertTrue(atl.get(0).matches(new ContentType("a/a")));
    assertEquals(TypeUtil.MEDIA_TYPE_WILDCARD, atl.get(1).getType());
    assertEquals(TypeUtil.MEDIA_TYPE_WILDCARD, atl.get(1).getSubtype());

    atl = AcceptType.create("a/a;x=y;q=0.1,b/b;x=y;q=0.3");
    assertNotNull(atl);
    assertTrue(atl.get(0).matches(new ContentType("b/b;x=y")));
    assertFalse(atl.get(0).matches(new ContentType("b/b;x=z")));
    assertTrue(atl.get(1).matches(new ContentType("a/a;x=y")));
    assertFalse(atl.get(1).matches(new ContentType("a/a;x=z")));

    atl = AcceptType.create("a/a; q=0.3, */*; q=0.1, b/b; q=0.2");
    assertNotNull(atl);
  }

  @Test
  public void withQParameter() {
    List<AcceptType> acceptTypes = AcceptType.create("application/json;q=0.2");

    assertEquals(1, acceptTypes.size());
    AcceptType acceptType = acceptTypes.get(0);
    assertEquals(ContentType.APPLICATION, acceptType.getType());
    assertEquals(ContentType.JSON, acceptType.getSubtype());
    assertEquals("0.2", acceptType.getParameters().get(TypeUtil.PARAMETER_Q));
    assertEquals("0.2", acceptType.getParameter(TypeUtil.PARAMETER_Q));
    assertEquals(Float.valueOf(0.2F), acceptType.getQuality());
    assertEquals("application/json;q=0.2", acceptType.toString());
  }

  @Test
  public void formatErrors() {
    expectCreateError("/");
    expectCreateError("//");
    expectCreateError("///");
    expectCreateError("a/b/c");
    expectCreateError("a//b");
  }

  @Test
  public void abbreviationsNotAllowed() {
    expectCreateError(ContentType.APPLICATION);
  }

  @Test
  public void wildcardError() {
    expectCreateError("*/json");
  }

  @Test
  public void wrongQParameter() {
    expectCreateError(" a/a;q=z ");
    expectCreateError("a/a;q=42");
    expectCreateError("a/a;q=0.0001");
    expectCreateError("a/a;q='");
    expectCreateError("a/a;q=0.8,abc");
  }

  @Test
  public void parameterErrors() {
    expectCreateError("a/b;parameter");
    expectCreateError("a/b;parameter=");
    expectCreateError("a/b;name= value");
    expectCreateError("a/b;=value");
    expectCreateError("a/b;the name=value");
  }

  @Test
  public void trailingSemicolon() {
    expectCreateError("a/b;");
  }

  @Test
  public void fromContentType() {
    List<AcceptType> acceptType = AcceptType.fromContentType(ContentType.APPLICATION_JSON);
    assertNotNull(acceptType);
    assertEquals(1, acceptType.size());
    assertEquals(ContentType.APPLICATION_JSON.toString(), acceptType.get(0).toString());
  }

  private void expectCreateError(String value) {
    try {
      AcceptType.create(value);
      fail("Expected exception not thrown.");
    } catch (IllegalArgumentException e) {
      assertNotNull(e);
    }
  }
  
  @Test
  public void multipleTypeswithQParameter() {
    List<AcceptType> acceptTypes = AcceptType.create("application/json;q=0.2,application/json;q=0.2");

    assertEquals(2, acceptTypes.size());
    AcceptType acceptType = acceptTypes.get(0);
    assertEquals(ContentType.APPLICATION, acceptType.getType());
    assertEquals(ContentType.JSON, acceptType.getSubtype());
    assertEquals("0.2", acceptType.getParameters().get(TypeUtil.PARAMETER_Q));
    assertEquals("0.2", acceptType.getParameter(TypeUtil.PARAMETER_Q));
    assertEquals(Float.valueOf(0.2F), acceptType.getQuality());
    assertEquals("application/json;q=0.2", acceptType.toString());
  }
  
  @Test
  public void multipleTypeswithIllegalTypes() {
    List<AcceptType> acceptTypes = AcceptType.create("application/json;q=0.2,abc/xyz");

    assertEquals(2, acceptTypes.size());
    AcceptType acceptType = acceptTypes.get(1);
    assertEquals(ContentType.APPLICATION, acceptType.getType());
    assertEquals(ContentType.JSON, acceptType.getSubtype());
    assertEquals("0.2", acceptType.getParameters().get(TypeUtil.PARAMETER_Q));
    assertEquals("0.2", acceptType.getParameter(TypeUtil.PARAMETER_Q));
    assertEquals(Float.valueOf(0.2F), acceptType.getQuality());
    assertEquals("application/json;q=0.2", acceptType.toString());
  }
  
  @Test
  public void multipleFormatErrors() {
    expectCreateError("/,abc,a/a;parameter=");
  }
  
  @Test
  public void nullAcceptType() {
    expectCreateError(null);
  }
  
  @Test
  public void emptyAcceptType() {
    expectCreateError("");
  }
  
  @Test
  public void noTypeAcceptType() {
    expectCreateError("/json");
  }
  
  @Test
  public void withCharset() {
    List<AcceptType> acceptTypes = AcceptType.create("application/json;charset=utf-8");
    assertEquals(1, acceptTypes.size());
    AcceptType acceptType = acceptTypes.get(0);
    assertEquals(ContentType.APPLICATION, acceptType.getType());
    assertEquals(ContentType.JSON, acceptType.getSubtype());
    assertEquals("utf-8", acceptType.getParameter(ContentType.PARAMETER_CHARSET));
    
    assertTrue(acceptType.matches(new ContentType("application/json;" + "odata.metadata=minimal;charset=utf-8")));
    assertFalse(acceptType.matches(new ContentType("application/atom+xml;" + "odata.metadata=minimal;charset=utf-8")));
    assertFalse(acceptType.matches(new ContentType("application/json;" + "odata.metadata=minimal")));
  }
  
  @Test
  public void withSubtypeStar1() {
    List<AcceptType> acceptTypes = AcceptType.create("application/json,application/*");
    assertEquals(2, acceptTypes.size());
    AcceptType acceptType1 = acceptTypes.get(0);
    assertEquals(ContentType.APPLICATION, acceptType1.getType());
    assertEquals(ContentType.JSON, acceptType1.getSubtype());
    
    AcceptType acceptType2 = acceptTypes.get(1);
    assertEquals(ContentType.APPLICATION, acceptType2.getType());
    assertEquals("*", acceptType2.getSubtype());
  }
  
  @Test
  public void withSubtypeStar2() {
    List<AcceptType> acceptTypes = AcceptType.create("application/*,application/json");
    assertEquals(2, acceptTypes.size());
    AcceptType acceptType1 = acceptTypes.get(0);
    assertEquals(ContentType.APPLICATION, acceptType1.getType());
    assertEquals(ContentType.JSON, acceptType1.getSubtype());
    
    AcceptType acceptType2 = acceptTypes.get(1);
    assertEquals(ContentType.APPLICATION, acceptType2.getType());
    assertEquals("*", acceptType2.getSubtype());
  }

}