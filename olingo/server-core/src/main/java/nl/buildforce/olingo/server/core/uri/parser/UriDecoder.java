/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import nl.buildforce.olingo.server.api.uri.queryoption.QueryOption;
import nl.buildforce.olingo.server.core.uri.queryoption.CustomQueryOptionImpl;

public class UriDecoder {

  private static final String ACCEPT_FORM_ENCODING = "odata-accept-forms-encoding";
  private static boolean formEncoding;
  
  public static boolean isFormEncoding() {
    return formEncoding;
  }

  /** Splits the path string at '/' characters and percent-decodes the resulting path segments. */
  protected static List<String> splitAndDecodePath(String path) throws UriParserSyntaxException {
    List<String> pathSegmentsDecoded = new ArrayList<>();
    for (String segment : split(path, '/')) {
      pathSegmentsDecoded.add(decode(segment));
    }
    return pathSegmentsDecoded;
  }

  /**
   * Splits the query-option string at '&' characters, the resulting parts at '=' characters,
   * and separately percent-decodes names and values of the resulting name-value pairs.
   * If there is no '=' character in an option, the whole option is considered as name.
   */
  protected static List<QueryOption> splitAndDecodeOptions(String queryOptionString)
      throws UriParserSyntaxException {
    List<QueryOption> queryOptions = new ArrayList<>();
    formEncoding = false;
    for (String option : split(queryOptionString, '&')) {
      int pos = option.indexOf('=');
      String name = pos >= 0 ? option.substring(0, pos)  : option;
      String text = pos >= 0 ? option.substring(pos + 1) : "";
      //OLINGO-846 We trim the query option text to be more lenient to wrong uri constructors
      if(ACCEPT_FORM_ENCODING.equals(name)){
        formEncoding = Boolean.parseBoolean(text);
      }
      queryOptions.add(new CustomQueryOptionImpl()
          .setName(decode(name).trim())
          .setText(decode(text).trim()));
    }
    return queryOptions;
  }

  /**
   * Splits the input string at the given character.
   * @param input string to split
   * @param c character at which to split
   * @return list of elements (can be empty)
   */
  private static List<String> split(String input, char c) {
    List<String> list = new LinkedList<>();

    int start = 0;
    int end;
    while ((end = input.indexOf(c, start)) >= 0) {
      list.add(input.substring(start, end));
      start = end + 1;
    }

    list.add(input.substring(start));

    return list;
  }

  public static String decode(String encoded) throws UriParserSyntaxException {
    try {
      return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    } catch (IllegalArgumentException e) {
      throw new UriParserSyntaxException("Wrong percent encoding!", e, UriParserSyntaxException.MessageKeys.SYNTAX);
    }
  }

}