package nl.buildforce.olingo.server.core.uri.parser;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.Constants;
import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.ex.ODataRuntimeException;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriInfoKind;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceAction;
import nl.buildforce.olingo.server.api.uri.UriResourceCount;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.UriResourceFunction;
import nl.buildforce.olingo.server.api.uri.UriResourcePartTyped;
import nl.buildforce.olingo.server.api.uri.UriResourceRef;
import nl.buildforce.olingo.server.api.uri.UriResourceValue;
import nl.buildforce.olingo.server.api.uri.queryoption.AliasQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyItem;
import nl.buildforce.olingo.server.api.uri.queryoption.ApplyOption;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandItem;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.FilterOption;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByItem;
import nl.buildforce.olingo.server.api.uri.queryoption.OrderByOption;
import nl.buildforce.olingo.server.api.uri.queryoption.QueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SearchOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;
import nl.buildforce.olingo.server.core.uri.UriInfoImpl;
import nl.buildforce.olingo.server.core.uri.UriResourceStartingTypeFilterImpl;
import nl.buildforce.olingo.server.core.uri.parser.UriTokenizer.TokenKind;
import nl.buildforce.olingo.server.core.uri.parser.search.SearchParser;
import nl.buildforce.olingo.server.core.uri.queryoption.AliasQueryOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.ApplyOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.CountOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.DeltaTokenOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.ExpandOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.FilterOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.FormatOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.IdOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.OrderByOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.SearchOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.SelectOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.SkipOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.SkipTokenOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.SystemQueryOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.TopOptionImpl;
import nl.buildforce.olingo.server.core.uri.queryoption.apply.DynamicStructuredType;
import nl.buildforce.olingo.server.core.uri.validator.UriValidationException;

public class Parser {
  private static final String AT = "@";
  private static final String ATOM = "atom";
  private static final String DOLLAR = "$";
  private static final String ENTITY = "$entity";
  private static final String HTTP = "http";
  private static final String JSON = ContentType.JSON;
  private static final String NULL = "null";
  private static final String XML = "xml";

  private final Edm edm;
  private final OData odata;

  public Parser(Edm edm, OData odata) {
    this.edm = edm;
    this.odata = odata;
  }

