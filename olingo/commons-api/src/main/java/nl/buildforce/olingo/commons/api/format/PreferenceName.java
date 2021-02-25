/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.format;

/**
 * Names of preferences defined in the OData standard.
 */
public enum PreferenceName {

  ALLOW_ENTITY_REFERENCES("odata.allow-entityreferences"),
  CALLBACK("odata.callback"),
  CONTINUE_ON_ERROR("odata.continue-on-error"),
  INCLUDE_ANNOTATIONS("odata.include-annotations"),
  MAX_PAGE_SIZE("odata.maxpagesize"),
  TRACK_CHANGES("odata.track-changes"),
  TRACK_CHANGES_PREF("track-changes"),
  RETURN("return"),
  RESPOND_ASYNC("respond-async"),
  WAIT("wait"),
  RETURN_CONTENT("return-content"),
  RETURN_NO_CONTENT("return-no-content"),
  KEY_AS_SEGMENT("KeyAsSegment");

  private final String preferenceName;

  PreferenceName(String preferenceName) {
    this.preferenceName = preferenceName;
  }

  public String getName() {
    return preferenceName;
  }

  @Override
  public String toString() {
    return getName();
  }
}
