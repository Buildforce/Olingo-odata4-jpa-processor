/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser;

import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchBinaryOperatorKind;
import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchExpression;
import nl.buildforce.olingo.server.api.uri.queryoption.search.SearchTerm;
import nl.buildforce.olingo.server.core.uri.parser.UriTokenizer.TokenKind;
import nl.buildforce.olingo.server.core.uri.parser.search.SearchBinaryImpl;
import nl.buildforce.olingo.server.core.uri.parser.search.SearchParserException;
import nl.buildforce.olingo.server.core.uri.parser.search.SearchTermImpl;
import nl.buildforce.olingo.server.core.uri.parser.search.SearchUnaryImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.SearchOptionImpl;

/**
 * Parses search expressions according to the following (rewritten) grammar:
 * <pre>
 * SearchExpr  ::= ExprOR
 * ExprOR      ::= ExprAnd ('OR' ExprAnd)*
 * ExprAnd     ::= Term ('AND'? Term)*
 * Term        ::= ('NOT'? (Word | Phrase)) | ('(' SearchExpr ')')
 * </pre> 
 */
public class SearchParser {

  public SearchOption parse(UriTokenizer tokenizer) throws SearchParserException {
    SearchOptionImpl searchOption = new SearchOptionImpl();
    searchOption.setSearchExpression(processExprOr(tokenizer));
    return searchOption;
  }

  private SearchExpression processExprOr(UriTokenizer tokenizer) throws SearchParserException {
    SearchExpression left = processExprAnd(tokenizer);

    while (tokenizer.next(TokenKind.OrOperatorSearch)) {
      SearchExpression right = processExprAnd(tokenizer);
      left = new SearchBinaryImpl(left, SearchBinaryOperatorKind.OR, right);
    }

    return left;
  }

  private SearchExpression processExprAnd(UriTokenizer tokenizer) throws SearchParserException {
    SearchExpression left = processTerm(tokenizer);

    while (tokenizer.next(TokenKind.AndOperatorSearch)) { 
      // Could be whitespace or whitespace-surrounded 'AND'.
      SearchExpression right = processTerm(tokenizer);
      left = new SearchBinaryImpl(left, SearchBinaryOperatorKind.AND, right);
    }

    return left;
  }

  private SearchExpression processTerm(UriTokenizer tokenizer) throws SearchParserException {
    if (tokenizer.next(TokenKind.OPEN)) {
      ParserHelper.bws(tokenizer);
      SearchExpression expr = processExprOr(tokenizer);
      ParserHelper.bws(tokenizer);
      if (!tokenizer.next(TokenKind.CLOSE)) {
        throw new SearchParserException("Missing close parenthesis after open parenthesis.",
            SearchParserException.MessageKeys.MISSING_CLOSE);
      }
      return expr;
    } else if (tokenizer.next(TokenKind.NotOperatorSearch)) {
      return processNot(tokenizer);
    } else if (tokenizer.next(TokenKind.Word)) {
      return new SearchTermImpl(tokenizer.getText());
    } else if (tokenizer.next(TokenKind.Phrase)) {
      return processPhrase(tokenizer);
    } else {
      throw new SearchParserException("Expected PHRASE or WORD not found.",
          SearchParserException.MessageKeys.EXPECTED_DIFFERENT_TOKEN, "PHRASE, WORD", "");
    }
  }

  private SearchExpression processNot(UriTokenizer tokenizer) throws SearchParserException {
    if (tokenizer.next(TokenKind.Word)) {
      return new SearchUnaryImpl(new SearchTermImpl(tokenizer.getText()));
    } else if (tokenizer.next(TokenKind.Phrase)) {
      return new SearchUnaryImpl(processPhrase(tokenizer));
    } else {
      throw new SearchParserException("NOT must be followed by a term.",
          SearchParserException.MessageKeys.INVALID_NOT_OPERAND, "");
    }
  }

  private SearchTerm processPhrase(UriTokenizer tokenizer) {
    String literal = tokenizer.getText();
    return new SearchTermImpl(literal.substring(1, literal.length() - 1)
        .replace("\\\"", "\"")
        .replace("\\\\", "\\"));
  }

}