  public UriInfo parseUri(String path, String query, String baseUri)
      throws UriParserException, UriValidationException {

    UriInfoImpl contextUriInfo = new UriInfoImpl();
   
    // Read the query options (system and custom options).
    // This is done before parsing the resource path because the aliases have to be available there.
    // System query options that can only be parsed with context from the resource path will be post-processed later.
    List<QueryOption> options =
        query == null ? Collections.emptyList() : UriDecoder.splitAndDecodeOptions(query);
    for (QueryOption option : options) {
      String optionName = option.getName();
      String value = option.getText();
      if(UriDecoder.isFormEncoding()){
        value = getFormEncodedValue(value);
      }
      // Parse the untyped option and retrieve a system-option or alias-option instance (or null for a custom option).
      QueryOption parsedOption = parseOption(optionName, value);
      try {
        contextUriInfo.setQueryOption(parsedOption == null ? option : parsedOption);
      } catch (ODataRuntimeException e) {
        throw new UriParserSyntaxException(
            parsedOption instanceof SystemQueryOption ?
                "Double system query option!" :
                "Alias already specified! Name: " + optionName,
            e,
            parsedOption instanceof SystemQueryOption ?
                UriParserSyntaxException.MessageKeys.DOUBLE_SYSTEM_QUERY_OPTION :
                UriParserSyntaxException.MessageKeys.DUPLICATED_ALIAS,
            optionName);
      }
    }

    // Read the decoded path segments.
    EdmType contextType = null;
    boolean contextIsCollection = false;

    List<String> pathSegmentsDecoded = UriDecoder.splitAndDecodePath(path);
    int numberOfSegments = pathSegmentsDecoded.size();
    // Remove an initial empty segment resulting from the expected '/' at the beginning of the path.
    if (numberOfSegments > 1 && pathSegmentsDecoded.get(0).isEmpty()) {
      pathSegmentsDecoded.remove(0);
      numberOfSegments--;
    }

    String firstSegment = pathSegmentsDecoded.get(0);

    if (firstSegment.isEmpty()) {
      ensureLastSegment(firstSegment, 1, numberOfSegments);
      contextUriInfo.setKind(UriInfoKind.service);

    } else if (firstSegment.startsWith("$batch")) {
      ensureLastSegment(firstSegment, 1, numberOfSegments);
      contextUriInfo.setKind(UriInfoKind.batch);

    } else if (firstSegment.startsWith(Constants.METADATA)) {
      ensureLastSegment(firstSegment, 1, numberOfSegments);
      contextUriInfo.setKind(UriInfoKind.metadata);
      //contextUriInfo.setFragment(fragment);
    } else if ("$all".equals(firstSegment)) {
      ensureLastSegment(firstSegment, 1, numberOfSegments);
      contextUriInfo.setKind(UriInfoKind.all);
      contextIsCollection = true;

    } else if ("$entity".equals(firstSegment)) {
      if (null != contextUriInfo.getIdOption()) {
        String idOptionText = contextUriInfo.getIdOption().getText();
        if (idOptionText.startsWith(HTTP)) {
          baseUri = UriDecoder.decode(baseUri);
          if (idOptionText.contains(baseUri)) {
            idOptionText = idOptionText.substring(baseUri.length() + 1);
          } else {
            throw new UriParserSemanticException("$id cannot have an absolute path",
                UriParserSemanticException.MessageKeys.NOT_IMPLEMENTED_SYSTEM_QUERY_OPTION);
          }
        }
        if (numberOfSegments > 1) {
          /*
            If url is of the form
            http://localhost:8080/odata-server-tecsvc/odata.svc/$entity/
            olingo.odata.test1.ETAllPrim?$id=ESAllPrim(32767)
           */
          ResourcePathParser resourcePathParser = new ResourcePathParser
            (edm, contextUriInfo.getAliasMap());
          String typeCastSegment = pathSegmentsDecoded.get(1);
          ensureLastSegment(typeCastSegment, 2, numberOfSegments);
          contextType = resourcePathParser.parseDollarEntityTypeCast(typeCastSegment);
          contextUriInfo = (UriInfoImpl) new Parser(edm, odata).
              parseUri("/" + idOptionText, query, baseUri);
          contextUriInfo.setEntityTypeCast((EdmEntityType) contextType);
        } else {
          /*
            If url is of the form
            http://localhost:8080/odata-server-tecsvc/odata.svc/$entity?$id=ESAllPrim(32527)
           */
          contextUriInfo = (UriInfoImpl) new Parser(edm, odata).
                  parseUri("/" + idOptionText, query, baseUri);
        }
        contextType = contextUriInfo.getEntityTypeCast();
        contextUriInfo.setKind(UriInfoKind.entityId);
        contextIsCollection = false;
      } else {
        /*
          If url is of the form
          http://localhost:8080/odata-server-tecsvc/odata.svc/$entity/olingo.odata.test1.ETKeyNav/$ref
         */
        ensureLastSegment(firstSegment, 2, numberOfSegments);
        /*
          If url is of the form
          http://localhost:8080/odata-server-tecsvc/odata.svc/$entity/olingo.odata.test1.ETKeyNav
         */
        throw new UriParserSyntaxException("The entity-id MUST be specified using the system query option $id",
                  UriParserSyntaxException.MessageKeys.ENTITYID_MISSING_SYSTEM_QUERY_OPTION_ID);
      }
    } else if (firstSegment.startsWith("$crossjoin")) {
      ensureLastSegment(firstSegment, 1, numberOfSegments);
      contextUriInfo.setKind(UriInfoKind.crossjoin);
      List<String> entitySetNames = new ResourcePathParser(edm, contextUriInfo.getAliasMap())
          .parseCrossjoinSegment(firstSegment);
      for (String name : entitySetNames) {
        contextUriInfo.addEntitySetName(name);
      }
      contextIsCollection = true;

    } else {
      contextUriInfo.setKind(UriInfoKind.resource);
      ResourcePathParser resourcePathParser = new ResourcePathParser(edm, contextUriInfo.getAliasMap());
      int count = 0;
      UriResource lastSegment = null;
      for (String pathSegment : pathSegmentsDecoded) {
        count++;
        if (pathSegment.startsWith(ENTITY)) {
          /*
            If url is of the form
            http://localhost:8080/odata-server-tecsvc/odata.svc/ESAllPrim/$entity
           */
          throw new UriParserSyntaxException("The entity-id MUST be specified using the system query option $id",
                    UriParserSyntaxException.MessageKeys.ENTITYID_MISSING_SYSTEM_QUERY_OPTION_ID);
        } else {
          UriResource segment = resourcePathParser.parsePathSegment(pathSegment, lastSegment);
          if (segment != null) {
            if (segment instanceof UriResourceCount
                || segment instanceof UriResourceRef
                || segment instanceof UriResourceValue) {
              ensureLastSegment(pathSegment, count, numberOfSegments);
            } else if (segment instanceof UriResourceAction
                || segment instanceof UriResourceFunction
                && !((UriResourceFunction) segment).getFunction().isComposable()) {
              if (count < numberOfSegments) {
                throw new UriValidationException(
                    "The segment of an action or of a non-composable function must be the last resource-path segment.",
                    UriValidationException.MessageKeys.UNALLOWED_RESOURCE_PATH,
                    pathSegmentsDecoded.get(count));
              }
              lastSegment = segment;
            } else if (segment instanceof UriResourceStartingTypeFilterImpl) {
              throw new UriParserSemanticException("First resource-path segment must not be namespace-qualified.",
                  UriParserSemanticException.MessageKeys.NAMESPACE_NOT_ALLOWED_AT_FIRST_ELEMENT);
            } else {
              lastSegment = segment;
            }
            contextUriInfo.addResourcePart(segment);
          }
        }
      }

      if (lastSegment instanceof UriResourcePartTyped) {
        UriResourcePartTyped typed = (UriResourcePartTyped) lastSegment;
        contextType = ParserHelper.getTypeInformation(typed);
        if (contextType != null && ((lastSegment instanceof UriResourceEntitySet &&
            (((UriResourceEntitySet) lastSegment).getTypeFilterOnCollection() != null
                || ((UriResourceEntitySet) lastSegment).getTypeFilterOnEntry() != null))
            || contextUriInfo.getIdOption() != null) && contextType instanceof EdmEntityType) {
          contextUriInfo.setEntityTypeCast((EdmEntityType) contextType);
        }
        contextIsCollection = typed.isCollection();
      }
    }

    // Post-process system query options that need context information from the resource path.
    if (contextType instanceof EdmStructuredType && contextUriInfo.getApplyOption() != null) {
      // Data aggregation may change the structure of the result.
      contextType = new DynamicStructuredType((EdmStructuredType) contextType);
    }
    parseApplyOption(contextUriInfo.getApplyOption(), contextType,
        contextUriInfo.getEntitySetNames(), contextUriInfo.getAliasMap());
    parseFilterOption(contextUriInfo.getFilterOption(), contextType,
        contextUriInfo.getEntitySetNames(), contextUriInfo.getAliasMap());
    parseOrderByOption(contextUriInfo.getOrderByOption(), contextType,
        contextUriInfo.getEntitySetNames(), contextUriInfo.getAliasMap());
    parseExpandOption(contextUriInfo.getExpandOption(), contextType,
        contextUriInfo.getKind() == UriInfoKind.all, contextUriInfo.getEntitySetNames(),
        contextUriInfo.getAliasMap());
    parseSelectOption(contextUriInfo.getSelectOption(), contextType, contextIsCollection);

    return contextUriInfo;
  }

