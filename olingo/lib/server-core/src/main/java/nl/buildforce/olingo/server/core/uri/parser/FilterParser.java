/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.buildforce.olingo.server.core.uri.parser;

import java.util.Collection;
import java.util.Map;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.uri.queryoption.AliasQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FilterOption;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;
import nl.buildforce.olingo.server.core.uri.queryoption.FilterOptionImpl;
import nl.buildforce.olingo.server.core.uri.validator.UriValidationException;

public class FilterParser {

  private final Edm edm;
  private final OData odata;

  public FilterParser(Edm edm, OData odata) {
    this.edm = edm;
    this.odata = odata;
  }

  public FilterOption parse(UriTokenizer tokenizer, EdmType referencedType,
      Collection<String> crossjoinEntitySetNames, Map<String, AliasQueryOption> aliases)
      throws UriParserException, UriValidationException {
    Expression filterExpression = new ExpressionParser(edm, odata)
        .parse(tokenizer, referencedType, crossjoinEntitySetNames, aliases);
    EdmType type = ExpressionParser.getType(filterExpression);
    if (type == null || type.equals(odata.createPrimitiveTypeInstance(EdmPrimitiveTypeKind.Boolean))) {
      return new FilterOptionImpl().setExpression(filterExpression);
    } else {
      throw new UriParserSemanticException("Filter expressions must be boolean.",
          UriParserSemanticException.MessageKeys.TYPES_NOT_COMPATIBLE,
          "Edm.Boolean", type.getFullQualifiedName().getFullQualifiedNameAsString());
    }
  }
}
