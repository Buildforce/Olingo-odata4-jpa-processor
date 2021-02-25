/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

/**
 * Represents the system query option $format
 * For example: http://.../entitySet?$format=json
 */
public interface FormatOption extends SystemQueryOption {

  // TODO planned: define best representation for format to enable user defined formats
  String getFormat();
}
