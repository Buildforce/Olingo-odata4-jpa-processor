/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser;

/**
 * <p>Simple OData URI tokenizer that works on a given string by keeping an index.</p>
 * <p>As far as feasible, it tries to work on character basis, assuming this to be faster than string operations.
 * Since only the index is "moved", backing out while parsing a token is easy and used throughout.
 * There is intentionally no method to push back tokens (although it would be easy to add such a method)
 * because this tokenizer should behave like a classical token-consuming tokenizer.
 * There is, however, the possibility to save the current state and return to it later.</p>
 * <p>Whitespace is not an extra token but consumed with the tokens that require whitespace.
 * Optional whitespace is not supported.</p>
 */
public class UriTokenizer {

  public enum TokenKind {
    EOF, // signals the end of the string to be parsed

    // constant-value tokens (convention: uppercase)
    REF,
    VALUE,
    COUNT,
    CROSSJOIN,
    ROOT,
    IT,

    APPLY, // for the aggregation extension
    EXPAND,
    FILTER,
    LEVELS,
    ORDERBY,
    SEARCH,
    SELECT,
    SKIP,
    TOP,

    ANY,
    ALL,

    OPEN,
    CLOSE,
    COMMA,
    SEMI,
    COLON,
    DOT,
    SLASH,
    EQ,
    STAR,
    PLUS,

    NULL,
    MAX,

    AVERAGE, // for the aggregation extension
    COUNTDISTINCT, // for the aggregation extension
    IDENTITY, // for the aggregation extension
    MIN, // for the aggregation extension
    SUM, // for the aggregation extension
    ROLLUP_ALL, // for the aggregation extension

    // variable-value tokens (convention: mixed case)
    ODataIdentifier,
    QualifiedName,
    ParameterAliasName,

    BooleanValue,
    StringValue,
    IntegerValue,
    GuidValue,
    DateValue,
    DateTimeOffsetValue,
    TimeOfDayValue,
    DecimalValue,
    DoubleValue,
    DurationValue,
    BinaryValue,
    EnumValue,

    /*GeographyPoint,
    GeometryPoint,
    GeographyLineString,
    GeometryLineString,
    GeographyPolygon,
    GeometryPolygon,
    GeographyMultiPoint,
    GeometryMultiPoint,
    GeographyMultiLineString,
    GeometryMultiLineString,
    GeographyMultiPolygon,
    GeometryMultiPolygon,
    GeographyCollection,
    GeometryCollection,*/

    jsonArrayOrObject,

    Word,
    Phrase,

    OrOperatorSearch,
    AndOperatorSearch,
    NotOperatorSearch,

    OrOperator,
    AndOperator,
    EqualsOperator,
    NotEqualsOperator,
    GreaterThanOperator,
    GreaterThanOrEqualsOperator,
    LessThanOperator,
    LessThanOrEqualsOperator,
    HasOperator,
    InOperator,
    AddOperator,
    SubOperator,
    MulOperator,
    DivOperator,
    ModOperator,
    MinusOperator,
    NotOperator,

    AsOperator, // for the aggregation extension
    FromOperator, // for the aggregation extension
    WithOperator, // for the aggregation extension

    CastMethod,
    CeilingMethod,
    ConcatMethod,
    ContainsMethod,
    DateMethod,
    DayMethod,
    EndswithMethod,
    FloorMethod,
    FractionalsecondsMethod,
/*
    GeoDistanceMethod,
    GeoIntersectsMethod,
    GeoLengthMethod,
*/
    HourMethod,
    IndexofMethod,
    IsofMethod,
    LengthMethod,
    MaxdatetimeMethod,
    MindatetimeMethod,
    MinuteMethod,
    MonthMethod,
    NowMethod,
    RoundMethod,
    SecondMethod,
    StartswithMethod,
    SubstringMethod,
    TimeMethod,
    TolowerMethod,
    TotaloffsetminutesMethod,
    TotalsecondsMethod,
    ToupperMethod,
    TrimMethod,
    YearMethod,
    SubstringofMethod,

    IsDefinedMethod, // for the aggregation extension

