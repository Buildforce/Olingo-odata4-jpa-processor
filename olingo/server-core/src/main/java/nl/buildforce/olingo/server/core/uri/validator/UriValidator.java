/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.validator;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.EdmReturnType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.server.api.uri.UriResourceAction;
import nl.buildforce.olingo.server.api.uri.UriResourceFunction;
import nl.buildforce.olingo.server.api.uri.UriResourcePartTyped;
import nl.buildforce.olingo.server.api.uri.UriResourceProperty;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

public class UriValidator {

  //@formatter:off (Eclipse formatter)
  //CHECKSTYLE:OFF (Maven checkstyle)
  private static final boolean[][] decisionMatrix =
    {
      /*                                          0-FILTER 1-FORMAT 2-EXPAND 3-ID     4-COUNT  5-ORDERBY 6-SEARCH 7-SELECT 8-SKIP   9-SKIPTOKEN 10-TOP 11-APPLY  12-DELTATOKEN */
      /*                              all  0 */ { true ,   true ,   true ,   false,   true ,   true ,    true ,   true ,   true ,   true ,      true , true,      true  },
      /*                            batch  1 */ { false,   false,   false,   false,   false,   false,    false,   false,   false,   false,      false, false,     false  },
      /*                        crossjoin  2 */ { true ,   true ,   true ,   false,   true ,   true ,    true ,   true ,   true ,   true ,      true , true,      true  },
      /*                         entityId  3 */ { false,   true ,   true ,   true ,   false,   false,    false,   true ,   false,   false,      false, false,     false },
      /*                         metadata  4 */ { false,   true ,   false,   false,   false,   false,    false,   false,   false,   false,      false, false,     false },
      /*                          service  5 */ { false,   true ,   false,   false,   false,   false,    false,   false,   false,   false,      false, false,     false },
      /*                        entitySet  6 */ { true ,   true ,   true ,   false,   true ,   true ,    true ,   true ,   true ,   true ,      true , true,      true  },
      /*                   entitySetCount  7 */ { true ,   false,   false,   false,   false,   false,    true ,   false,   false,   false,      false, true,      true  },
      /*                           entity  8 */ { false,   true ,   true ,   false,   false,   false,    false,   true ,   false,   false,      false, false,     false },
      /*                      mediaStream  9 */ { false,   false,   false,   false,   false,   false,    false,   false,   false,   false,      false, false,     false },
      /*                       references 10 */ { true ,   true ,   false,   false,   true ,   true ,    true ,   false,   true ,   true ,      true , false,     true },
      /*                        reference 11 */ { false,   true ,   false,   false,   false,   false,    false,   false,   false,   false,      false, false,     false },
      /*                  propertyComplex 12 */ { false,   true ,   true ,   false,   false,   false,    false,   true ,   false,   false,      false, false,     false },
      /*        propertyComplexCollection 13 */ { true ,   true ,   true ,   false,   true ,   true ,    false,   true ,   true ,   true ,      true , true ,     true },
      /*   propertyComplexCollectionCount 14 */ { true ,   false,   false,   false,   false,   false,    false,   false,   false,   false,      false, true ,     false },
      /*                propertyPrimitive 15 */ { false,   true ,   false,   false,   false,   false,    false,   false,   false,   false,      false, false,     false },
      /*      propertyPrimitiveCollection 16 */ { true ,   true ,   false,   false,   true ,   true ,    false,   false,   true ,   true ,      true , false,     true },
      /* propertyPrimitiveCollectionCount 17 */ { true ,   false,   false,   false,   false,   false,    false,   false,   false,   false,      false, false,     false },
      /*           propertyPrimitiveValue 18 */ { false,   true ,   false,   false,   false,   false,    false,   false,   false,   false,      false, false,     false },
      /*                             none 19 */ { false,   true ,   false,   false,   false,   false,    false,   false,   false,   false,      false, false,     false }
    };
  //CHECKSTYLE:ON
  //@formatter:on

  private enum UriType {
    all(0),
    batch(1),
    crossjoin(2),
    entityId(3),
    metadata(4),
    service(5),
    entitySet(6),
    entitySetCount(7),
    entity(8),
    mediaStream(9),
    references(10),
    reference(11),
    propertyComplex(12),
    propertyComplexCollection(13),
    propertyComplexCollectionCount(14),
    propertyPrimitive(15),
    propertyPrimitiveCollection(16),
    propertyPrimitiveCollectionCount(17),
    propertyPrimitiveValue(18),
    none(19);

