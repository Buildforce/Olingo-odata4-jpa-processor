/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption;


/**
 * Defines the supported system query options.
 */
public enum SystemQueryOptionKind {

  /**
   * @see FilterOption
   */
  FILTER("$filter"),

  /**
   * @see FormatOption
   */
  FORMAT("$format"),

  /**
   * @see ExpandOption
   */
  EXPAND("$expand"),

  /**
   * @see IdOption
   */
  ID("$id"),

  /**
   * @see CountOption
   */
  COUNT("$count"),

  /**
   * @see OrderByOption
   */
  ORDERBY("$orderby"),

  /**
   * @see SearchOption
   */
  SEARCH("$search"),

  /**
   * @see SelectOption
   */
  SELECT("$select"),

  /**
   * @see SkipOption
   */
  SKIP("$skip"),

  /**
   * @see SkipTokenOption
   */
  SKIPTOKEN("$skiptoken"),

  /**
   * @see TopOption
   */
  TOP("$top"),

  /**
   * @see LevelsExpandOption
   */
  LEVELS("$levels"),
  
  /**
   * @see DeltaTokenOption
   */
  DELTATOKEN("$deltatoken"),
  
  /**
   * @see ApplyOption
   */
  APPLY("$apply");

  private final String syntax;

  SystemQueryOptionKind(String syntax) {
    this.syntax = syntax;
  }

  /**
   * Converts the URI syntax to an enumeration value.
   * @param option option in the syntax used in the URI
   * @return system query option kind representing the given option
   *         (or <code>null</code> if the option does not represent a system query option)
   */
  public static SystemQueryOptionKind get(String option) {
    for (SystemQueryOptionKind kind : values()) {
      if (kind.syntax.equals(option)) {
        return kind;
      }
    }
    return null;
  }

  /**
   * @return URI syntax for this system query option
   */
  @Override
  public String toString() {
    return syntax;
  }

}