    AggregateTrafo, // for the aggregation extension
    BottomCountTrafo, // for the aggregation extension
    BottomPercentTrafo, // for the aggregation extension
    BottomSumTrafo, // for the aggregation extension
    ComputeTrafo, // for the aggregation extension
    ExpandTrafo, // for the aggregation extension
    FilterTrafo, // for the aggregation extension
    GroupByTrafo, // for the aggregation extension
    SearchTrafo, // for the aggregation extension
    TopCountTrafo, // for the aggregation extension
    TopPercentTrafo, // for the aggregation extension
    TopSumTrafo, // for the aggregation extension

    RollUpSpec, // for the aggregation extension

    AscSuffix,
    DescSuffix
  }

  private final String parseString;

  private int startIndex;
  private int index;

  private int savedStartIndex;
  private int savedIndex;

  public UriTokenizer(String parseString) {
    this.parseString = parseString == null ? "" : parseString;
  }

  /**
   * Save the current state.
   * @see #returnToSavedState()
   */
  public void saveState() {
    savedStartIndex = startIndex;
    savedIndex = index;
  }

  /**
   * Return to the previously saved state.
   * @see #saveState()
   */
  public void returnToSavedState() {
    startIndex = savedStartIndex;
    index = savedIndex;
  }

  /** Returns the string value corresponding to the last successful {@link #next(TokenKind)} call. */
  public String getText() {
    return parseString.substring(startIndex, index);
  }

