/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;

/**
 * Represents a single transformation from the system query option $apply.
 */
public interface ApplyItem {

  /** The kind of the transformation. */
  enum Kind {
    AGGREGATE,
    BOTTOM_TOP,
    COMPUTE,
    CONCAT,
    CUSTOM_FUNCTION,
    EXPAND,
    FILTER,
    GROUP_BY,
    IDENTITY,
    SEARCH
  }

  /**
   * Gets the kind of the transformation.
   * @return transformation kind
   */
  Kind getKind();
}