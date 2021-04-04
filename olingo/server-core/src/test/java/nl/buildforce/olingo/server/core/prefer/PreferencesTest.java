/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.prefer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;

import nl.buildforce.olingo.server.api.prefer.Preferences;
import nl.buildforce.olingo.server.api.prefer.Preferences.Return;
import org.junit.Test;

public class PreferencesTest {

  @Test
  public void empty() {
    Preferences preferences = new PreferencesImpl(null);
/*
    assertFalse(preferences.hasAllowEntityReferences());
    assertNull(preferences.getCallback());
    assertFalse(preferences.hasContinueOnError());
    assertNull(preferences.getMaxPageSize());
    assertFalse(preferences.hasTrackChanges());
*/
    assertNull(preferences.getReturn());
//    assertFalse(preferences.hasRespondAsync());
//    assertNull(preferences.getWait());
  }

  @Test
  public void all() {
    Preferences preferences = new PreferencesImpl(Collections.singleton(
        "odata.allow-entityreferences, odata.callback;url=\"callbackURI\","
            + "odata.continue-on-error, odata.include-annotations=\"*\", odata.maxpagesize=42,"
            + "odata.track-changes, return=representation, respond-async, wait=12345"));
/*
    assertTrue(preferences.hasAllowEntityReferences());
    assertEquals(URI.create("callbackURI"), preferences.getCallback());
    assertNotNull(preferences.getPreference("odata.callback"));
    assertNull(preferences.getPreference("odata.callback").getValue());
    assertEquals("callbackURI", preferences.getPreference("odata.callback").getParameters().get("url"));
    assertTrue(preferences.hasContinueOnError());
    assertEquals("*", preferences.getPreference("odata.Include-Annotations").getValue());
    assertEquals(Integer.valueOf(42), preferences.getMaxPageSize());
    assertEquals("42", preferences.getPreference("odata.MaxPageSize").getValue());
    assertTrue(preferences.hasTrackChanges());
*/
    assertEquals(Return.REPRESENTATION, preferences.getReturn());
    // assertTrue(preferences.hasRespondAsync());
    // assertEquals(Integer.valueOf(12345), preferences.getWait());
  }

  @Test
  public void caseSensitivity() {
    Preferences preferences = new PreferencesImpl(Collections.singleton(
        "OData.Callback;URL=\"callbackURI\", return=REPRESENTATION, Wait=42"));
    // assertEquals(URI.create("callbackURI"), preferences.getCallback());
    assertNull(preferences.getReturn());
    // assertEquals(Integer.valueOf(42), preferences.getWait());
  }

  @Test
  public void multipleValues() {
    Preferences preferences = new PreferencesImpl(Collections.singleton(
        ",return=minimal, ,, return=representation, wait=1, wait=2, wait=3,"));
    assertEquals(Return.MINIMAL, preferences.getReturn());
    // assertEquals(Integer.valueOf(1), preferences.getWait());
  }

  @Test
  public void multipleValuesDifferentHeaders() {
    Preferences preferences = new PreferencesImpl(Arrays.asList(
        null, "",
        "return=representation, wait=1",
        "return=minimal, wait=2",
        "wait=3"));
    assertEquals(Return.REPRESENTATION, preferences.getReturn());
    //assertEquals(Integer.valueOf(1), preferences.getWait());
  }

  /*
  @Test
  public void multipleParameters() {
    Preferences preferences = new PreferencesImpl(Collections.singleton(
        "preference=a;;b=c; d = e; f;; ; g; h=\"i\";, wait=42"));
    Preference preference = preferences.getPreference("preference");
    assertEquals("a", preference.getValue());
    Map<String, String> parameters = preference.getParameters();
    assertEquals(5, parameters.size());
    assertEquals("c", parameters.get("b"));
    assertEquals("e", parameters.get("d"));
    assertTrue(parameters.containsKey("f"));
    assertNull(parameters.get("f"));
    assertTrue(parameters.containsKey("g"));
    assertNull(parameters.get("g"));
    assertEquals("i", parameters.get("h"));
    assertEquals(Integer.valueOf(42), preferences.getWait());
  }


  @Test
  public void quotedValue() {
    Preferences preferences = new PreferencesImpl(Collections.singleton(
        "strangePreference=\"x\\\\y,\\\"abc\\\"z\", wait=42"));
    assertEquals("x\\y,\"abc\"z", preferences.getPreference("strangePreference").getValue());
    assertEquals(Integer.valueOf(42), preferences.getWait());
  }

  @Test
  public void specialCharacters() {
    Preferences preferences = new PreferencesImpl(Collections.singleton(
        "!#$%&'*+-.^_`|~ = \"!#$%&'()*+,-./:;<=>?@[]^_`{|}~¡\u00FF\", wait=42"));
    assertEquals("!#$%&'()*+,-./:;<=>?@[]^_`{|}~¡\u00FF", preferences.getPreference("!#$%&'*+-.^_`|~").getValue());
    assertEquals(Integer.valueOf(42), preferences.getWait());
  }
*/

  @Test
  public void wrongContent() {
    Preferences preferences = new PreferencesImpl(Arrays.asList(
        "odata.callback;url=\":\"",
        "odata.maxpagesize=12345678901234567890",
        "return=something",
        "wait=-1"));
/*
    assertNull(preferences.getCallback());
    assertEquals(":", preferences.getPreference("odata.callback").getParameters().get("url"));
    assertNull(preferences.getMaxPageSize());
    assertEquals("12345678901234567890", preferences.getPreference("odata.maxpagesize").getValue());
*/
    assertNull(preferences.getReturn());
    // assertEquals("something", preferences.getPreference("return").getValue());
    // assertNull(preferences.getWait());
    // assertEquals("-1", preferences.getPreference("wait").getValue());
  }

/*  @Test
  public void wrongFormat() {
    Preferences preferences = new PreferencesImpl(Arrays.asList(
        "return=, wait=1",
        "return=;, wait=2",
        "return=representation=, wait=3",
        "return=\"representation\"respond-async, wait=4",
        "respond-async[], wait=5",
        "odata.callback;=, wait=6",
        "odata.callback;url=, wait=7",
        "odata.callback;[], wait=8",
        "odata.callback;url=\"url\"parameter, wait=9",
        "wait=10"));
    assertEquals(Integer.valueOf(10), preferences.getWait());
  }*/

}