  /**
   * Tries to find a token of the given token kind at the current index.
   * The order in which this method is called with different token kinds is important,
   * not only for performance reasons but also if tokens can start with the same characters
   * (e.g., a qualified name starts with an OData identifier).
   * The index is advanced to the end of this token if the token is found.
   * @param allowedTokenKind the kind of token to expect
   * @return <code>true</code> if the token is found; <code>false</code> otherwise
   * @see #getText()
   */
  public boolean next(TokenKind allowedTokenKind) {
    if (allowedTokenKind == null) {
      return false;
    }

    boolean found = false;
    int previousIndex = index;
      found = switch (allowedTokenKind) {
          case EOF -> index >= parseString.length();

          // Constants
          case REF -> nextConstant("$ref");
          case VALUE -> nextConstant("$value");
          case COUNT -> nextConstant("$count");
          case CROSSJOIN -> nextConstant("$crossjoin");
          case ROOT -> nextConstant("$root");
          case IT -> nextConstant("$it");
          case APPLY -> nextConstant("$apply");
          case EXPAND -> nextConstant("$expand");
          case FILTER -> nextConstant("$filter");
          case LEVELS -> nextConstant("$levels");
          case ORDERBY -> nextConstant("$orderby");
          case SEARCH -> nextConstant("$search");
          case SELECT -> nextConstant("$select");
          case SKIP -> nextConstant("$skip");
          case TOP -> nextConstant("$top");
          case ANY -> nextConstant("any");
          case ALL -> nextConstant("all");
          case OPEN -> nextCharacter('(');
          case CLOSE -> nextCharacter(')');
          case COMMA -> nextCharacter(',');
          case SEMI -> nextCharacter(';');
          case COLON -> nextCharacter(':');
          case DOT -> nextCharacter('.');
          case SLASH -> nextCharacter('/');
          case EQ -> nextCharacter('=');
          case STAR -> nextCharacter('*');
          case PLUS -> nextCharacter('+');
          case NULL -> nextConstant("null");
          case MAX -> nextConstant("max");
          case AVERAGE -> nextConstant("average");
          case COUNTDISTINCT -> nextConstant("countdistinct");
          case IDENTITY -> nextConstant("identity");
          case MIN -> nextConstant("min");
          case SUM -> nextConstant("sum");
          case ROLLUP_ALL -> nextConstant("$all");

          // Identifiers
          case ODataIdentifier -> nextODataIdentifier();
          case QualifiedName -> nextQualifiedName();
          case ParameterAliasName -> nextParameterAliasName();

          // Primitive Values
          case BooleanValue -> nextBooleanValue();
          case StringValue -> nextStringValue();
          case IntegerValue -> nextIntegerValue(true);
          case GuidValue -> nextGuidValue();
          case DateValue -> nextDateValue();
          case DateTimeOffsetValue -> nextDateTimeOffsetValue();
          case TimeOfDayValue -> nextTimeOfDayValue();
          case DecimalValue -> nextDecimalValue();
          case DoubleValue -> nextDoubleValue();
          case DurationValue -> nextDurationValue();
          case BinaryValue -> nextBinaryValue();
          case EnumValue -> nextEnumValue();

          // Complex or Collection Value
          case jsonArrayOrObject -> nextJsonArrayOrObject();

          // Search
          case Word -> nextWord();
          case Phrase -> nextPhrase();

          // Operators in Search Expressions
          case OrOperatorSearch -> nextBinaryOperator("OR");
          case AndOperatorSearch -> nextAndOperatorSearch();
          case NotOperatorSearch -> nextUnaryOperator("NOT");

          // Operators
          case OrOperator -> nextBinaryOperator("or");
          case AndOperator -> nextBinaryOperator("and");
          case EqualsOperator -> nextBinaryOperator("eq");
          case NotEqualsOperator -> nextBinaryOperator("ne");
          case GreaterThanOperator -> nextBinaryOperator("gt");
          case GreaterThanOrEqualsOperator -> nextBinaryOperator("ge");
          case LessThanOperator -> nextBinaryOperator("lt");
          case LessThanOrEqualsOperator -> nextBinaryOperator("le");
          case HasOperator -> nextBinaryOperator("has");
          case InOperator -> nextBinaryOperator("in");
          case AddOperator -> nextBinaryOperator("add");
          case SubOperator -> nextBinaryOperator("sub");
          case MulOperator -> nextBinaryOperator("mul");
          case DivOperator -> nextBinaryOperator("div");
          case ModOperator -> nextBinaryOperator("mod");
          case MinusOperator ->
                  // To avoid unnecessary minus operators for negative numbers, we have to check what follows the minus sign.
                  nextCharacter('-') && !nextDigit() && !nextConstant("INF");
          case NotOperator -> nextUnaryOperator("not");

          // Operators for the aggregation extension
          case AsOperator -> nextBinaryOperator("as");
          case FromOperator -> nextBinaryOperator("from");
          case WithOperator -> nextBinaryOperator("with");

          // Methods
          case CastMethod -> nextMethod("cast");
          case CeilingMethod -> nextMethod("ceiling");
          case ConcatMethod -> nextMethod("concat");
          case ContainsMethod -> nextMethod("contains");
          case DateMethod -> nextMethod("date");
          case DayMethod -> nextMethod("day");
          case EndswithMethod -> nextMethod("endswith");
          case FloorMethod -> nextMethod("floor");
          case FractionalsecondsMethod -> nextMethod("fractionalseconds");
          case HourMethod -> nextMethod("hour");
          case IndexofMethod -> nextMethod("indexof");
          case IsofMethod -> nextMethod("isof");
          case LengthMethod -> nextMethod("length");
          case MaxdatetimeMethod -> nextMethod("maxdatetime");
          case MindatetimeMethod -> nextMethod("mindatetime");
          case MinuteMethod -> nextMethod("minute");
          case MonthMethod -> nextMethod("month");
          case NowMethod -> nextMethod("now");
          case RoundMethod -> nextMethod("round");
          case SecondMethod -> nextMethod("second");
          case StartswithMethod -> nextMethod("startswith");
          case SubstringMethod -> nextMethod("substring");
          case TimeMethod -> nextMethod("time");
          case TolowerMethod -> nextMethod("tolower");
          case TotaloffsetminutesMethod -> nextMethod("totaloffsetminutes");
          case TotalsecondsMethod -> nextMethod("totalseconds");
          case ToupperMethod -> nextMethod("toupper");
          case TrimMethod -> nextMethod("trim");
          case YearMethod -> nextMethod("year");
          case SubstringofMethod -> nextMethod("substringof");

          // Method for the aggregation extension
          case IsDefinedMethod -> nextMethod("isdefined");

          // Transformations for the aggregation extension
          case AggregateTrafo -> nextMethod("aggregate");
          case BottomCountTrafo -> nextMethod("bottomcount");
          case BottomPercentTrafo -> nextMethod("bottompercent");
          case BottomSumTrafo -> nextMethod("bottomsum");
          case ComputeTrafo -> nextMethod("compute");
          case ExpandTrafo -> nextMethod("expand");
          case FilterTrafo -> nextMethod("filter");
          case GroupByTrafo -> nextMethod("groupby");
          case SearchTrafo -> nextMethod("search");
          case TopCountTrafo -> nextMethod("topcount");
          case TopPercentTrafo -> nextMethod("toppercent");
          case TopSumTrafo -> nextMethod("topsum");

          // Roll-up specification for the aggregation extension
          case RollUpSpec -> nextMethod("rollup");

          // Suffixes
          case AscSuffix -> nextSuffix("asc");
          case DescSuffix -> nextSuffix("desc");
      };

    if (found) {
      startIndex = previousIndex;
    } else {
      index = previousIndex;
    }
    return found;
  }

