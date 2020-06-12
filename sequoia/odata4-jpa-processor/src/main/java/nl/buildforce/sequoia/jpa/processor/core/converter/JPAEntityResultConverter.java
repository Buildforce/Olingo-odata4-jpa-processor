package nl.buildforce.sequoia.jpa.processor.core.converter;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.uri.UriHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class JPAEntityResultConverter extends JPAStructuredResultConverter {
  private final EdmEntityType edmEntityType;
  private final UriHelper odataUriHelper;

  public JPAEntityResultConverter(final UriHelper uriHelper, final JPAServiceDocument sd, final List<?> jpaQueryResult,
      final EdmEntityType returnType) {
    super(jpaQueryResult, sd.getEntity(returnType));
    this.edmEntityType = returnType;
    this.odataUriHelper = uriHelper;
  }

  @Override
  public EntityCollection getResult() throws ODataApplicationException, SerializerException, URISyntaxException {
    final EntityCollection odataEntityCollection = new EntityCollection();
    final List<Entity> odataResults = odataEntityCollection.getEntities();

    for (final Object row : jpaQueryResult) {
      final Entity odataEntity = new Entity();
      odataEntity.setType(this.jpaTopLevelType.getExternalFQN().getFullQualifiedNameAsString());
      final List<Property> properties = odataEntity.getProperties();
      convertProperties(row, properties, jpaTopLevelType);
      odataEntity.setId(new URI(odataUriHelper.buildKeyPredicate(edmEntityType, odataEntity)));
      odataResults.add(odataEntity);
    }
    return odataEntityCollection;
  }

}