  private String getFormEncodedValue(String value) {
    if(value.contains("+")){
      value = value.replaceAll("\\+", " ");
    }
    return value;    
  }

  private QueryOption parseOption(String optionName, String optionValue)
      throws UriParserException/*, UriValidationException*/ {
    if (optionName.startsWith(DOLLAR)) {
      SystemQueryOptionKind kind = SystemQueryOptionKind.get(optionName);
      if (kind == null) {
        throw new UriParserSyntaxException("Unknown system query option!",
            UriParserSyntaxException.MessageKeys.UNKNOWN_SYSTEM_QUERY_OPTION, optionName);
      }
      SystemQueryOptionImpl systemOption;
      switch (kind) {
      case SEARCH:
        SearchOption searchOption = new SearchParser().parse(optionValue);
        SearchOptionImpl tmp = new SearchOptionImpl();
        tmp.setSearchExpression(searchOption.getSearchExpression());
        systemOption = tmp;
        break;
      case FILTER:
        systemOption = new FilterOptionImpl();
        break;
      case COUNT:
        if ("true".equals(optionValue) || "false".equals(optionValue)) {
          systemOption = new CountOptionImpl().setValue(Boolean.parseBoolean(optionValue));
        } else {
          throw new UriParserSyntaxException("Illegal value of $count option!",
              UriParserSyntaxException.MessageKeys.WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION,
              optionName, optionValue);
        }
        break;
      case ORDERBY:
        systemOption = new OrderByOptionImpl();
        break;
      case SKIP:
        systemOption = new SkipOptionImpl()
            .setValue(ParserHelper.parseNonNegativeInteger(optionName, optionValue, true));
        break;
      case SKIPTOKEN:
        if (optionValue.isEmpty()) {
          throw new UriParserSyntaxException("Illegal value of $skiptoken option!",
              UriParserSyntaxException.MessageKeys.WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION,
              optionName, optionValue);
        }
        systemOption = new SkipTokenOptionImpl().setValue(optionValue);
        break;
      case DELTATOKEN:
        if (optionValue.isEmpty()) {
          throw new UriParserSyntaxException("Illegal value of $deltatoken option!",
              UriParserSyntaxException.MessageKeys.WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION,
              optionName, optionValue);
        }
        systemOption = new DeltaTokenOptionImpl().setValue(optionValue);
        break;
      case TOP:
        systemOption = new TopOptionImpl()
            .setValue(ParserHelper.parseNonNegativeInteger(optionName, optionValue, true));
        break;
      case EXPAND:
        systemOption = new ExpandOptionImpl();
        break;
      case SELECT:
        systemOption = new SelectOptionImpl();
        break;
      case FORMAT:
        if (optionValue.equalsIgnoreCase(JSON)
            || optionValue.equalsIgnoreCase(XML)
            || optionValue.equalsIgnoreCase(ATOM)
            || isFormatSyntaxValid(optionValue)) {
          systemOption = new FormatOptionImpl().setFormat(optionValue);
        } else {
          throw new UriParserSyntaxException("Illegal value of $format option!",
              UriParserSyntaxException.MessageKeys.WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION_FORMAT, optionValue);
        }
        break;
      case ID:
        if (optionValue.isEmpty()) {
          throw new UriParserSyntaxException("Illegal value of $id option!",
              UriParserSyntaxException.MessageKeys.WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION,
              optionName, optionValue);
        }
        systemOption = new IdOptionImpl(); //.setValue(optionValue);
        break;
      case LEVELS:
        throw new UriParserSyntaxException("System query option '$levels' is allowed only inside '$expand'!",
            UriParserSyntaxException.MessageKeys.SYSTEM_QUERY_OPTION_LEVELS_NOT_ALLOWED_HERE);
      case APPLY:
        systemOption = new ApplyOptionImpl();
        break;
      default:
          throw new UriParserSyntaxException("System query option '" + kind + "' is not known!",
              UriParserSyntaxException.MessageKeys.UNKNOWN_SYSTEM_QUERY_OPTION, optionName);
      }
      systemOption.setText(optionValue);
      return systemOption;

    } else if (optionName.startsWith(AT)) {
      // Aliases can only be parsed in the context of their usage, so the value is not checked here.
      return new AliasQueryOptionImpl()
          .setName(optionName)
          .setText(NULL.equals(optionValue) ? null : optionValue);

    } else {
      // The option is a custom query option; the caller can re-use its query option.
      return null;
    }
  }

