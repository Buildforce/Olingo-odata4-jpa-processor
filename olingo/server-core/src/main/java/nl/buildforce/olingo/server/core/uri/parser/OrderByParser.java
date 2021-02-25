/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser;

import java.util.Collection;
import java.util.Map;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.uri.queryoption.AliasQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByOption;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;
import nl.buildforce.olingo.server.core.uri.parser.UriTokenizer.TokenKind;
import nl.buildforce.olingo.server.core.uri.queryoption.OrderByItemImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.OrderByOptionImpl;
import nl.buildforce.olingo.server.core.uri.validator.UriValidationException;

public class OrderByParser {

  private final Edm edm;
  private final OData odata;

  public OrderByParser(Edm edm, OData odata) {
    this.edm = edm;
    this.odata = odata;
  }

  public OrderByOption parse(UriTokenizer tokenizer, EdmStructuredType referencedType,
      Collection<String> crossjoinEntitySetNames, Map<String, AliasQueryOption> aliases)
      throws UriParserException, UriValidationException {
    OrderByOptionImpl orderByOption = new OrderByOptionImpl();
    do {
      Expression orderByExpression = new ExpressionParser(edm, odata)
          .parse(tokenizer, referencedType, crossjoinEntitySetNames, aliases);
      OrderByItemImpl item = new OrderByItemImpl();
      item.setExpression(orderByExpression);
      if (tokenizer.next(TokenKind.AscSuffix)) {
        item.setDescending(false);
      } else if (tokenizer.next(TokenKind.DescSuffix)) {
        item.setDescending(true);
      }
      orderByOption.addOrder(item);
    } while (tokenizer.next(TokenKind.COMMA));
    return orderByOption;
  }
}