    private final int idx;

    UriType(int i) {
      idx = i;
    }

    public int getIndex() {
      return idx;
    }
  }

  private static final Map<SystemQueryOptionKind, Integer> OPTION_INDEX;
  static {
    Map<SystemQueryOptionKind, Integer> temp =
        new EnumMap<>(SystemQueryOptionKind.class);
    temp.put(SystemQueryOptionKind.FILTER, 0);
    temp.put(SystemQueryOptionKind.FORMAT, 1);
    temp.put(SystemQueryOptionKind.EXPAND, 2);
    temp.put(SystemQueryOptionKind.ID, 3);
    temp.put(SystemQueryOptionKind.COUNT, 4);
    temp.put(SystemQueryOptionKind.ORDERBY, 5);
    temp.put(SystemQueryOptionKind.SEARCH, 6);
    temp.put(SystemQueryOptionKind.SELECT, 7);
    temp.put(SystemQueryOptionKind.SKIP, 8);
    temp.put(SystemQueryOptionKind.SKIPTOKEN, 9);
    temp.put(SystemQueryOptionKind.TOP, 10);
    temp.put(SystemQueryOptionKind.APPLY, 11);
    temp.put(SystemQueryOptionKind.DELTATOKEN, 12);
    OPTION_INDEX = Collections.unmodifiableMap(temp);
  }

  public void validate(UriInfo uriInfo, HttpMethod httpMethod) throws UriValidationException {
    UriType uriType = getUriType(uriInfo);
    if (HttpMethod.GET == httpMethod) {
      validateReadQueryOptions(uriType, uriInfo.getSystemQueryOptions());
    } else {
      validateNonReadQueryOptions(uriType, isAction(uriInfo), uriInfo.getSystemQueryOptions(), httpMethod);
      validatePropertyOperations(uriInfo, httpMethod);
    }
  }

  private UriType getUriType(UriInfo uriInfo) throws UriValidationException {

    return switch (uriInfo.getKind()) {
      case all -> UriType.all;
      case batch -> UriType.batch;
      case crossjoin -> UriType.crossjoin;
      case entityId -> UriType.entityId;
      case metadata -> UriType.metadata;
      case resource -> getUriTypeForResource(uriInfo.getUriResourceParts());
      case service -> UriType.service;
/*
      default -> throw new UriValidationException("Unsupported uriInfo kind: " + uriInfo.getKind(),
              UriValidationException.MessageKeys.UNSUPPORTED_URI_KIND, uriInfo.getKind().toString());
*/
    };
  }

  /**
   * Determines the URI type for a resource path.
   * The URI parser has already made sure that there are enough segments for a given type of the last segment,
   * but don't try to extract always the second-to-last segment, it could cause an {@link IndexOutOfBoundsException}.
   */
  private UriType getUriTypeForResource(List<UriResource> segments) throws UriValidationException {
    UriResource lastPathSegment = segments.get(segments.size() - 1);

    return switch (lastPathSegment.getKind()) {
      case count -> getUriTypeForCount(segments.get(segments.size() - 2));
      case action -> getUriTypeForAction(lastPathSegment);
      case complexProperty -> getUriTypeForComplexProperty(lastPathSegment);
      case entitySet, navigationProperty -> getUriTypeForEntitySet(lastPathSegment);
      case function -> getUriTypeForFunction(lastPathSegment);
      case primitiveProperty -> getUriTypeForPrimitiveProperty(lastPathSegment);
      case ref -> getUriTypeForRef(segments.get(segments.size() - 2));
      case singleton -> UriType.entity;
      case value -> getUriTypeForValue(segments.get(segments.size() - 2));
      default -> throw new UriValidationException("Unsupported uriResource kind: " + lastPathSegment.getKind(),
              UriValidationException.MessageKeys.UNSUPPORTED_URI_RESOURCE_KIND, lastPathSegment.getKind().toString());
    };
  }

  private UriType getUriTypeForValue(UriResource secondLastPathSegment) throws UriValidationException {
    UriType uriType;
    switch (secondLastPathSegment.getKind()) {
      case primitiveProperty -> uriType = UriType.propertyPrimitiveValue;
      case entitySet, navigationProperty, singleton -> uriType = UriType.mediaStream;
      case function -> {
        UriResourceFunction uriFunction = (UriResourceFunction) secondLastPathSegment;
        EdmFunction function = uriFunction.getFunction();
        uriType = function.getReturnType().getType().getKind() == EdmTypeKind.ENTITY ?
                UriType.mediaStream : UriType.propertyPrimitiveValue;
      }
      default -> throw new UriValidationException(
              "Unexpected kind in path segment before $value: " + secondLastPathSegment.getKind(),
              UriValidationException.MessageKeys.UNALLOWED_KIND_BEFORE_VALUE, secondLastPathSegment.toString());
    }
    return uriType;
  }