  private void parseFilterOption(FilterOption filterOption, EdmType contextType,
      List<String> entitySetNames, Map<String, AliasQueryOption> aliases)
      throws UriParserException, UriValidationException {
    if (filterOption != null) {
      String optionValue = filterOption.getText();
      UriTokenizer filterTokenizer = new UriTokenizer(optionValue);
      // The referring type could be a primitive type or a structured type.
      ((FilterOptionImpl) filterOption).setExpression(
          new FilterParser(edm, odata).parse(filterTokenizer, contextType, entitySetNames, aliases)
              .getExpression());
      checkOptionEOF(filterTokenizer, filterOption.getName(), optionValue);
    }
  }

  private void parseOrderByOption(OrderByOption orderByOption, EdmType contextType,
      List<String> entitySetNames, Map<String, AliasQueryOption> aliases)
      throws UriParserException, UriValidationException {
    if (orderByOption != null) {
      String optionValue = orderByOption.getText();
      UriTokenizer orderByTokenizer = new UriTokenizer(optionValue);
      OrderByOption option = new OrderByParser(edm, odata).parse(orderByTokenizer,
          contextType instanceof EdmStructuredType ? (EdmStructuredType) contextType : null,
          entitySetNames,
          aliases);
      checkOptionEOF(orderByTokenizer, orderByOption.getName(), optionValue);
      for (OrderByItem item : option.getOrders()) {
        ((OrderByOptionImpl) orderByOption).addOrder(item);
      }
    }
  }

