/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.prefer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.buildforce.olingo.server.api.prefer.Preferences.Preference;

/**
 * <p>Parses the values of <code>Prefer</code> HTTP header fields.</p>
 * <p>See <a href="https://www.ietf.org/rfc/rfc7240.txt">RFC 7240</a> for details;
 * there the following grammar is defined:</p>
 * <pre>
 * Prefer = "Prefer" ":" 1#preference
 * preference = token [ BWS "=" BWS word ] *( OWS ";" [ OWS parameter ] )
 * parameter = token [ BWS "=" BWS word ]
 * token = 1*tchar
 * tchar = "!" / "#" / "$" / "%" / "&" / "'" / "*"
 * / "+" / "-" / "." / "^" / "_" / "`" / "|" / "~" / DIGIT / ALPHA
 * word = token / quoted-string
 * quoted-string = DQUOTE *( qdtext / quoted-pair ) DQUOTE
 * qdtext = HTAB / SP / %x21 / %x23-5B / %x5D-7E / %x80-FF
 * quoted-pair = "\" ( HTAB / SP / %x21-7E / %x80-FF )
 * OWS = *( SP / HTAB ) ; optional whitespace
 * BWS = OWS ; "bad" whitespace
 * </pre>
 * <p>Values with illegal syntax do not contribute to the result but no exception is thrown.</p>
 */
public class PreferParser {

  private static final String TOKEN = "(?:[-!#$%&'*+.^_`|~]|\\w)+";
  private static final String QUOTED_STRING = "(?:\"(?:[\\t !#-\\[\\]-~\\x80-\\xFF]|"
      + "(?:\\\\[\\t !-~\\x80-\\xFF]))*\")";
  private static final String NAMED_VALUE =
      "(" + TOKEN + ")(?:\\s*=\\s*(" + TOKEN + "|" + QUOTED_STRING + "))?";
  private static final Pattern PREFERENCE = Pattern.compile("\\s*(,\\s*)+|"
      + "(?:" + NAMED_VALUE + "((?:\\s*;\\s*(?:" + NAMED_VALUE + ")?)*))");
  private static final Pattern PARAMETER = Pattern.compile("\\s*(;\\s*)+|(?:" + NAMED_VALUE + ")");

  private PreferParser() {
    // Private constructor for utility classes
  }

  protected static Map<String, Preference> parse(Collection<String> values) {
    if (values == null || values.isEmpty()) {
      return Collections.emptyMap();
    }

    Map<String, Preference> result = new HashMap<>();
    for (String value : values) {
      if (value != null && !value.isEmpty()) {
        parse(value, result);
      }
    }
    return result;
  }

  private static void parse(String value, Map<String, Preference> result) {
    Map<String, Preference> partResult = new HashMap<>();
    String separator = "";
    int start = 0;
    Matcher matcher = PREFERENCE.matcher(value.trim());
    while (matcher.find() && matcher.start() == start) {
      start = matcher.end();
      if (matcher.group(1) != null) {
        separator = matcher.group(1);
      } else if (separator != null) {
        String name = matcher.group(2).toLowerCase(Locale.ROOT);
        // RFC 7240:
        // If any preference is specified more than once, only the first instance is to be
        // considered. All subsequent occurrences SHOULD be ignored without signaling
        // an error or otherwise altering the processing of the request.
        if (!partResult.containsKey(name)) {
          String preferenceValue = getValue(matcher.group(3));
          Map<String, String> parameters =
              matcher.group(4) == null || matcher.group(4).isEmpty() ? null :
                parseParameters(matcher.group(4));
          partResult.put(name, new Preference(preferenceValue, parameters));
        }
        separator = null;
      } else {
        return;
      }
    }
    if (matcher.hitEnd()) {
      // Here we also have to keep already existing preferences.
      for (Map.Entry<String, Preference> entry : partResult.entrySet()) {
        if (!result.containsKey(entry.getKey())) {
          result.put(entry.getKey(), entry.getValue());
        }
      }
    }
  }

  private static Map<String, String> parseParameters(String parameters) {
    Map<String, String> result = new HashMap<>();
    String separator = "";
    int start = 0;
    Matcher matcher = PARAMETER.matcher(parameters.trim());
    while (matcher.find() && matcher.start() == start) {
      start = matcher.end();
      if (matcher.group(1) != null) {
        separator = matcher.group(1);
      } else if (separator != null) {
        String name = matcher.group(2).toLowerCase(Locale.ROOT);
        // We have to keep already existing parameters.
        if (!result.containsKey(name)) {
          result.put(name, getValue(matcher.group(3)));
        }
        separator = null;
      } else {
        return null;
      }
    }
    return matcher.hitEnd() ? Collections.unmodifiableMap(result) : null;
  }

  private static String getValue(String value) {
    if (value == null) {
      return null;
    }
    String result = value;
    if (value.startsWith("\"")) {
      result = value.substring(1, value.length() - 1);
    }
    // Unquote backslash-quoted characters.
    if (result.indexOf('\\') >= 0) {
      result = result.replaceAll("\\\\(.)", "$1");
    }
    return result;
  }

}