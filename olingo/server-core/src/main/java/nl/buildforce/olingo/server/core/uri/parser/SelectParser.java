/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.parser;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.server.api.uri.UriResourcePartTyped;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectItem;
import nl.buildforce.olingo.server.api.uri.UriInfoKind;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.core.uri.UriInfoImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceActionImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceComplexPropertyImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceFunctionImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceNavigationPropertyImpl;
import nl.buildforce.olingo.server.core.uri.UriResourcePrimitivePropertyImpl;
import nl.buildforce.olingo.server.core.uri.parser.UriTokenizer.TokenKind;
import nl.buildforce.olingo.server.core.uri.queryoption.SelectItemImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.SelectOptionImpl;
import nl.buildforce.olingo.server.core.uri.validator.UriValidationException;

public class SelectParser {

  private final Edm edm;

  public SelectParser(Edm edm) {
    this.edm = edm;
  }

  public SelectOption parse(UriTokenizer tokenizer, EdmStructuredType referencedType,
      boolean referencedIsCollection) throws UriParserException, UriValidationException {
    List<SelectItem> selectItems = new ArrayList<>();
    SelectItem item;
    do {
      item = parseItem(tokenizer, referencedType, referencedIsCollection);
      selectItems.add(item);
    } while (tokenizer.next(TokenKind.COMMA));

    return new SelectOptionImpl().setSelectItems(selectItems);
  }

  private SelectItem parseItem(UriTokenizer tokenizer,
      EdmStructuredType referencedType, boolean referencedIsCollection) throws UriParserException {
    SelectItemImpl item = new SelectItemImpl();
    if (tokenizer.next(TokenKind.STAR)) {
      item.setStar(true);

    } else if (tokenizer.next(TokenKind.QualifiedName)) {
      // The namespace or its alias could consist of dot-separated OData identifiers.
      FullQualifiedName allOperationsInSchema = parseAllOperationsInSchema(tokenizer);
      if (allOperationsInSchema != null) {
        item.addAllOperationsInSchema(allOperationsInSchema);

      } else {
        ensureReferencedTypeNotNull(referencedType);
        FullQualifiedName qualifiedName = new FullQualifiedName(tokenizer.getText());
        EdmStructuredType type = edm.getEntityType(qualifiedName);
        if (type == null) {
          type = edm.getComplexType(qualifiedName);
        }
        if (type == null) {
          item.setResourcePath(new UriInfoImpl().setKind(UriInfoKind.resource).addResourcePart(
              parseBoundOperation(tokenizer, qualifiedName, referencedType, referencedIsCollection)));

        } else {
          if (type.compatibleTo(referencedType)) {
            item.setTypeFilter(type);
            if (tokenizer.next(TokenKind.SLASH)) {
              ParserHelper.requireNext(tokenizer, TokenKind.ODataIdentifier);
              UriInfoImpl resource = new UriInfoImpl().setKind(UriInfoKind.resource);
              addSelectPath(tokenizer, type, resource);
              item.setResourcePath(resource);
            }
          } else {
            throw new UriParserSemanticException("The type cast is not compatible.",
                UriParserSemanticException.MessageKeys.INCOMPATIBLE_TYPE_FILTER, type.getName());
          }
        }
      }

    } else {
      ParserHelper.requireNext(tokenizer, TokenKind.ODataIdentifier);
      // The namespace or its alias could be a single OData identifier.
      FullQualifiedName allOperationsInSchema = parseAllOperationsInSchema(tokenizer);
      if (allOperationsInSchema != null) {
        item.addAllOperationsInSchema(allOperationsInSchema);

      } else {
        ensureReferencedTypeNotNull(referencedType);
        UriInfoImpl resource = new UriInfoImpl().setKind(UriInfoKind.resource);
        addSelectPath(tokenizer, referencedType, resource);
        item.setResourcePath(resource);
      }
    }

    return item;
  }

  private FullQualifiedName parseAllOperationsInSchema(UriTokenizer tokenizer) throws UriParserException {
    String namespace = tokenizer.getText();
    if (tokenizer.next(TokenKind.DOT)) {
      if (tokenizer.next(TokenKind.STAR)) {
        // Validate the namespace.  Currently a namespace from a non-default schema is not supported.
        // There is no direct access to the namespace without loading the whole schema;
        // however, the default entity container should always be there, so its access methods can be used.
        if (edm.getEntityContainer(new FullQualifiedName(namespace, edm.getEntityContainer().getName())) == null) {
          throw new UriParserSemanticException("Wrong namespace '" + namespace + "'.",
              UriParserSemanticException.MessageKeys.UNKNOWN_PART, namespace);
        }
        return new FullQualifiedName(namespace, tokenizer.getText());
      } else {
        throw new UriParserSemanticException("Expected star after dot.",
            UriParserSemanticException.MessageKeys.UNKNOWN_PART, "");
      }
    }
    return null;
  }

