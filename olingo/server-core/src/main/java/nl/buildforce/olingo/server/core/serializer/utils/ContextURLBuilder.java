/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer.utils;

import java.net.URI;

import nl.buildforce.olingo.commons.api.Constants;
import nl.buildforce.olingo.commons.api.data.ContextURL;
import nl.buildforce.olingo.commons.core.Encoder;

/**
 * Builder to build a context URL (as defined in the <a
 * href="http://docs.oasis-open.org/odata/odata/v4.0/os/part1-protocol/odata-v4.0-os-part1-protocol.html#_Toc372793655">
 * protocol specification</a>).
 */
public final class ContextURLBuilder {
  
  private ContextURLBuilder() { /* private ctor for helper class */}

  public static URI create(ContextURL contextURL) {
    StringBuilder result = new StringBuilder();
    if (contextURL.getServiceRoot() != null) {
      result.append(contextURL.getServiceRoot());
    } else if (contextURL.getODataPath() != null) {
      String oDataPath = contextURL.getODataPath();
      char[] chars = oDataPath.toCharArray();
      for (int i = 1; i < chars.length - 1; i++) {
        if (chars[i] == '/' && chars[i - 1] != '/') {
          result.append("../");
        }
      }
    }

    result.append(Constants.METADATA);
    if (contextURL.getEntitySetOrSingletonOrType() != null) {
      result.append('#');
      if (contextURL.isCollection()) {
        result.append("Collection(")
            .append(Encoder.encode(contextURL.getEntitySetOrSingletonOrType()))
            .append(")");
      } else {
        result.append(Encoder.encode(contextURL.getEntitySetOrSingletonOrType()));
      }
    }
    if (contextURL.getDerivedEntity() != null) {
      if (contextURL.getEntitySetOrSingletonOrType() == null) {
        throw new IllegalArgumentException("ContextURL: Derived Type without anything to derive from!");
      }
      result.append('/').append(Encoder.encode(contextURL.getDerivedEntity()));
    }
    if (contextURL.getKeyPath() != null) {
      result.append('(').append(contextURL.getKeyPath()).append(')');
    }
    if (contextURL.getNavOrPropertyPath() != null) {
      if (contextURL.getServiceRoot() == null || 
          !contextURL.getServiceRoot().isAbsolute()) {
        String[] paths = contextURL.getNavOrPropertyPath().split("/");
        for (String path : paths) {
          result.insert(0, "../");
        }
      }
      result.append('/').append(contextURL.getNavOrPropertyPath());
    }
    if (contextURL.getSelectList() != null) {
      result.append('(').append(contextURL.getSelectList()).append(')');
    }
    if (contextURL.isReference()) {
      if (contextURL.getServiceRoot() == null ||
          !contextURL.getServiceRoot().isAbsolute()) {
        result.insert(0, "../");
      }
      if (contextURL.getEntitySetOrSingletonOrType() != null) {
        throw new IllegalArgumentException("ContextURL: $ref with Entity Set");
      }
      if (contextURL.isCollection()) {
        result.append('#')
            .append("Collection(")
            .append(ContextURL.Suffix.REFERENCE.getRepresentation())
            .append(")");
      } else {
        result.append('#').append(ContextURL.Suffix.REFERENCE.getRepresentation());
      }
    } else if (contextURL.getSuffix() != null) {
      if (contextURL.getEntitySetOrSingletonOrType() == null) {
        throw new IllegalArgumentException("ContextURL: Suffix without preceding Entity Set!");
      }
      result.append('/').append(contextURL.getSuffix().getRepresentation());
    }
    return URI.create(result.toString());
  }
}