  /**
   * Moves past the given string constant if found; otherwise leaves the index unchanged.
   * @return whether the constant has been found at the current index
   */
  private boolean nextConstant(String constant) {
    if (parseString.startsWith(constant, index)) {
      index += constant.length();
      return true;
    } else {
      return false;
    }
  }

  /**
   * Moves past the given string constant, ignoring case, if found; otherwise leaves the index unchanged.
   * @return whether the constant has been found at the current index
   */
  private boolean nextConstantIgnoreCase(String constant) {
    int length = constant.length();
    if (index + length <= parseString.length()
        && constant.equalsIgnoreCase(parseString.substring(index, index + length))) {
      index += length;
      return true;
    } else {
      return false;
    }
  }

  /**
   * Moves past the given character if found; otherwise leaves the index unchanged.
   * @return whether the given character has been found at the current index
   */
  private boolean nextCharacter(char character) {
    if (index < parseString.length() && parseString.charAt(index) == character) {
      index++;
      return true;
    } else {
      return false;
    }
  }

  /**
   * Moves past the next character if it is in the given character range;
   * otherwise leaves the index unchanged.
   * @return whether the given character has been found at the current index
   */
  private boolean nextCharacterRange(char from, char to) {
    if (index < parseString.length()) {
      char code = parseString.charAt(index);
      if (code >= from && code <= to) {
        index++;
        return true;
      }
    }
    return false;
  }

  /**
   * Moves past a digit character ('0' to '9') if found; otherwise leaves the index unchanged.
   * @return whether a digit character has been found at the current index
   */
  private boolean nextDigit() {
    return nextCharacterRange('0', '9');
  }

  /**
   * Moves past a hexadecimal digit character ('0' to '9', 'A' to 'F', or 'a' to 'f') if found;
   * otherwise leaves the index unchanged.
   * @return whether a hexadecimal digit character has been found at the current index
   */
  private boolean nextHexDigit() {
    return nextCharacterRange('0', '9') || nextCharacterRange('A', 'F') || nextCharacterRange('a', 'f');
  }

  /**
   * Moves past a base64 character ('0' to '9', 'A' to 'Z', 'a' to 'z', '-', or '_') if found;
   * otherwise leaves the index unchanged.
   * @return whether a base64 character has been found at the current index
   */
  private boolean nextBase64() {
    return nextCharacterRange('0', '9') || nextCharacterRange('A', 'Z') || nextCharacterRange('a', 'z')
        || nextCharacter('-') || nextCharacter('_');
  }

  /**
   * Moves past a sign character ('+' or '-') if found; otherwise leaves the index unchanged.
   * @return whether a sign character has been found at the current index
   */
  private boolean nextSign() {
    return nextCharacter('+') || nextCharacter('-');
  }

  /**
   * Moves past whitespace (space or horizontal tabulator) characters if found;
   * otherwise leaves the index unchanged.
   * @return whether whitespace characters have been found at the current index
   */
  boolean nextWhitespace() {
    int count = 0;
    while (nextCharacter(' ') || nextCharacter('\t')) {
      count++;
    }
    return count > 0;
  }
  /**
   * Moves past an OData identifier if found; otherwise leaves the index unchanged.
   * @return whether an OData identifier has been found at the current index
   */
  private boolean nextODataIdentifier() {
    int count = 0;
    if (index < parseString.length()) {
      int code = parseString.codePointAt(index);
      if (Character.isUnicodeIdentifierStart(code) || code == '_') {
        count++;
        // Unicode characters outside of the Basic Multilingual Plane are represented as two Java characters.
        index += Character.isSupplementaryCodePoint(code) ? 2 : 1;
        while (index < parseString.length() && count < 128) {
          code = parseString.codePointAt(index);
          if (Character.isUnicodeIdentifierPart(code) && !Character.isISOControl(code)) {
            count++;
            // Unicode characters outside of the Basic Multilingual Plane are represented as two Java characters.
            index += Character.isSupplementaryCodePoint(code) ? 2 : 1;
          } else {
            break;
          }
        }
      }
    }
    return count > 0;
  }

