/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.apply.Identity;

/**
 * Represents the identity transformation.
 */
public class IdentityImpl implements Identity {

  @Override
  public Kind getKind() {
    return Kind.IDENTITY;
  }
}
