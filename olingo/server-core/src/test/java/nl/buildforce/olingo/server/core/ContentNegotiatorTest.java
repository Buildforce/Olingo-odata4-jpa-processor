/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.serializer.CustomContentTypeSupport;
import nl.buildforce.olingo.server.api.serializer.RepresentationType;
import nl.buildforce.olingo.server.api.uri.queryoption.FormatOption;

import org.junit.Test;

import static com.google.common.net.HttpHeaders.ACCEPT;
import static com.google.common.net.HttpHeaders.ACCEPT_CHARSET;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContentNegotiatorTest {

  static final private String ACCEPT_CASE_MIN = ContentType.CT_JSON.toString();
  static final private String ACCEPT_CASE_MIN_UTF8 = "application/json;charset=UTF-8;odata.metadata=minimal";
  static final private String ACCEPT_CASE_MIN_UTF81 = "application/json;charset=utf-8;odata.metadata=minimal";
  static final private String ACCEPT_CASE_ISO_8859_1 = "application/json;charset=ISO-8859-1";
  static final private String ACCEPT_CASE_FULL = ContentType.JSON_FULL_METADATA.toString();
  static final private String ACCEPT_CASE_NONE = ContentType.JSON_NO_METADATA.toString();
  static final private String ACCEPT_CASE_MIN_UTF8_IEEE754 =
      "application/json;charset=UTF-8;odata.metadata=minimal;IEEE754Compatible=true";
  static final private String ACCEPT_CASE_MIN_IEEE754 = ACCEPT_CASE_MIN + ";IEEE754Compatible=true";
  String ACCEPT_CASE_MIN_IEEE754_1 = ACCEPT_CASE_MIN + ";IEEE754Compatible=false";
  static final private String ACCEPT_CASE_MIN_IEEE754_FAIL = ACCEPT_CASE_MIN + ";IEEE754Compatible=xyz";
  static final private String ACCEPT_CASE_JSONQ = "application/json;q=0.2";
  static final private String ACCEPT_CASE_XML = ContentType.APPLICATION_XML.toString();
  static final private String ACCEPT_CASE_JSON = ContentType.APPLICATION_JSON.toString();
  static final private String ACCEPT_CASE_WILDCARD1 = "*/*";
  static final private String ACCEPT_CASE_WILDCARD2 = "application/*";
  static final private String ACCEPT_CASE_JSON_IEEE754 = ACCEPT_CASE_JSON + ";IEEE754Compatible=true";
  static final private String ACCEPT_CASE_MULTIPART_MIXED = ContentType.MULTIPART_MIXED.toString();

  //@formatter:off (Eclipse formatter)
  //CHECKSTYLE:OFF (Maven checkstyle)

  String[][] casesServiceDocument = {
      /* expected                     $format           accept                          modified content types */
      { ACCEPT_CASE_MIN,              null,             null,                           null                  },
      { ACCEPT_CASE_MIN,              ContentType.JSON,           null,                           null                  },
      { ACCEPT_CASE_MIN,              ContentType.JSON,           ACCEPT_CASE_JSONQ,              null                  },
      { ACCEPT_CASE_NONE,             ACCEPT_CASE_NONE, null,                           null                  },
      { "a/a",                        "a/a",            null,                           "a/a"                 },
      { ACCEPT_CASE_MIN,              null,             ACCEPT_CASE_JSONQ,              null                  },
      { ACCEPT_CASE_MIN,              null,             ACCEPT_CASE_WILDCARD1,          null                  },
      { ACCEPT_CASE_MIN,              null,             ACCEPT_CASE_WILDCARD2,          null                  },
      { ACCEPT_CASE_MIN,              null,             null,                           ACCEPT_CASE_MIN       },
      { "a/a",                        "a/a",            null,                           "a/a,b/b"             },
      { "a/a;x=y",                    "a/a",            ACCEPT_CASE_WILDCARD1,          "a/a;x=y"             },
      { "a/a;v=w;x=y",                null,             "a/a;x=y",                      "a/a;b=c,a/a;v=w;x=y" },
      { "a/a;v=w;x=y",                "a/a;x=y",        null,                           "a/a;b=c,a/a;v=w;x=y" },
      { ACCEPT_CASE_MIN,              ContentType.JSON,           ACCEPT_CASE_MIN,                null                  },
      { ACCEPT_CASE_FULL,             null,             ACCEPT_CASE_FULL,               ACCEPT_CASE_FULL      },
      { ACCEPT_CASE_MIN_UTF8,         null,             ACCEPT_CASE_MIN_UTF8,           null                  },
      { ACCEPT_CASE_MIN_IEEE754,      null,             ACCEPT_CASE_MIN_IEEE754,        null                  },
      { ACCEPT_CASE_MIN_UTF8_IEEE754, null,             ACCEPT_CASE_MIN_UTF8_IEEE754,   null                  },
      { ACCEPT_CASE_MIN_IEEE754,      ACCEPT_CASE_MIN_IEEE754, ACCEPT_CASE_MIN ,        null                  },
      { ACCEPT_CASE_XML,              "xml",            null,                           null                  },
      { ACCEPT_CASE_XML,              null,             ACCEPT_CASE_XML,                null                  },
	  { ACCEPT_CASE_MIN_IEEE754_1,    null,             ACCEPT_CASE_MIN_IEEE754_1,      null                  }
  };

  String[][] casesMetadata = {
      /* expected               $format           accept                 modified content types */
      { ACCEPT_CASE_XML,        null,             null,                  null             },
      { ACCEPT_CASE_XML,        "xml",            null,                  null             },
      { ACCEPT_CASE_XML,        "xml",            ACCEPT_CASE_XML,       null             },
      { "a/a",                  "a/a",            null,                  "a/a"            },
      { ACCEPT_CASE_XML,        null,             ACCEPT_CASE_XML,       null             },
      { ACCEPT_CASE_XML,        null,             ACCEPT_CASE_WILDCARD1, null             },
      { ACCEPT_CASE_XML,        null,             ACCEPT_CASE_WILDCARD2, null             },
      { "a/a",                  "a/a",            null,                  "a/a,b/b"        },
      { "a/a;x=y",              "a/a",            ACCEPT_CASE_WILDCARD1, "a/a;x=y"        },
      { ACCEPT_CASE_JSON,       ContentType.JSON,           ACCEPT_CASE_JSON_IEEE754, null          },
      { ACCEPT_CASE_JSON,       ContentType.JSON,           ACCEPT_CASE_WILDCARD1,   null           },
      { ACCEPT_CASE_JSON,       "application/json",ACCEPT_CASE_JSON_IEEE754, null         },
      { ACCEPT_CASE_JSON_IEEE754,null,            ACCEPT_CASE_JSON_IEEE754, null          },
      { ACCEPT_CASE_JSON,        null,            ACCEPT_CASE_JSON,         null          }
  };

  String[][] casesMetadataFail = {
      /* expected               $format           accept                 modified content types */
      { "Unsupported $format = json;IEEE754Compatible=true","json;IEEE754Compatible=true", null, null},
      { "Unsupported $format = json;charset=ISO-8859-1","json;charset=ISO-8859-1",     null, null},
      { "Unsupported or illegal Accept header value: json;"
          + "charset=ISO-8859-1 != [application/xml, application/json]",null,
          "json;charset=ISO-8859-1", null},
      { "Unsupported $format = application/json;charset=ISO-8859-1",
            "application/json;charset=ISO-8859-1",null, null},
  };
  
  String[][] casesFail = {
      /* expected               $format           accept                 modified content types */
      { null,                   "xxx/yyy",        null,                  null             },
      { null,                   "a/a",            null,                  "b/b"            },
      { null,                   "a/a;x=y",        null,                  "a/a;v=w"        },
      { null,                   null,             "a/a;x=y",             "a/a;v=w"        },
      { null,                   null,             "*",                   null             },
      { null,                   "a/b;charset=ISO-8859-1", null,          "a/b"            },
      { null,                   null,             "a/b;charset=ISO-8859-1", "a/b"         },
      { null,                   null,             null,                  "text/plain"     },
	  { null,                   "xxx",            null,                  null             },
      { null,                   null,             ACCEPT_CASE_MIN_IEEE754_FAIL,null       },
      { null,                   null,             "application/json;charset=utf<8",null   },
      { null,                   null,             "application/json;charset=utf-8;q=<",null},
      { null,                   null,             "application/json;charset=utf-8,application/json;q=1<",null},
      { null,                   null,             "application/json;charset=utf-8,abc",null}
  };
  
  String[][] casesAcceptCharset = {
      /* expected               $format           accept                 modified content types  acceptCharset*/
      { ACCEPT_CASE_MIN_UTF8,    null,             null,                  null,                    "utf-8"    },
      { ACCEPT_CASE_MIN_UTF8,   ContentType.JSON,           ACCEPT_CASE_MIN_UTF8,   null,                    "utf-8"    },
      { ACCEPT_CASE_MIN_UTF8,    null,            ACCEPT_CASE_ISO_8859_1, null,                    "utf-8"    },
      { ACCEPT_CASE_MIN_UTF81,   null,            ACCEPT_CASE_ISO_8859_1, null,                    "utf-8"    },
      { ACCEPT_CASE_MIN_UTF81,   null,           "application/json;charset=abc", null,             "utf-8"    },
      { ACCEPT_CASE_MIN_UTF8,   null,            "application/json;charset=utf-8", null,              null    },
      { ACCEPT_CASE_MIN_UTF8,   null,            "application/json;charset=utf8", null,              null    },
      { ACCEPT_CASE_MIN_UTF8,   null,            "application/json;charset=utf8;q=0.8", null,        null    }
  };
  
  String[][] casesAcceptCharsetFail = {
      /* expected               $format           accept                 modified content types   acceptCharset*/
      { null,                   null,             null,                   null,                     "ISO-8859-1" },
      { null,                   ContentType.JSON,           ACCEPT_CASE_MIN_UTF8,   null,                     "abc"        },
      { null,                   null,             ACCEPT_CASE_ISO_8859_1, null,                     "utf<8"      },
      { null,                   null,             ACCEPT_CASE_MIN_UTF8, null,                       "utf-8;abc=xyz"},
      { null,                   null,             ACCEPT_CASE_MIN_UTF8, null,                       "utf-8;q=1<"  },
      { null,                   null,             ACCEPT_CASE_MIN_UTF8, null,                        "utf-8;<"   },
      { null,                   null,             ACCEPT_CASE_ISO_8859_1, null,                       null       },
      { null,                   null,             "application/json;charset=abc", null,               null       },
      { null,                   null,             "application/json;charset=utf-8;q", null,           null       },
      { null,                   null,             "application/json;charset=utf-8;abc=xyz", null,     null       },
      { null,                   null,             "application/json;charset=utf-8;q<", null,          null       },
      { null,                   null,             "application/json;charset=utf<8", null,             null       },
      { null,                  "json;charset=abc",ACCEPT_CASE_MIN_UTF8,             null,             null       },
      { null,                  "json;charset=utf<8",ACCEPT_CASE_MIN_UTF8,           null,             null       },
      { null,                  "json;charset=utf-8;abc=xyz",ACCEPT_CASE_MIN_UTF8,   null,             null       },
      { null,                  "json;charset=utf-8;q=1<",ACCEPT_CASE_MIN_UTF8,      null,             null       },
      { null,                  "json;charset=utf-8;q='",ACCEPT_CASE_MIN_UTF8,       null,             null       },
      { null,                  "application/json;abc=xyz",ACCEPT_CASE_MIN_UTF8,       null,           null       },
      { null,                  "application/json;charset=utf<8",ACCEPT_CASE_MIN_UTF8, null,           null       },
      { null,                  "application/json;charset=abc",ACCEPT_CASE_MIN_UTF8,   null,           null       }
  };
  
  //CHECKSTYLE:ON
  //@formatter:on

  @Test
  public void serviceDocumentSingleCase() throws Exception {
    testContentNegotiation(
        new String[] { ACCEPT_CASE_MIN_UTF8, null, ACCEPT_CASE_MIN_UTF8, null },
        RepresentationType.SERVICE);
  }

  @Test
  public void serviceDocument() throws Exception {
    for (String[] useCase : casesServiceDocument) {
      testContentNegotiation(useCase, RepresentationType.SERVICE);
    }
  }

  @Test
  public void metadataSingleCase() throws Exception {
    testContentNegotiation(new String[] { ACCEPT_CASE_XML, null, null, null }, RepresentationType.METADATA);
  }

  @Test
  public void metadataJson() throws Exception {
    testContentNegotiation(new String[] { ACCEPT_CASE_JSON, 
        "application/json", null, null }, RepresentationType.METADATA);
  }

  @Test
  public void metadata() throws Exception {
    for (String[] useCase : casesMetadata) {
      testContentNegotiation(useCase, RepresentationType.METADATA);
    }
  }

  @Test
  public void entityCollectionFail() throws Exception {
    for (String[] useCase : casesFail) {
      try {
        testContentNegotiation(useCase, RepresentationType.COLLECTION_ENTITY);
        fail("Exception expected for '" + useCase[1] + '|' + useCase[2] + '|' + useCase[3] + "'!");
      } catch (ContentNegotiatorException | IllegalArgumentException e) {
        // Expected Exception
      }
    }
  }
  
  @Test
  public void metadataFail() throws Exception {
    for (String[] useCase : casesMetadataFail) {
      try {
        testContentNegotiation(useCase, RepresentationType.METADATA);
        fail("Unsupported $format = " + useCase[1] + '|' + useCase[2] + '|' + useCase[3] + "'!");
      } catch (ContentNegotiatorException e) {
        // Expected Exception
      }
    }
  }

  @Test
  public void checkSupport() throws Exception {
    ContentNegotiator.checkSupport(ContentType.CT_JSON, null, RepresentationType.ENTITY);
    ContentNegotiator.checkSupport(ContentType.TEXT_PLAIN, null, RepresentationType.VALUE);
    try {
      ContentNegotiator.checkSupport(ContentType.APPLICATION_SVG_XML, null, RepresentationType.ENTITY);
      fail("Exception expected.");
    } catch (ContentNegotiatorException e) {
      assertEquals(ContentNegotiatorException.MessageKeys.UNSUPPORTED_CONTENT_TYPE, e.getMessageKey());
    }

    ContentNegotiator.checkSupport(new ContentType("a/b"), createCustomContentTypeSupport("a/b"), RepresentationType.ENTITY);
    ContentNegotiator.checkSupport(new ContentType(
        new ContentType("a/b"), "c", "d"),
        createCustomContentTypeSupport("a/b"),
        RepresentationType.ENTITY);
    try {
      ContentNegotiator.checkSupport(
          new ContentType("a/b"),
          createCustomContentTypeSupport("a/b;c=d"),
          RepresentationType.ENTITY);
      fail("Exception expected.");
    } catch (ContentNegotiatorException e) {
      assertEquals(ContentNegotiatorException.MessageKeys.UNSUPPORTED_CONTENT_TYPE, e.getMessageKey());
    }
  }

  private void testContentNegotiation(String[] useCase, RepresentationType representationType)
      throws Exception {

    FormatOption formatOption = null;
    if (useCase[1] != null) {
      formatOption = mock(FormatOption.class);
      when(formatOption.getFormat()).thenReturn(useCase[1]);
    }

    ODataRequest request = new ODataRequest();
    if (useCase[2] != null) {
      request.addHeader(ACCEPT, Collections.singletonList(useCase[2]));
    }
	
	if (useCase.length > 4) {
      if (useCase[4] != null) {
        request.addHeader(ACCEPT_CHARSET, Collections.singletonList(useCase[4]));
      }
    }

    CustomContentTypeSupport customContentTypeSupport = useCase[3] == null ? null :
      createCustomContentTypeSupport(useCase[3]);
    ContentType requestedContentType = ContentNegotiator.doContentNegotiation(
          formatOption, request, customContentTypeSupport, representationType);
    assertNotNull(requestedContentType);
    if (useCase[0] != null) {
      assertEquals(new ContentType(useCase[0]), requestedContentType);
    }
  }

  private CustomContentTypeSupport createCustomContentTypeSupport(String contentTypeString) {
    String[] contentTypes = contentTypeString.split(",");

    List<ContentType> types = new ArrayList<>();
    for (String contentType : contentTypes) { types.add(new ContentType(contentType)); }

    CustomContentTypeSupport customContentTypeSupport = mock(CustomContentTypeSupport.class);
    when(customContentTypeSupport.modifySupportedContentTypes(
        anyListOf(ContentType.class), any(RepresentationType.class)))
        .thenReturn(types);
    return customContentTypeSupport;
  }
  
  @Test
  public void testAcceptCharset() throws Exception {
    for (String[] useCase : casesAcceptCharset) {
      testContentNegotiation(useCase, RepresentationType.ENTITY);
    }
  }
  
  @Test
  public void testAcceptCharsetFail() throws Exception {
    for (String[] useCase : casesAcceptCharsetFail) {
      try {
        testContentNegotiation(useCase, RepresentationType.ENTITY);
        fail("Exception expected for '" + useCase[1] + '|' + useCase[2] + '|' + useCase[3] + "'!");
      } catch (ContentNegotiatorException | IllegalArgumentException e) {
        // Expected Exception
      }
    }
  }
  
  @Test
  public void testSupportedTypes() throws ContentNegotiatorException, IllegalArgumentException {
    assertTrue(ContentNegotiator.isSupported(new ContentType("a/b"),
        createCustomContentTypeSupport("a/b"), RepresentationType.ENTITY));
    assertFalse(ContentNegotiator.isSupported(new ContentType("a/b"),
        createCustomContentTypeSupport("x/y"), RepresentationType.ENTITY));
    assertTrue(ContentNegotiator.isSupported(new ContentType("a/b"),
        createCustomContentTypeSupport("a/b"), RepresentationType.BATCH));
    assertTrue(ContentNegotiator.isSupported(new ContentType("a/b"),
        createCustomContentTypeSupport("a/b"), RepresentationType.BINARY));
  }
  
  @Test
  public void checBatchkSupport() throws Exception {
    testContentNegotiation(new String[] { ACCEPT_CASE_MULTIPART_MIXED, null, ACCEPT_CASE_MULTIPART_MIXED, null },
        RepresentationType.BATCH);
  }
  
}