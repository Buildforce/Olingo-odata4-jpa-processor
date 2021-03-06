/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.buildforce.olingo.commons.api.format.ContentType;

import org.junit.Test;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

public class HeaderTest {

  @Test
  public void test() {
    Header header = new Header(1);
    header.addHeader(CONTENT_TYPE, ContentType.MULTIPART_MIXED.toString(), 1);

    assertEquals(ContentType.MULTIPART_MIXED.toString(), header.getHeader(CONTENT_TYPE));
    assertEquals(1, header.getHeaders(CONTENT_TYPE).size());
    assertEquals(ContentType.MULTIPART_MIXED.toString(),
        header.getHeaders(CONTENT_TYPE).get(0));
  }

  @Test
  public void notAvailable() {
    Header header = new Header(1);

    assertNull(header.getHeader(CONTENT_TYPE));
    assertEquals(0, header.getHeaders(CONTENT_TYPE).size());
  }

  @Test
  public void caseInsensitive() {
    Header header = new Header(1);
    header.addHeader(CONTENT_TYPE, ContentType.MULTIPART_MIXED.toString(), 1);

    assertEquals(ContentType.MULTIPART_MIXED.toString(), header.getHeader("cOnTenT-TyPE"));
    assertEquals(1, header.getHeaders("cOnTenT-TyPE").size());
    assertEquals(ContentType.MULTIPART_MIXED.toString(), header.getHeaders("cOnTenT-TyPE").get(0));
  }

  @Test
  public void duplicatedAdd() {
    Header header = new Header(1);
    header.addHeader(CONTENT_TYPE, ContentType.MULTIPART_MIXED.toString(), 1);
    header.addHeader(CONTENT_TYPE, ContentType.MULTIPART_MIXED.toString(), 2);

    assertEquals(ContentType.MULTIPART_MIXED.toString(), header.getHeader(CONTENT_TYPE));
    assertEquals(1, header.getHeaders(CONTENT_TYPE).size());
    assertEquals(ContentType.MULTIPART_MIXED.toString(),
        header.getHeaders(CONTENT_TYPE).get(0));
  }

  @Test
  public void fieldName() {
    Header header = new Header(0);
    header.addHeader("MyFieldNamE", "myValue", 1);

    assertEquals("MyFieldNamE", header.getHeaderField("myfieldname").getFieldName());
    assertEquals("MyFieldNamE", header.toSingleMap().keySet().toArray(new String[0])[0]);
    assertEquals("MyFieldNamE", header.toMultiMap().keySet().toArray(new String[0])[0]);

    assertEquals("myValue", header.toMultiMap().get("MyFieldNamE").get(0));
    assertEquals("myValue", header.toSingleMap().get("MyFieldNamE"));
  }

  @Test
  public void deepCopy() throws Exception {
    Header header = new Header(1);
    header.addHeader(CONTENT_TYPE, ContentType.MULTIPART_MIXED + ";boundary=123", 1);

    Header copy = header.clone();
    assertEquals(header.getHeaders(CONTENT_TYPE), copy.getHeaders(CONTENT_TYPE));
    assertEquals(header.getHeader(CONTENT_TYPE), copy.getHeader(CONTENT_TYPE));
    assertEquals(header.getHeaderField(CONTENT_TYPE), copy.getHeaderField(CONTENT_TYPE));

      assertNotSame(header.getHeaders(CONTENT_TYPE), copy.getHeaders(CONTENT_TYPE));
      assertNotSame(header.getHeaderField(CONTENT_TYPE), copy.getHeaderField(CONTENT_TYPE));
  }

  @Test
  public void deepCopyHeaderField() throws Exception {
    List<String> values = new ArrayList<String>();
    values.add("abc");
    values.add("def");
    HeaderField field = new HeaderField("name", values, 17);

    HeaderField clone = field.clone();
    assertEquals(field.getFieldName(), clone.getFieldName());
    assertEquals(field.getLineNumber(), clone.getLineNumber());
    assertEquals(field.getValues(), clone.getValues());

      assertNotSame(field.getValues(), clone.getValues());
  }

  @Test
  public void duplicatedAddList() {
    Header header = new Header(1);
    header.addHeader(CONTENT_TYPE, ContentType.MULTIPART_MIXED.toString(), 1);
    header.addHeader(CONTENT_TYPE,
            Arrays.asList(ContentType.MULTIPART_MIXED.toString(), ContentType.APPLICATION_ATOM_SVC.toString()),
            2);

    assertEquals(ContentType.MULTIPART_MIXED + ", " + ContentType.APPLICATION_ATOM_SVC, header
        .getHeader(CONTENT_TYPE));
    assertEquals(2, header.getHeaders(CONTENT_TYPE).size());
    assertEquals(ContentType.MULTIPART_MIXED.toString(),
        header.getHeaders(CONTENT_TYPE).get(0));
    assertEquals(ContentType.APPLICATION_ATOM_SVC.toString(),
        header.getHeaders(CONTENT_TYPE).get(1));
  }

  @Test
  public void remove() {
    Header header = new Header(1);
    header.addHeader(CONTENT_TYPE, ContentType.MULTIPART_MIXED.toString(), 1);
    header.removeHeader(CONTENT_TYPE);

    assertNull(header.getHeader(CONTENT_TYPE));
    assertEquals(0, header.getHeaders(CONTENT_TYPE).size());
  }

  @Test
  public void multipleValues() {
    Header header = new Header(1);
    header.addHeader(CONTENT_TYPE, ContentType.MULTIPART_MIXED.toString(), 1);
    header.addHeader(CONTENT_TYPE, ContentType.APPLICATION_ATOM_SVC.toString(), 2);
    header.addHeader(CONTENT_TYPE, ContentType.APPLICATION_ATOM_XML.toString(), 3);

    String fullHeaderString =
        ContentType.MULTIPART_MIXED + ", " + ContentType.APPLICATION_ATOM_SVC + ", "
            + ContentType.APPLICATION_ATOM_XML;

    assertEquals(fullHeaderString, header.getHeader(CONTENT_TYPE));
    assertEquals(3, header.getHeaders(CONTENT_TYPE).size());
    assertEquals(ContentType.MULTIPART_MIXED.toString(),
        header.getHeaders(CONTENT_TYPE).get(0));
    assertEquals(ContentType.APPLICATION_ATOM_SVC.toString(),
        header.getHeaders(CONTENT_TYPE).get(1));
    assertEquals(ContentType.APPLICATION_ATOM_XML.toString(),
        header.getHeaders(CONTENT_TYPE).get(2));
  }

  @Test
  public void splitValues() {
    final String values = "abc, def,123,77,   99, ysd";
    List<String> splittedValues = Header.splitValuesByComma(values);

    assertEquals(6, splittedValues.size());
    assertEquals("abc", splittedValues.get(0));
    assertEquals("def", splittedValues.get(1));
    assertEquals("123", splittedValues.get(2));
    assertEquals("77", splittedValues.get(3));
    assertEquals("99", splittedValues.get(4));
    assertEquals("ysd", splittedValues.get(5));
  }
  
  @Test
  public void testHashCode() {
    HeaderField header = new HeaderField("filed", 0);
    assertNotNull(header.hashCode());
  }

}