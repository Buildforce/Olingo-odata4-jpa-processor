/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.buildforce.olingo.commons.api.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ContentTypeTest {

  @Test
  public void create() {
    assertEquals("a/b", new ContentType("a/b").toString());
    assertEquals(new ContentType("a/b;c=d;x=y"), new ContentType("a/b;x=y;c=d"));
    assertEquals(new ContentType("a/b;c=d;x=y"), new ContentType("a/b; x=y; c=d"));
    assertEquals(new ContentType("a/b"), new ContentType("a/b"));
  }

  @Test
  public void createFail() {
    createWrong("a");
    createWrong(" a / b ");
    createWrong("a/b;");
    createWrong("a/b;parameter");
    createWrong("a/b;parameter=");
    createWrong("a/b;=value");
    createWrong("a/b;the name=value");
    createWrong("a/b;name= value");

    createWrong("*/*");
    createWrong("*");
    createWrong("a//b");
    createWrong("///");
    createWrong("a/*");
    createWrong("*/b");

    createWrong(null);
  }

  @Test
  public void createWithParameter() {
    assertEquals(new ContentType("a/b;c=d"), new ContentType(new ContentType("a/b"), "c", "d"));
    assertEquals(new ContentType("a/b;e=f;c=d"), new ContentType(
        new ContentType(new ContentType("a/b"), "c", "d"), "e", "f"));
    assertEquals(new ContentType("a/b;e=f;c=d"), new ContentType(
        new ContentType(new ContentType("a/b"), "C", "D"), "E", "F"));
  }

  @Test
  public void createAndModify() {
    ContentType ct1 = new ContentType("a/b");
    assertEquals(new ContentType("a/b;c=d"), new ContentType(ct1, "c", "d"));

    ContentType ct2 = new ContentType("a/b;c=d");
    assertEquals(new ContentType("a/b;c=d;e=f"), new ContentType(ct2, "e", "f"));
    assertEquals(new ContentType("a/b;c=g"), new ContentType(ct2, "c", "g"));

      assertNotEquals(new ContentType(ct2, "c", "g"), ct2);
  }

  @Test
  public void parse() {
    assertEquals(ContentType.APPLICATION_OCTET_STREAM, ContentType.parse("application/octet-stream"));

    assertNull(ContentType.parse("a"));
    assertNull(ContentType.parse("a/b;c"));
    assertNull(ContentType.parse("a/b;c="));
    assertNull(ContentType.parse("a/b;c= "));
  }

  @Test
  public void charsetUtf8() {
    ContentType ct1 = new ContentType("a/b;charset=utf8");
    ContentType ct2 = new ContentType("a/b;charset=utf-8");

    assertNotEquals(ct1, ct2);
    assertEquals(ct1.getMainType(), ct2.getMainType());
    assertEquals(ct1.getSubtype(), ct2.getSubtype());
    assertEquals("utf8", ct1.getParameters().get(ContentType.PARAMETER_CHARSET));
    assertEquals("utf-8", ct2.getParameters().get(ContentType.PARAMETER_CHARSET));
    assertEquals("utf-8", ct2.getParameter(ContentType.PARAMETER_CHARSET));

    assertTrue(ct1.isCompatible(ct2));
  }

  @Test
  public void toContentTypeString() {
    assertEquals("application/json;a=b;c=d",
        new ContentType(new ContentType(ContentType.APPLICATION_JSON, "a", "b"), "c", "d")
            .toString());
  }

  private void createWrong(String value) {
    try {
      new ContentType(value);
      fail("Expected exception not thrown.");
    } catch (IllegalArgumentException e) {
      assertNotNull(e);
    }
  }

}