  /**
   * Moves past a qualified name if found; otherwise leaves the index unchanged.
   * @return whether a qualified name has been found at the current index
   */
  private boolean nextQualifiedName() {
    int lastGoodIndex = index;
    if (!nextODataIdentifier()) {
      return false;
    }
    int count = 1;
    while (nextCharacter('.')) {
      if (nextODataIdentifier()) {
        count++;
      } else {
        index--;
        break;
      }
    }
    if (count >= 2) {
      return true;
    } else {
      index = lastGoodIndex;
      return false;
    }
  }

  /**
   * Moves past the given whitespace-surrounded operator constant if found.
   * @return whether the operator has been found at the current index
   */
  private boolean nextBinaryOperator(String operator) {
    return nextWhitespace() && nextConstant(operator) && nextWhitespace();
  }

  /**
   * Moves past the given whitespace-suffixed operator constant if found.
   * @return whether the operator has been found at the current index
   */
  private boolean nextUnaryOperator(String operator) {
    return nextConstant(operator) && nextWhitespace();
  }

  /**
   * Moves past the given method name and its immediately following opening parenthesis if found.
   * @return whether the method has been found at the current index
   */
  private boolean nextMethod(String methodName) {
    return nextConstant(methodName) && nextCharacter('(');
  }

  /**
   * Moves past (required) whitespace and the given suffix name if found.
   * @return whether the suffix has been found at the current index
   */
  private boolean nextSuffix(String suffixName) {
    return nextWhitespace() && nextConstant(suffixName);
  }

  private boolean nextParameterAliasName() {
    return nextCharacter('@') && nextODataIdentifier();
  }

  private boolean nextBooleanValue() {
    return nextConstantIgnoreCase("true") || nextConstantIgnoreCase("false");
  }

  private boolean nextStringValue() {
    if (!nextCharacter('\'')) {
      return false;
    }
    while (index < parseString.length()) {
      if (parseString.charAt(index) == '\'') {
        // If a single quote is followed by another single quote,
        // it represents one single quote within the string literal,
        // otherwise it marks the end of the string literal.
        if (index + 1 < parseString.length() && parseString.charAt(index + 1) == '\'') {
          index++;
        } else {
          break;
        }
      }
      index++;
    }
    return nextCharacter('\'');
  }

  /**
   * Moves past an integer value if found; otherwise leaves the index unchanged.
   * @param signed whether a sign character ('+' or '-') at the beginning is allowed
   * @return whether an integer value has been found at the current index
   */
  private boolean nextIntegerValue(boolean signed) {
    int lastGoodIndex = index;
    if (signed) {
      nextSign();
    }
    boolean hasDigits = false;
    while (nextDigit()) {
      hasDigits = true;
    }
    if (hasDigits) {
      return true;
    } else {
      index = lastGoodIndex;
      return false;
    }
  }

  /**
   * Moves past a decimal value with a fractional part if found; otherwise leaves the index unchanged.
   * Whole numbers must be found with {@link #nextIntegerValue()}.
   */
  private boolean nextDecimalValue() {
    int lastGoodIndex = index;
    if (nextIntegerValue(true) && nextCharacter('.') && nextIntegerValue(false)) {
      return true;
    } else {
      index = lastGoodIndex;
      return false;
    }
  }

  /**
   * Moves past a floating-point-number value with an exponential part
   * or one of the special constants "NaN", "-INF", and "INF"
   * if found; otherwise leaves the index unchanged.
   * Whole numbers must be found with {@link #nextIntegerValue()}.
   * Decimal numbers must be found with {@link #nextDecimalValue()}.
   */
  private boolean nextDoubleValue() {
    if (nextConstant("NaN") || nextConstant("-INF") || nextConstant("INF")) {
      return true;
    } else {
      int lastGoodIndex = index;
      if (!nextIntegerValue(true)) {
        return false;
      }
      if (nextCharacter('.') && !nextIntegerValue(false)) {
        index = lastGoodIndex;
        return false;
      }
      if ((nextCharacter('E') || nextCharacter('e')) && nextIntegerValue(true)) {
        return true;
      } else {
        index = lastGoodIndex;
        return false;
      }
    }
  }

