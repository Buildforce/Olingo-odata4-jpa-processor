/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.etag;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class ETagParserTest {

  private static final ETagHelperImpl eTagHelper = new ETagHelperImpl();

  @Test
  public void empty() {
    ETagInformation eTagInformation = eTagHelper.createETagInformation(null);
    assertFalse(eTagInformation.isAll());
    assertNotNull(eTagInformation.getETags());
    assertTrue(eTagInformation.getETags().isEmpty());
  }

  @Test
  public void loneStar() {
    ETagInformation eTagInformation = eTagHelper.createETagInformation(Collections.singleton("*"));
    assertTrue(eTagInformation.isAll());
    assertNotNull(eTagInformation.getETags());
    assertTrue(eTagInformation.getETags().isEmpty());
  }

  @Test
  public void starWins() {
    ETagInformation eTagInformation = eTagHelper.createETagInformation(Arrays.asList("\"ETag\"", "*"));
    assertTrue(eTagInformation.isAll());
    assertNotNull(eTagInformation.getETags());
    assertTrue(eTagInformation.getETags().isEmpty());
  }

  @Test
  public void starAsEtagAndEmptyEtag() {
    ETagInformation eTagInformation = eTagHelper.createETagInformation(
        Collections.singleton("\"*\", \"\""));
    assertFalse(eTagInformation.isAll());
    assertNotNull(eTagInformation.getETags());
    assertThat(eTagInformation.getETags().size(), equalTo(2));
    assertThat(eTagInformation.getETags(), hasItems("\"*\"", "\"\""));
  }

  @Test
  public void severalEtags() {
    ETagInformation eTagInformation = eTagHelper.createETagInformation(
        Arrays.asList("\"ETag1\"", "\"ETag2\",, , ,W/\"ETag3\", ,"));
    assertFalse(eTagInformation.isAll());
    assertNotNull(eTagInformation.getETags());
    assertThat(eTagInformation.getETags().size(), equalTo(3));
    assertThat(eTagInformation.getETags(), hasItems("\"ETag1\"", "\"ETag2\"", "W/\"ETag3\""));
  }

  @Test
  public void duplicateEtagValues() {
    ETagInformation eTagInformation = eTagHelper.createETagInformation(
        Arrays.asList("\"ETag1\"", "\"ETag2\", W/\"ETag1\", \"ETag1\""));
    assertFalse(eTagInformation.isAll());
    assertNotNull(eTagInformation.getETags());
    assertThat(eTagInformation.getETags().size(), equalTo(3));
    assertThat(eTagInformation.getETags(), hasItems("\"ETag1\"", "\"ETag2\"", "W/\"ETag1\""));
  }

  @Test
  public void specialCharacters() {
    ETagInformation eTagInformation = eTagHelper.createETagInformation(
        Collections.singleton("\"!#$%&'()*+,-./:;<=>?@[]^_`{|}~¡\u00FF\", \"ETag2\""));
    assertFalse(eTagInformation.isAll());
    assertNotNull(eTagInformation.getETags());
    assertThat(eTagInformation.getETags().size(), equalTo(2));
    assertThat(eTagInformation.getETags(), hasItems(
        "\"!#$%&'()*+,-./:;<=>?@[]^_`{|}~¡\u00FF\"", "\"ETag2\""));
  }

  @Test
  public void wrongFormat() {
    ETagInformation eTagInformation = eTagHelper.createETagInformation(
        Arrays.asList("\"ETag1\", ETag2", "w/\"ETag3\"", "W//\"ETag4\"", "W/ETag5",
            "\"\"ETag6\"\"", " \"ETag7\"\"ETag7\" ", "\"ETag8\" \"ETag8\"",
            "\"ETag 9\"", "\"ETag10\""));
    assertFalse(eTagInformation.isAll());
    assertNotNull(eTagInformation.getETags());
    assertThat(eTagInformation.getETags().size(), equalTo(2));
    assertThat(eTagInformation.getETags(), hasItems("\"ETag1\"", "\"ETag10\""));
  }

  @Test
  public void match() {
    assertFalse(eTagHelper.createETagInformation(Collections.emptySet()).isMatchedBy("\"ETag\""));
    assertFalse(eTagHelper.createETagInformation(Collections.singleton("\"ETag\"")).isMatchedBy(null));
    assertTrue(eTagHelper.createETagInformation(Collections.singleton("\"ETag\"")).isMatchedBy("\"ETag\""));
    assertTrue(eTagHelper.createETagInformation(Collections.singleton("*")).isMatchedBy("\"ETag\""));
    assertTrue(eTagHelper.createETagInformation(Collections.singleton("\"ETag\"")).isMatchedBy("W/\"ETag\""));
    assertTrue(eTagHelper.createETagInformation(Collections.singleton("W/\"ETag\"")).isMatchedBy("\"ETag\""));
    assertFalse(eTagHelper.createETagInformation(Collections.singleton("\"ETag\"")).isMatchedBy("W/\"ETag2\""));
    assertFalse(eTagHelper.createETagInformation(Collections.singleton("W/\"ETag\"")).isMatchedBy("\"ETag2\""));
    assertTrue(eTagHelper.createETagInformation(Arrays.asList("\"ETag1\",\"ETag2\"", "\"ETag3\",\"ETag4\""))
        .isMatchedBy("\"ETag4\""));
    assertFalse(eTagHelper.createETagInformation(Arrays.asList("\"ETag1\",\"ETag2\"", "\"ETag3\",\"ETag4\""))
        .isMatchedBy("\"ETag5\""));
  }
}
