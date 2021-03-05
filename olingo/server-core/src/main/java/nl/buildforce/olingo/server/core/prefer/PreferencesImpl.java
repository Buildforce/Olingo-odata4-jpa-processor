/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.prefer;

import java.net.URI;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import nl.buildforce.olingo.commons.api.format.PreferenceName;
import nl.buildforce.olingo.server.api.prefer.Preferences;

/**
 * Provides access methods to the preferences set in the Prefer HTTP request
 * header as described in <a href="https://www.ietf.org/rfc/rfc7240.txt">RFC 7240</a>.
 * Preferences defined in the OData standard can be accessed with named methods.
 */
public class PreferencesImpl implements Preferences {

  //parameter name for odata.callback
  private static final String URL = "url";

  private final Map<String, Preference> preferences;

  public PreferencesImpl(Collection<String> preferHeaders) {
    preferences = PreferParser.parse(preferHeaders);
  }

  @Override
  public Preference getPreference(String name) {
    return preferences.get(name.toLowerCase(Locale.ROOT));
  }

  @Override
  public boolean hasAllowEntityReferences() {
    return preferences.containsKey(PreferenceName.ALLOW_ENTITY_REFERENCES.getName());
  }

  @Override
  public URI getCallback() {
    if (preferences.containsKey(PreferenceName.CALLBACK.getName())
        && preferences.get(PreferenceName.CALLBACK.getName()).getParameters() != null
        && preferences.get(PreferenceName.CALLBACK.getName()).getParameters().get(URL) != null) {
      try {
        return URI.create(preferences.get(PreferenceName.CALLBACK.getName()).getParameters().get(URL));
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
    return null;
  }

  @Override
  public boolean hasContinueOnError() {
    return preferences.containsKey(PreferenceName.CONTINUE_ON_ERROR.getName());
  }

  @Override
  public Integer getMaxPageSize() {
    return getNonNegativeIntegerPreference(PreferenceName.MAX_PAGE_SIZE.getName());
  }

  @Override
  public boolean hasTrackChanges() {
    return (preferences.containsKey(PreferenceName.TRACK_CHANGES.getName())
        ||preferences.containsKey(PreferenceName.TRACK_CHANGES_PREF.getName()));
  }

  @Override
  public Return getReturn() {
    if (preferences.containsKey(PreferenceName.RETURN.getName())) {
      String value = preferences.get(PreferenceName.RETURN.getName()).getValue();
      if (Return.REPRESENTATION.toString().toLowerCase(Locale.ROOT).equals(value)) {
        return Return.REPRESENTATION;
      } else if (Return.MINIMAL.toString().toLowerCase(Locale.ROOT).equals(value)) {
        return Return.MINIMAL;
      }
    }
    return null;
  }

  @Override
  public boolean hasRespondAsync() {
    return preferences.containsKey(PreferenceName.RESPOND_ASYNC.getName());
  }

  @Override
  public Integer getWait() {
    return getNonNegativeIntegerPreference(PreferenceName.WAIT.getName());
  }

  private Integer getNonNegativeIntegerPreference(String name) {
    if (preferences.containsKey(name) && preferences.get(name).getValue() != null) {
      try {
        int result = Integer.parseInt(preferences.get(name).getValue());
        return result < 0 ? null : result;
      } catch (NumberFormatException e) {
        return null;
      }
    }
    return null;
  }

}