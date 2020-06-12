package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

public interface JPAEntitySet extends JPAElement {

  JPAEntityType getODataEntityType();

  JPAEntityType getEntityType();

}