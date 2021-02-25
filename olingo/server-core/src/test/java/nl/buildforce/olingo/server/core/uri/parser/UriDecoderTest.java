/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.server.api.uri.queryoption.QueryOption;
import org.junit.Test;

public class UriDecoderTest {

  @Test
  public void split() throws Exception {
    assertEquals(Collections.singletonList(""), UriDecoder.splitAndDecodePath(""));
    assertEquals(Arrays.asList("", ""), UriDecoder.splitAndDecodePath("/"));
    assertEquals(Collections.singletonList("a"), UriDecoder.splitAndDecodePath("a"));
    assertEquals(Arrays.asList("a", ""), UriDecoder.splitAndDecodePath("a/"));
    assertEquals(Arrays.asList("", "a"), UriDecoder.splitAndDecodePath("/a"));
    assertEquals(Arrays.asList("a", "b"), UriDecoder.splitAndDecodePath("a/b"));
    assertEquals(Arrays.asList("", "a", "b"), UriDecoder.splitAndDecodePath("/a/b"));
    assertEquals(Arrays.asList("", "a", "", "", "b", ""), UriDecoder.splitAndDecodePath("/a///b/"));
  }

  @Test
  public void path() throws Exception {
    assertEquals(Arrays.asList("a", "entitySet('/')", "bcd"),
        UriDecoder.splitAndDecodePath("a/entitySet('%2F')/b%63d"));
  }

  @Test
  public void options() throws Exception {
    checkOption("", "", "");

    checkOption("a", "a", "");
    checkOption("a=b", "a", "b");
    checkOption("=", "", "");
    checkOption("=b", "", "b");

    checkOption("a&c", "a", "");
    checkOption("a&c", "c", "");

    checkOption("a=b&c", "a", "b");
    checkOption("a=b&c", "c", "");

    checkOption("a=b&c=d", "a", "b");
    checkOption("a=b&c=d", "c", "d");

    checkOption("=&=", "", "");
    assertEquals(2, UriDecoder.splitAndDecodeOptions("=&=").size());
    assertEquals(13, UriDecoder.splitAndDecodeOptions("&&&&&&&&&&&&").size());

    checkOption("=&c=d", "", "");
    checkOption("=&c=d", "c", "d");

    checkOption("a%62c=d%65f", "abc", "def");
    checkOption("a='%26%3D'", "a", "'&='");
  }

  @Test(expected = UriParserSyntaxException.class)
  public void wrongPercentEncoding() throws Exception {
    UriDecoder.splitAndDecodePath("%wrong");
  }

  private void checkOption(String query, String name, String value)
      throws UriParserSyntaxException {
    List<QueryOption> options = UriDecoder.splitAndDecodeOptions(query);
    for (QueryOption option : options) {
      if (option.getName().equals(name)) {
        assertEquals(value, option.getText());
        return;
      }
    }
    fail("Option " + name + " not found!");
  }
}