  private boolean nextGuidValue() {
    return nextHexDigit() && nextHexDigit() && nextHexDigit() && nextHexDigit()
        && nextHexDigit() && nextHexDigit() && nextHexDigit() && nextHexDigit()
        && nextCharacter('-')
        && nextHexDigit() && nextHexDigit() && nextHexDigit() && nextHexDigit()
        && nextCharacter('-')
        && nextHexDigit() && nextHexDigit() && nextHexDigit() && nextHexDigit()
        && nextCharacter('-')
        && nextHexDigit() && nextHexDigit() && nextHexDigit() && nextHexDigit()
        && nextCharacter('-')
        && nextHexDigit() && nextHexDigit() && nextHexDigit() && nextHexDigit()
        && nextHexDigit() && nextHexDigit() && nextHexDigit() && nextHexDigit()
        && nextHexDigit() && nextHexDigit() && nextHexDigit() && nextHexDigit();
  }

  private boolean nextYear() {
    nextCharacter('-');
    if (nextCharacter('0')) {
      return nextDigit() && nextDigit() && nextDigit();
    } else if (nextCharacterRange('1', '9')) {
      int count = 0;
      while (nextDigit()) {
        count++;
      }
      return count >= 3;
    } else {
      return false;
    }
  }

  private boolean nextDateValue() {
    return nextYear()
        && nextCharacter('-')
        && (nextCharacter('0') && nextCharacterRange('1', '9')
        || nextCharacter('1') && nextCharacterRange('0', '2'))
        && nextCharacter('-')
        && (nextCharacter('0') && nextCharacterRange('1', '9')
            || nextCharacterRange('1', '2') && nextDigit()
            || nextCharacter('3') && nextCharacterRange('0', '1'));
  }

  private boolean nextHours() {
    return nextCharacterRange('0', '1') && nextDigit()
        || nextCharacter('2') && nextCharacterRange('0', '3');
  }

  private boolean nextMinutesOrSeconds() {
    return nextCharacterRange('0', '5') && nextDigit();
  }

  private boolean nextDateTimeOffsetValue() {
    return nextDateValue()
        && (nextCharacter('T') || nextCharacter('t'))
        && nextTimeOfDayValue()
        && (nextCharacter('Z')
            || nextCharacter('z')
            || nextSign() && nextHours() && nextCharacter(':') && nextMinutesOrSeconds());
  }