  private UriType getUriTypeForRef(UriResource secondLastPathSegment) throws UriValidationException {
    return isCollection(secondLastPathSegment) ? UriType.references : UriType.reference;
  }

  private boolean isCollection(UriResource pathSegment) throws UriValidationException {
    if (pathSegment instanceof UriResourcePartTyped) {
      return ((UriResourcePartTyped) pathSegment).isCollection();
    } else {
      throw new UriValidationException(
          "Path segment is not an instance of UriResourcePartTyped but " + pathSegment.getClass(),
          UriValidationException.MessageKeys.LAST_SEGMENT_NOT_TYPED, pathSegment.toString());
    }
  }

  private UriType getUriTypeForPrimitiveProperty(UriResource lastPathSegment) throws UriValidationException {
    return isCollection(lastPathSegment) ? UriType.propertyPrimitiveCollection : UriType.propertyPrimitive;
  }

  private UriType getUriTypeForFunction(UriResource lastPathSegment) throws UriValidationException {
    UriResourceFunction uriFunction = (UriResourceFunction) lastPathSegment;
    boolean isCollection = uriFunction.isCollection();
    EdmTypeKind typeKind = uriFunction.getFunction().getReturnType().getType().getKind();

    return switch (typeKind) {
      case ENTITY -> isCollection ? UriType.entitySet : UriType.entity;
      case PRIMITIVE, ENUM, DEFINITION -> isCollection ? UriType.propertyPrimitiveCollection : UriType.propertyPrimitive;
      case COMPLEX -> isCollection ? UriType.propertyComplexCollection : UriType.propertyComplex;
      default -> throw new UriValidationException("Unsupported function return type: " + typeKind,
              UriValidationException.MessageKeys.UNSUPPORTED_FUNCTION_RETURN_TYPE, typeKind.toString());
    };
  }

  private UriType getUriTypeForEntitySet(UriResource lastPathSegment) throws UriValidationException {
    return isCollection(lastPathSegment) ? UriType.entitySet : UriType.entity;
  }

  private UriType getUriTypeForComplexProperty(UriResource lastPathSegment) throws UriValidationException {
    return isCollection(lastPathSegment) ? UriType.propertyComplexCollection : UriType.propertyComplex;
  }

  private UriType getUriTypeForAction(UriResource lastPathSegment) throws UriValidationException {
    EdmReturnType rt = ((UriResourceAction) lastPathSegment).getAction().getReturnType();
    if (rt == null) {
      return UriType.none;
    }
    return switch (rt.getType().getKind()) {
      case ENTITY -> rt.isCollection() ? UriType.entitySet : UriType.entity;
      case PRIMITIVE, ENUM, DEFINITION -> rt.isCollection() ? UriType.propertyPrimitiveCollection : UriType.propertyPrimitive;
      case COMPLEX -> rt.isCollection() ? UriType.propertyComplexCollection : UriType.propertyComplex;
      default -> throw new UriValidationException("Unsupported action return type: " + rt.getType().getKind(),
              UriValidationException.MessageKeys.UNSUPPORTED_ACTION_RETURN_TYPE, rt.getType().getKind().toString());
    };
  }

  private UriType getUriTypeForCount(UriResource secondLastPathSegment) throws UriValidationException {
    UriType uriType;
    switch (secondLastPathSegment.getKind()) {
      case entitySet, navigationProperty -> uriType = UriType.entitySetCount;
      case complexProperty -> uriType = UriType.propertyComplexCollectionCount;
      case primitiveProperty -> uriType = UriType.propertyPrimitiveCollectionCount;
      case function -> {
        UriResourceFunction uriFunction = (UriResourceFunction) secondLastPathSegment;
        EdmFunction function = uriFunction.getFunction();
        EdmType returnType = function.getReturnType().getType();
        uriType = switch (returnType.getKind()) {
          case ENTITY -> UriType.entitySetCount;
          case COMPLEX -> UriType.propertyComplexCollectionCount;
          case PRIMITIVE, ENUM, DEFINITION -> UriType.propertyPrimitiveCollectionCount;
          default -> throw new UriValidationException("Unsupported return type: " + returnType.getKind(),
                  UriValidationException.MessageKeys.UNSUPPORTED_FUNCTION_RETURN_TYPE, returnType.getKind().toString());
        };
      }
      default -> throw new UriValidationException("Illegal path part kind before $count: " + secondLastPathSegment.getKind(),
              UriValidationException.MessageKeys.UNALLOWED_KIND_BEFORE_COUNT, secondLastPathSegment.toString());
    }

    return uriType;
  }

