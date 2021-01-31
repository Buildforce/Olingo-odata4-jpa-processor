package nl.buildforce.sequoia.processor.core.processor;

import nl.buildforce.olingo.commons.api.data.Annotatable;
import nl.buildforce.olingo.commons.api.data.ComplexValue;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.commons.api.data.ValueType;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;
import nl.buildforce.olingo.server.api.uri.UriHelper;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAOperation;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.processor.core.converter.JPAComplexResultConverter;
import nl.buildforce.sequoia.processor.core.converter.JPAEntityResultConverter;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPASerializerException;
import nl.buildforce.sequoia.processor.core.serializer.JPAOperationSerializer;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract class JPAOperationRequestProcessor extends JPAAbstractRequestProcessor {

  private static final String RESULT = "Result";

  public JPAOperationRequestProcessor(OData odata, JPAODataCRUDContextAccess context,
      JPAODataRequestContextAccess requestContext) throws ODataJPAException {
    super(odata, context, requestContext);
  }

  protected Annotatable convertResult(final Object result, final EdmType returnType,
                                      final JPAOperation jpaOperation) throws ODataApplicationException {

    switch (returnType.getKind()) {
      case PRIMITIVE:
        if (jpaOperation.getResultParameter().isCollection()) {
            final List<Object> response = new ArrayList<>((Collection<?>) result);
          return new Property(null, RESULT, ValueType.COLLECTION_PRIMITIVE, response);
        } else if (result == null) {
          return null;
        }
        return new Property(null, RESULT, ValueType.PRIMITIVE, result);
      case ENTITY:
        return createEntityCollection((EdmEntityType) returnType, result, odata.createUriHelper(), jpaOperation);
      case COMPLEX:
        if (jpaOperation.getResultParameter().isCollection()) {
          return new Property(null, RESULT, ValueType.COLLECTION_COMPLEX, createComplexCollection(
              (EdmComplexType) returnType, result));
        } else if (result == null) {
          return null;
        }
        return new Property(null, RESULT, ValueType.COMPLEX, createComplexValue((EdmComplexType) returnType,
            result));
      default:
        break;
    }
    return null;
  }

  private List<ComplexValue> createComplexCollection(final EdmComplexType returnType, final Object result)
      throws ODataApplicationException {

    final List<Object> jpaQueryResult = new ArrayList<>((Collection<?>) result);
    return new JPAComplexResultConverter(sd, jpaQueryResult, returnType).getResult();
  }

  private ComplexValue createComplexValue(final EdmComplexType returnType, final Object result)
      throws ODataApplicationException {

    final List<Object> jpaQueryResult = new ArrayList<>();
    jpaQueryResult.add(result);
    final List<ComplexValue> valueList = new JPAComplexResultConverter(sd, jpaQueryResult, returnType).getResult();
    return valueList.get(0);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private EntityCollection createEntityCollection(final EdmEntityType returnType, Object result,
                                                  UriHelper createUriHelper, final JPAOperation jpaFunction)
      throws ODataApplicationException {

    final List resultList = new ArrayList();
    if (jpaFunction.getResultParameter().isCollection())
      resultList.addAll((Collection<?>) result);
    else if (result == null)
      return null;
    else
      resultList.add(result);
    try {
      return new JPAEntityResultConverter(createUriHelper, sd, resultList, returnType).getResult();
    } catch (SerializerException | URISyntaxException e) {
      throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.QUERY_RESULT_CONV_ERROR,
          HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }

  protected void serializeResult(final EdmType returnType, final ODataResponse response,
      final ContentType responseFormat, final Annotatable result, final ODataRequest request)
      throws ODataJPASerializerException, SerializerException {

    if (result != null
        && !(result instanceof EntityCollection && ((EntityCollection) result).getEntities().isEmpty())) {

      final SerializerResult serializerResult = ((JPAOperationSerializer) serializer).serialize(result, returnType,
          request);
      createSuccessResponse(response, responseFormat, serializerResult);
    } else {
      response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    }
  }

}