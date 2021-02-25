/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

/**
 * Enumeration of supported methods
 * For the semantic of these methods please see the ODATA specification for URL conventions
 */
public enum MethodKind {
  CONTAINS("contains"),
  STARTSWITH("startswith"),
  ENDSWITH("endswith"),
  LENGTH("length"),
  INDEXOF("indexof"),
  SUBSTRING("substring"),
  TOLOWER("tolower"),
  TOUPPER("toupper"),
  TRIM("trim"),
  CONCAT("concat"),
  YEAR("year"),
  MONTH("month"),
  DAY("day"),
  HOUR("hour"),
  MINUTE("minute"),
  SECOND("second"),
  FRACTIONALSECONDS("fractionalseconds"),
  TOTALSECONDS("totalseconds"), DATE("date"), TIME("time"),
  TOTALOFFSETMINUTES("totaloffsetminutes"),
  MINDATETIME("mindatetime"),
  MAXDATETIME("maxdatetime"),
  NOW("now"),
  ROUND("round"),
  FLOOR("floor"),
  CEILING("ceiling"),
  GEODISTANCE("geo.distance"),
  GEOLENGTH("geo.length"),
  GEOINTERSECTS("geo.intersects"),
  CAST("cast"),
  ISOF("isof"),
  SUBSTRINGOF("substringof");

  private final String syntax;

  /**
   * Constructor for enumeration value
   * @param syntax used in the URI
   */
  MethodKind(String syntax) {
    this.syntax = syntax;
  }

  @Override
  /*
    @return URI syntax for that operator kind
   */
  public String toString() {
    return syntax;
  }

  /**
   * URI syntax to enumeration value
   * @param method Method in the syntax used in the URI
   * @return Method kind which represents the given syntax
   */
  public static MethodKind get(String method) {
    for (MethodKind op : MethodKind.values()) {

      if (op.toString().equals(method)) {
        return op;
      }
    }
    return null;
  }

}