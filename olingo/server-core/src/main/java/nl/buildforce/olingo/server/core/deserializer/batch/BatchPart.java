/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

public interface BatchPart {
  Header getHeaders();

  // --Commented out by Inspection (''21-03-06 17:24):boolean isStrict();

}