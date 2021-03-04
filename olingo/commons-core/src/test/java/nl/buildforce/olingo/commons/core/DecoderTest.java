/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core;

import static org.junit.Assert.assertEquals;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


import org.junit.Test;

/**
 *
 */
public class DecoderTest {
  private final Charset UTF8 = StandardCharsets.UTF_8;

  @Test
  public void asciiCharacters() {
    //assertNull(URLDecoder.decode(null, UTF8));

    String s = "azAZ019";
    assertEquals(s, URLDecoder.decode(s, UTF8));

    s = "\"\\`{}|";
    assertEquals(s, URLDecoder.decode(s, UTF8));
  }

  @Test
  public void asciiControl() {
    assertEquals("\u0000\b\t\n\r", URLDecoder.decode("%00%08%09%0a%0d", UTF8));
  }

  @Test
  public void asciiEncoded() {
    assertEquals("<>%&", URLDecoder.decode("%3c%3e%25%26", UTF8));
    assertEquals(":/?#[]@", URLDecoder.decode("%3a%2f%3f%23%5b%5d%40", UTF8));
    assertEquals(" !\"$'()*+,-.", URLDecoder.decode("%20%21%22%24%27%28%29%2A%2B%2C%2D%2E", UTF8));
  }

  @Test
  public void unicodeCharacters() {
    assertEquals("€", URLDecoder.decode("%E2%82%AC", UTF8));
    assertEquals("\uFDFC", URLDecoder.decode("%EF%B7%BC", UTF8));
  }

  @Test
  public void charactersOutsideBmp() {
    assertEquals(String.valueOf(Character.toChars(0x1F603)), URLDecoder.decode("%f0%9f%98%83", UTF8));
  }

  @Test
  public void correctCharacter() {
    assertEquals(" ä", URLDecoder.decode("%20ä", UTF8));
  }


  @Test(expected = IllegalArgumentException.class)
  public void wrongPercentNumber() {
    URLDecoder.decode("%-3", UTF8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void wrongPercentPercent() {
    URLDecoder.decode("%%a", UTF8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void unfinishedPercent() {
    URLDecoder.decode("%a", UTF8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullByte() {
    URLDecoder.decode("%\u0000ff", UTF8);
  }

}