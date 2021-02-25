/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser.search;

public interface SearchQueryToken {
  enum Token {
    OPEN, NOT, AND, OR, WORD, PHRASE, CLOSE
  }

  Token getToken();

  String getLiteral();
}
