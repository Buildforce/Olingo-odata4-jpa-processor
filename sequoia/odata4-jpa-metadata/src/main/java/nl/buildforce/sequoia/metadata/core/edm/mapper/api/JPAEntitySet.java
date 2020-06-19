package nl.buildforce.sequoia.metadata.core.edm.mapper.api;

public interface JPAEntitySet extends JPAElement {

  JPAEntityType getODataEntityType();

  JPAEntityType getEntityType();

}