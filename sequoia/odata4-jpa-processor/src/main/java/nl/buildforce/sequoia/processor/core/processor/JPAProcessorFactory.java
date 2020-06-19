package nl.buildforce.sequoia.processor.core.processor;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.api.JPAODataPage;
import nl.buildforce.sequoia.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.processor.core.exception.JPAIllegalAccessException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.processor.core.modify.JPAConversionHelper;
import nl.buildforce.sequoia.processor.core.query.JPACountQuery;
import nl.buildforce.sequoia.processor.core.query.JPAJoinQuery;
import nl.buildforce.sequoia.processor.core.serializer.JPASerializerFactory;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpHeader;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SystemQueryOptionKind;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public final class JPAProcessorFactory {
  private final JPAODataCRUDContextAccess sessionContext;
  private final JPASerializerFactory serializerFactory;
  private final OData odata;
  private final ServiceMetadata serviceMetadata;

  public JPAProcessorFactory(final OData odata, final ServiceMetadata serviceMetadata,
      final JPAODataCRUDContextAccess context) {
    this.sessionContext = context;
    this.serializerFactory = new JPASerializerFactory(odata, serviceMetadata, context);
    this.odata = odata;
    this.serviceMetadata = serviceMetadata;
  }

  public JPACUDRequestProcessor createCUDRequestProcessor(final UriInfo uriInfo, final ContentType responseFormat,
      final JPAODataRequestContextAccess context, final Map<String, List<String>> header) throws ODataJPAException, SerializerException {

    final JPAODataRequestContextAccess requestContext = new JPAODataRequestContextImpl(uriInfo, serializerFactory
        .createCUDSerializer(responseFormat, uriInfo, Optional.ofNullable(header.get(HttpHeader.ODATA_MAX_VERSION))),
        context);

    return new JPACUDRequestProcessor(odata, serviceMetadata, sessionContext, requestContext,
        new JPAConversionHelper());
  }

  public JPACUDRequestProcessor createCUDRequestProcessor(final UriInfo uriInfo,
      final JPAODataRequestContextAccess context) throws ODataJPAException {

    final JPAODataRequestContextAccess requestContext = new JPAODataRequestContextImpl(uriInfo, context);

    return new JPACUDRequestProcessor(odata, serviceMetadata, sessionContext, requestContext,
        new JPAConversionHelper());
  }

  public JPAActionRequestProcessor createActionProcessor(final UriInfo uriInfo, final ContentType responseFormat,
      final Map<String, List<String>> header, final JPAODataRequestContextAccess context) throws ODataJPAException, ODataApplicationException, SerializerException {

    final JPAODataRequestContextAccess requestContext = new JPAODataRequestContextImpl(uriInfo,
        responseFormat != null ? serializerFactory.createSerializer(responseFormat, uriInfo, Optional.ofNullable(header
            .get(HttpHeader.ODATA_MAX_VERSION))) : null, context);

    return new JPAActionRequestProcessor(odata, sessionContext, requestContext);

  }

  public JPARequestProcessor createProcessor(final UriInfo uriInfo, final ContentType responseFormat,
      final Map<String, List<String>> header, final JPAODataRequestContextAccess context) throws ODataJPAException, ODataApplicationException {

    final List<UriResource> resourceParts = uriInfo.getUriResourceParts();
    final UriResource lastItem = resourceParts.get(resourceParts.size() - 1);
    final JPAODataPage page = getPage(header, uriInfo, context);
    JPAODataRequestContextAccess requestContext;
    try {
      requestContext = new JPAODataRequestContextImpl(page,
              serializerFactory.createSerializer(responseFormat,
                      page.getUriInfo(),
                      Optional.ofNullable(header.get(HttpHeader.ODATA_MAX_VERSION))),
              context);
    } catch (SerializerException | JPAIllegalAccessException | ODataApplicationException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }

    switch (lastItem.getKind()) {
      case count:
        return new JPACountRequestProcessor(odata, sessionContext, requestContext);
      case function:
        checkFunctionPathSupported(resourceParts);
        return new JPAFunctionRequestProcessor(odata, sessionContext, requestContext);
      case complexProperty:
      case primitiveProperty:
      case navigationProperty:
      case entitySet:
      case value:
        checkNavigationPathSupported(resourceParts);
        return new JPANavigationRequestProcessor(odata, serviceMetadata, sessionContext, requestContext);
      default:
        throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_RESOURCE_TYPE,
            HttpStatusCode.NOT_IMPLEMENTED, lastItem.getKind().toString());
    }
  }

  private void checkFunctionPathSupported(final List<UriResource> resourceParts) throws ODataApplicationException {
    if (resourceParts.size() > 2)
      throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_FUNC_WITH_NAVI,
          HttpStatusCode.NOT_IMPLEMENTED);
  }

  private void checkNavigationPathSupported(final List<UriResource> resourceParts) throws ODataApplicationException {
    for (final UriResource resourceItem : resourceParts) {
      if (resourceItem.getKind() != UriResourceKind.complexProperty
          && resourceItem.getKind() != UriResourceKind.primitiveProperty
          && resourceItem.getKind() != UriResourceKind.navigationProperty
          && resourceItem.getKind() != UriResourceKind.entitySet
          && resourceItem.getKind() != UriResourceKind.value)
        throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_RESOURCE_TYPE,
            HttpStatusCode.NOT_IMPLEMENTED, resourceItem.getKind().toString());
    }
  }

  private JPAODataPage getPage(final Map<String, List<String>> headers, final UriInfo uriInfo,
      final JPAODataRequestContextAccess requestContext) throws ODataJPAException, ODataApplicationException {

    JPAODataPage page = new JPAODataPage(uriInfo, 0, Integer.MAX_VALUE, null);
    // Server-Driven-Paging
    if (serverDrivenPaging(uriInfo)) {
      final String skiptoken = skipToken(uriInfo);
      if (skiptoken != null && !skiptoken.isEmpty()) {
        page = sessionContext.getPagingProvider().getNextPage(skiptoken);
        if (page == null)
          throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.QUERY_SERVER_DRIVEN_PAGING_GONE, HttpStatusCode.GONE, skiptoken);
      } else {
        final JPACountQuery countQuery = new JPAJoinQuery(odata, sessionContext, headers,
            new JPAODataRequestContextImpl(uriInfo, requestContext));
        final Integer preferredPagesize = getPreferredPageSize(headers);
        final JPAODataPage firstPage = sessionContext.getPagingProvider().getFirstPage(uriInfo, preferredPagesize,
            countQuery, requestContext.getEntityManager());
        page = firstPage != null ? firstPage : page;
      }
    }
    return page;
  }

  private Integer getPreferredPageSize(final Map<String, List<String>> headers) throws ODataJPAProcessorException {

    final List<String> preferredHeaders = getHeader("Prefer", headers);
    if (preferredHeaders != null) {
      for (String header : preferredHeaders) {
        if (header.startsWith("odata.maxpagesize")) {
          try {
            return Integer.valueOf((header.split("=")[1]));
          } catch (NumberFormatException e) {
            throw new ODataJPAProcessorException(e, HttpStatusCode.BAD_REQUEST);
          }
        }
      }
    }
    return null;
  }

  private boolean serverDrivenPaging(final UriInfo uriInfo) throws ODataJPAProcessorException {

    for (SystemQueryOption option : uriInfo.getSystemQueryOptions()) {
      if (option.getKind() == SystemQueryOptionKind.SKIPTOKEN
          && sessionContext.getPagingProvider() == null)
        throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.QUERY_SERVER_DRIVEN_PAGING_NOT_IMPLEMENTED,
            HttpStatusCode.NOT_IMPLEMENTED);
    }
    final List<UriResource> resourceParts = uriInfo.getUriResourceParts();
    return sessionContext.getPagingProvider() != null
        && resourceParts.get(resourceParts.size() - 1).getKind() != UriResourceKind.function;
  }

  private String skipToken(final UriInfo uriInfo) {
    for (SystemQueryOption option : uriInfo.getSystemQueryOptions()) {
      if (option.getKind() == SystemQueryOptionKind.SKIPTOKEN)
        return option.getText();
    }
    return null;
  }

  private List<String> getHeader(final String name, final Map<String, List<String>> headers) {
    for (final Entry<String, List<String>> header : headers.entrySet()) {
      if (header.getKey().equalsIgnoreCase(name)) {
        return header.getValue();
      }
    }
    return Collections.emptyList();
  }
}