package nl.buildforce.sequoia.jpa.processor.core.serializer;

import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPASerializerException;
import org.apache.olingo.commons.api.data.Annotatable;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.serializer.ComplexSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;

import java.net.URISyntaxException;

public class JPASerializeComplexCollection implements JPAOperationSerializer {
  private final ServiceMetadata serviceMetadata;
  private final ODataSerializer serializer;
  private final ContentType responseFormat;
  private final JPAODataCRUDContextAccess serviceContext;

  JPASerializeComplexCollection(final ServiceMetadata serviceMetadata, final ODataSerializer serializer,
      final ContentType responseFormat, final JPAODataCRUDContextAccess context) {

    this.serializer = serializer;
    this.serviceMetadata = serviceMetadata;
    this.responseFormat = responseFormat;
    this.serviceContext = context;
  }

  @Override
  public SerializerResult serialize(ODataRequest request, EntityCollection result) {
    return null;
  }

  @Override
  public SerializerResult serialize(final Annotatable result, final EdmType complexType, final ODataRequest request)
          throws SerializerException, ODataJPASerializerException {

    try {
      final ContextURL contextUrl = ContextURL.with()
              .serviceRoot(buildServiceRoot(request, serviceContext))
              .asCollection()
              .build();
      final ComplexSerializerOptions options = ComplexSerializerOptions.with().contextURL(contextUrl).build();

      return serializer.complexCollection(serviceMetadata, (EdmComplexType) complexType, (Property) result,
              options);
    } catch (final URISyntaxException e) {
      throw new ODataJPASerializerException(e, HttpStatusCode.BAD_REQUEST);
    }

/*
    final ContextURL contextUrl = ContextURL.with().asCollection().build();
    final ComplexSerializerOptions options = ComplexSerializerOptions.with().contextURL(contextUrl).build();

    return serializer.complexCollection(serviceMetadata, (EdmComplexType) complexType, (Property) result,
        options);
*/
  }

  @Override
  public ContentType getContentType() {
    return responseFormat;
  }

}