  private boolean nextTimeOfDayValue() {
    if (nextHours() && nextCharacter(':') && nextMinutesOrSeconds()) {
      if (nextCharacter(':')) {
        if (nextMinutesOrSeconds()) {
            return !nextCharacter('.') || nextIntegerValue(false);
        } else {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  private boolean nextDurationValue() {
    if (nextConstantIgnoreCase("duration") && nextCharacter('\'')) {
      nextSign();
      if (nextCharacter('P') || nextCharacter('p')) {
        if (nextIntegerValue(false) && (!(nextCharacter('D') || nextCharacter('d')))) {
          return false;
        }
        if (nextCharacter('T') || nextCharacter('t')) {
          boolean hasNumber = false;
          if (nextIntegerValue(false)) {
              hasNumber = !nextCharacter('H') && !nextCharacter('h');
          }
          if (hasNumber || nextIntegerValue(false)) {
              hasNumber = !nextCharacter('M') && !nextCharacter('m');
          }
          if (hasNumber || nextIntegerValue(false)) {
            if (nextCharacter('.') && !nextIntegerValue(false)) {
              return false;
            }
            if (!(nextCharacter('S') || nextCharacter('s'))) {
              return false;
            }
          }
        }
        return nextCharacter('\'');
      }
    }
    return false;
  }

  private boolean nextBinaryValue() {
    if (nextConstantIgnoreCase("binary") && nextCharacter('\'')) {
      int lastGoodIndex = index;
      while (nextBase64() && nextBase64() && nextBase64() && nextBase64()) {
        lastGoodIndex += 4;
      }
      index = lastGoodIndex;
      if (nextBase64() && nextBase64()
          && (nextCharacter('A') || nextCharacter('E') || nextCharacter('I') || nextCharacter('M')
              || nextCharacter('Q') || nextCharacter('U') || nextCharacter('Y') || nextCharacter('c')
              || nextCharacter('g') || nextCharacter('k') || nextCharacter('o') || nextCharacter('s')
              || nextCharacter('w') || nextCharacter('0') || nextCharacter('4') || nextCharacter('8'))) {
        nextCharacter('=');
      } else {
        index = lastGoodIndex;
        if (nextBase64()) {
          if (nextCharacter('A') || nextCharacter('Q') || nextCharacter('g') || nextCharacter('w')) {
            nextConstant("==");
          } else {
            return false;
          }
        }
      }
      return nextCharacter('\'');
    }
    return false;
  }

  private boolean nextEnumValue() {
    if (nextQualifiedName() && nextCharacter('\'')) {
      do {
        if (!(nextODataIdentifier() || nextIntegerValue(true))) {
          return false;
        }
      } while (nextCharacter(','));
      return nextCharacter('\'');
    }
    return false;
  }

  /**
   * Moves past a JSON string if found; otherwise leaves the index unchanged.
   * @return whether a JSON string has been found at the current index
   */
  private boolean nextJsonString() {
    int lastGoodIndex = index;
    if (nextCharacter('"')) {
      do {
        if (nextCharacter('\\')) {
          if (!(nextCharacter('b') || nextCharacter('t')
              || nextCharacter('n') || nextCharacter('f') || nextCharacter('r')
              || nextCharacter('"') || nextCharacter('/') || nextCharacter('\\')
              || nextCharacter('u') && nextHexDigit() && nextHexDigit() && nextHexDigit() && nextHexDigit())) {
            index = lastGoodIndex;
            return false;
          }
        } else if (nextCharacter('"')) {
          return true;
        } else {
          index++;
        }
      } while (index < parseString.length());
      index = lastGoodIndex;
      return false;
    }
    index = lastGoodIndex;
    return false;
  }

  private boolean nextJsonValue() {
    return nextConstant("null") || nextConstant("true") || nextConstant("false")
        || nextDoubleValue() || nextDecimalValue() || nextIntegerValue(true)
        || nextJsonString()
        || nextJsonArrayOrObject();
  }

  /**
   * Moves past a JSON object member if found; otherwise leaves the index unchanged.
   * @return whether a JSON object member has been found at the current index
   */
  private boolean nextJsonMember() {
    int lastGoodIndex = index;
    if (nextJsonString() && nextCharacter(':') && nextJsonValue()) {
      return true;
    } else {
      index = lastGoodIndex;
      return false;
    }
  }

  /**
   * Moves past a JSON array or object if found; otherwise leaves the index unchanged.
   * @return whether a JSON array or object has been found at the current index
   */
  private boolean nextJsonArrayOrObject() {
    int lastGoodIndex = index;
    if (nextCharacter('[')) {
      if (nextJsonValue()) {
        while (nextCharacter(',')) {
          if (!nextJsonValue()) {
            index = lastGoodIndex;
            return false;
          }
        }
      }
      if (nextCharacter(']')) {
        return true;
      } else {
        index = lastGoodIndex;
        return false;
      }
    } else if (nextCharacter('{')) {
      if (nextJsonMember()) {
        while (nextCharacter(',')) {
          if (!nextJsonMember()) {
            index = lastGoodIndex;
            return false;
          }
        }
      }
      if (nextCharacter('}')) {
        return true;
      } else {
        index = lastGoodIndex;
        return false;
      }
    } else {
      return false;
    }
  }

  private boolean nextAndOperatorSearch() {
    if (nextWhitespace()) {
      int lastGoodIndex = index;
      if (nextUnaryOperator("OR")) {
        return false;
      } else if (!(nextUnaryOperator("AND"))) {
        index = lastGoodIndex;
      }
      return true;
    } else {
      return false;
    }
  }

  private boolean nextWord() {
    int count = 0;
    while (index < parseString.length()) {
      int code = parseString.codePointAt(index);
      if (Character.isUnicodeIdentifierStart(code)) {
        count++;
        // Unicode characters outside of the Basic Multilingual Plane are represented as two Java characters.
        index += Character.isSupplementaryCodePoint(code) ? 2 : 1;
      } else {
        break;
      }
    }
    String word = parseString.substring(index - count, index);
    return count > 0 && !("OR".equals(word) || "AND".equals(word) || "NOT".equals(word));
  }

  private boolean nextPhrase() {
    if (nextCharacter('"')) {
      do {
        if (nextCharacter('\\')) {
          if (!(nextCharacter('\\') || nextCharacter('"'))) {
            return false;
          }
        } else if (nextCharacter('"')) {
          return true;
        } else {
          index++;
        }
      } while (index < parseString.length());
      return false;
    }
    return false;
  }

}