  private void ensureReferencedTypeNotNull(EdmStructuredType referencedType) throws UriParserException {
    if (referencedType == null) {
      throw new UriParserSemanticException("The referenced part is not typed.",
          UriParserSemanticException.MessageKeys.ONLY_FOR_TYPED_PARTS, "select");
    }
  }

  private UriResourcePartTyped parseBoundOperation(UriTokenizer tokenizer, FullQualifiedName qualifiedName,
                                                   EdmStructuredType referencedType, boolean referencedIsCollection) throws UriParserException {
    EdmAction boundAction = edm.getBoundAction(qualifiedName,
        referencedType.getFullQualifiedName(),
        referencedIsCollection);
    if (boundAction == null) {
      List<String> parameterNames = parseFunctionParameterNames(tokenizer);
      EdmFunction boundFunction = edm.getBoundFunction(qualifiedName,
          referencedType.getFullQualifiedName(), referencedIsCollection, parameterNames);
      if (boundFunction == null) {
        throw new UriParserSemanticException("Function not found.",
            UriParserSemanticException.MessageKeys.UNKNOWN_PART, qualifiedName.getFullQualifiedNameAsString());
      } else {
        return new UriResourceFunctionImpl(null, boundFunction, null);
      }
    } else {
      return new UriResourceActionImpl(boundAction);
    }
  }

  private List<String> parseFunctionParameterNames(UriTokenizer tokenizer) throws UriParserException {
    List<String> names = new ArrayList<>();
    if (tokenizer.next(TokenKind.OPEN)) {
      do {
        ParserHelper.requireNext(tokenizer, TokenKind.ODataIdentifier);
        names.add(tokenizer.getText());
      } while (tokenizer.next(TokenKind.COMMA));
      ParserHelper.requireNext(tokenizer, TokenKind.CLOSE);
    }
    return names;
  }

  private void addSelectPath(UriTokenizer tokenizer, EdmStructuredType referencedType, UriInfoImpl resource)
      throws UriParserException {
    String name = tokenizer.getText();
    EdmProperty property = referencedType.getStructuralProperty(name);

    if (property == null) {
      EdmNavigationProperty navigationProperty = referencedType.getNavigationProperty(name);
      if (navigationProperty == null) {
        throw new UriParserSemanticException("Selected property not found.",
            UriParserSemanticException.MessageKeys.EXPRESSION_PROPERTY_NOT_IN_TYPE,
            referencedType.getName(), name);
      } else {
        resource.addResourcePart(new UriResourceNavigationPropertyImpl(navigationProperty));
      }

    } else if (property.isPrimitive()
        || property.getType().getKind() == EdmTypeKind.ENUM
        || property.getType().getKind() == EdmTypeKind.DEFINITION) {
      resource.addResourcePart(new UriResourcePrimitivePropertyImpl(property));

    } else {
      UriResourceComplexPropertyImpl complexPart = new UriResourceComplexPropertyImpl(property);
      resource.addResourcePart(complexPart);
      if (tokenizer.next(TokenKind.SLASH)) {
        if (tokenizer.next(TokenKind.QualifiedName)) {
          FullQualifiedName qualifiedName = new FullQualifiedName(tokenizer.getText());
          EdmComplexType type = edm.getComplexType(qualifiedName);
          if (type == null) {
            throw new UriParserSemanticException("Type not found.",
                UriParserSemanticException.MessageKeys.UNKNOWN_TYPE, qualifiedName.getFullQualifiedNameAsString());
          } else if (type.compatibleTo(property.getType())) {
            complexPart.setTypeFilter(type);
            if (tokenizer.next(TokenKind.SLASH)) {
              if (tokenizer.next(TokenKind.ODataIdentifier)) {
                addSelectPath(tokenizer, type, resource);
              } else {
                throw new UriParserSemanticException("Unknown part after '/'.",
                    UriParserSemanticException.MessageKeys.UNKNOWN_PART, "");
              }
            }
          } else {
            throw new UriParserSemanticException("The type cast is not compatible.",
                UriParserSemanticException.MessageKeys.INCOMPATIBLE_TYPE_FILTER, type.getName());
          }
        } else if (tokenizer.next(TokenKind.ODataIdentifier)) {
          addSelectPath(tokenizer, (EdmStructuredType) property.getType(), resource);
        } else if (tokenizer.next(TokenKind.SLASH)) {
          throw new UriParserSyntaxException("Illegal $select expression.",
              UriParserSyntaxException.MessageKeys.SYNTAX);
        } else {
          throw new UriParserSemanticException("Unknown part after '/'.",
              UriParserSemanticException.MessageKeys.UNKNOWN_PART, "");
        }
      }
    }
  }
}
