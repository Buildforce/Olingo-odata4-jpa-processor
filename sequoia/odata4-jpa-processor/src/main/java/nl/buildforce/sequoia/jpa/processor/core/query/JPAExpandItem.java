package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import org.apache.olingo.server.api.uri.UriInfoResource;

public interface JPAExpandItem extends UriInfoResource {

  JPAEntityType getEntityType();

}