  private void parseExpandOption(ExpandOption expandOption, EdmType contextType, boolean isAll,
      List<String> entitySetNames, Map<String, AliasQueryOption> aliases)
      throws UriParserException, UriValidationException {
    if (expandOption != null) {
      if (!(contextType instanceof EdmStructuredType || isAll
      || (entitySetNames != null && !entitySetNames.isEmpty()))) {
        throw new UriValidationException("Expand is only allowed on structured types!",
            UriValidationException.MessageKeys.SYSTEM_QUERY_OPTION_NOT_ALLOWED, expandOption.getName());
      }
      String optionValue = expandOption.getText();
      UriTokenizer expandTokenizer = new UriTokenizer(optionValue);
      ExpandOption option = new ExpandParser(edm, odata, aliases, entitySetNames).parse(expandTokenizer,
          contextType instanceof EdmStructuredType ? (EdmStructuredType) contextType : null);
      checkOptionEOF(expandTokenizer, expandOption.getName(), optionValue);
      for (ExpandItem item : option.getExpandItems()) {
        ((ExpandOptionImpl) expandOption).addExpandItem(item);
      }
    }
  }

  private void parseSelectOption(SelectOption selectOption, EdmType contextType,
      boolean contextIsCollection) throws UriParserException {
    if (selectOption != null) {
      String optionValue = selectOption.getText();
      UriTokenizer selectTokenizer = new UriTokenizer(optionValue);
      ((SelectOptionImpl) selectOption).setSelectItems(
          new SelectParser(edm).parse(selectTokenizer,
              contextType instanceof EdmStructuredType ? (EdmStructuredType) contextType : null,
              contextIsCollection)
              .getSelectItems());
      checkOptionEOF(selectTokenizer, selectOption.getName(), optionValue);
    }
  }

  private void parseApplyOption(ApplyOption applyOption, EdmType contextType,
                                List<String> entitySetNames, Map<String, AliasQueryOption> aliases)
      throws UriParserException, UriValidationException {
    if (applyOption != null) {
      String optionValue = applyOption.getText();
      UriTokenizer applyTokenizer = new UriTokenizer(optionValue);
      ApplyOption option = new ApplyParser(edm, odata).parse(applyTokenizer,
          contextType instanceof EdmStructuredType ? (EdmStructuredType) contextType : null,
          entitySetNames,
          aliases);
      checkOptionEOF(applyTokenizer, applyOption.getName(), optionValue);
      for (ApplyItem item : option.getApplyItems()) {
        ((ApplyOptionImpl) applyOption).add(item);
      }
      ((ApplyOptionImpl) applyOption).setEdmStructuredType(option.getEdmStructuredType());
    }
  }

  private void ensureLastSegment(String segment, int pos, int size)
      throws UriParserSyntaxException {
    if (pos < size) {
      throw new UriParserSyntaxException(segment + " must be the last segment.",
          UriParserSyntaxException.MessageKeys.MUST_BE_LAST_SEGMENT, segment);
    }
  }

  private boolean isFormatSyntaxValid(String value) {
    int index = value.indexOf('/');
    return index > 0 && index < value.length() - 1 && index == value.lastIndexOf('/');
  }

  private void checkOptionEOF(UriTokenizer tokenizer, String optionName, String optionValue)
      throws UriParserException {
    if (!tokenizer.next(TokenKind.EOF)) {
      throw new UriParserSyntaxException("Illegal value of '" + optionName + "' option!",
          UriParserSyntaxException.MessageKeys.WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION,
          optionName, optionValue);
    }
  }

}