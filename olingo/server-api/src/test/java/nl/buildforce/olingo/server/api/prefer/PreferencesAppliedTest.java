/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.prefer;

import static nl.buildforce.olingo.commons.api.format.PreferenceName.INCLUDE_ANNOTATIONS;
import static org.junit.Assert.assertEquals;

import nl.buildforce.olingo.server.api.prefer.Preferences.Return;
import org.junit.Test;

public class PreferencesAppliedTest {

  @Test
  public void empty() {
    assertEquals("", PreferencesApplied.with().build().toValueString());
  }

  @Test
  public void all() {
    assertEquals("odata.allow-entityreferences, odata.callback,"
        + " odata.continue-on-error, odata.include-annotations=\"*\", odata.maxpagesize=42,"
        + " odata.track-changes, return=representation, respond-async, wait=12345",
        PreferencesApplied.with().allowEntityReferences().callback().continueOnError()
        .preference(INCLUDE_ANNOTATIONS.getName()/*"odata.include-annotations"*/, "*").maxPageSize(42).trackChanges()
        .returnRepresentation(Return.REPRESENTATION).respondAsync().waitPreference(12345)
        .build().toValueString());
  }

  @Test
  public void caseSensitivity() {
    assertEquals("odata.include-annotations=\"*\", odata.maxpagesize=255",
        PreferencesApplied.with()
        .preference("OData.Include-Annotations", "*").maxPageSize(0xFF)
        .build().toValueString());
  }

  @Test
  public void multipleValues() {
    assertEquals("return=minimal, wait=1",
        PreferencesApplied.with()
        .returnRepresentation(Return.MINIMAL).returnRepresentation(Return.REPRESENTATION)
        .preference(null, null).preference(null, "nullValue")
        .waitPreference(1).waitPreference(2).waitPreference(3)
        .build().toValueString());
  }

  @Test
  public void quotedValue() {
    assertEquals("strangepreference=\"x\\\\y,\\\"abc\\\"z\"",
        PreferencesApplied.with().preference("strangePreference", "x\\y,\"abc\"z").build().toValueString());
  }

}