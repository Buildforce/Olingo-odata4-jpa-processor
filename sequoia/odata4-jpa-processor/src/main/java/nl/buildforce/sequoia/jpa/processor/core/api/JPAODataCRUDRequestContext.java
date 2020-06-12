package nl.buildforce.sequoia.jpa.processor.core.api;

public interface JPAODataCRUDRequestContext extends JPAODataRequestContext {
  void setCUDRequestHandler(final JPACUDRequestHandler jpaCUDRequestHandler);
}