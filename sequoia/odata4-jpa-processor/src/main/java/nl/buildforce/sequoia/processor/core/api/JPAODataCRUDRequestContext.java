package nl.buildforce.sequoia.processor.core.api;

public interface JPAODataCRUDRequestContext extends JPAODataRequestContext {
  void setCUDRequestHandler(final JPACUDRequestHandler jpaCUDRequestHandler);
}