  private void validateReadQueryOptions(UriType uriType, List<SystemQueryOption> options)
      throws UriValidationException {
    for (SystemQueryOption option : options) {
      SystemQueryOptionKind kind = option.getKind();
      if (OPTION_INDEX.containsKey(kind)) {
        int columnIndex = OPTION_INDEX.get(kind);
        if (!decisionMatrix[uriType.getIndex()][columnIndex]) {
          throw new UriValidationException("System query option not allowed: " + option.getName(),
              UriValidationException.MessageKeys.SYSTEM_QUERY_OPTION_NOT_ALLOWED, option.getName());
        }
      } else {
        throw new UriValidationException("Unsupported option: " + kind,
            UriValidationException.MessageKeys.UNSUPPORTED_QUERY_OPTION, kind.toString());
      }
    }
  }

  private void validateNonReadQueryOptions(UriType uriType, boolean isAction,
                                           List<SystemQueryOption> options, HttpMethod httpMethod) throws UriValidationException {
    if (httpMethod == HttpMethod.POST && isAction) {
      // From the OData specification:
      // For POST requests to an action URL the return type of the action determines the applicable
      // system query options that a service MAY support, following the same rules as GET requests.
      validateReadQueryOptions(uriType, options);

    } else if (httpMethod == HttpMethod.DELETE && uriType == UriType.references) {
      // Only $id is allowed as SystemQueryOption for DELETE on a reference collection.
      for (SystemQueryOption option : options) {
        if (SystemQueryOptionKind.ID != option.getKind()) {
          throw new UriValidationException(
              "System query option " + option.getName() + " is not allowed for method " + httpMethod,
              UriValidationException.MessageKeys.SYSTEM_QUERY_OPTION_NOT_ALLOWED_FOR_HTTP_METHOD,
              option.getName(), httpMethod.toString());
        }
      }

    } else if (!options.isEmpty()) {
      StringBuilder optionsString = new StringBuilder();
      for (SystemQueryOption option : options) {
        optionsString.append(option.getName()).append(' ');
      }
      throw new UriValidationException(
          "System query option(s) " + optionsString + "not allowed for method " + httpMethod,
          UriValidationException.MessageKeys.SYSTEM_QUERY_OPTION_NOT_ALLOWED_FOR_HTTP_METHOD,
          optionsString.toString(), httpMethod.toString());
    }
  }

  private boolean isAction(UriInfo uriInfo) {
    List<UriResource> uriResourceParts = uriInfo.getUriResourceParts();
    return !uriResourceParts.isEmpty()
        && UriResourceKind.action == uriResourceParts.get(uriResourceParts.size() - 1).getKind();
  }

  private void validatePropertyOperations(UriInfo uriInfo, HttpMethod method)
      throws UriValidationException {
    List<UriResource> parts = uriInfo.getUriResourceParts();
    UriResource last = !parts.isEmpty() ? parts.get(parts.size() - 1) : null;
    UriResource previous = parts.size() > 1 ? parts.get(parts.size() - 2) : null;
    if (last != null
        && (last.getKind() == UriResourceKind.primitiveProperty
        || last.getKind() == UriResourceKind.complexProperty
        || (last.getKind() == UriResourceKind.value
            && previous != null && previous.getKind() == UriResourceKind.primitiveProperty))) {
      EdmProperty property = ((UriResourceProperty)
          (last.getKind() == UriResourceKind.value ? previous : last)).getProperty();
      if (method == HttpMethod.PATCH && property.isCollection()) {
        throw new UriValidationException("Attempt to patch collection property.",
            UriValidationException.MessageKeys.UNSUPPORTED_HTTP_METHOD, method.toString());
      }
      if (method == HttpMethod.DELETE && !property.isNullable()) {
        throw new UriValidationException("Attempt to delete non-nullable property.",
            UriValidationException.MessageKeys.UNSUPPORTED_HTTP_METHOD, method.toString());
      }
    }
  }

}