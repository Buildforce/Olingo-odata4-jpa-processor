package nl.buildforce.sequoia.processor.core.api;

public final class JPAODataCRUDHandler extends JPAODataGetHandler {

  public JPAODataCRUDHandler(final JPAODataCRUDContextAccess serviceContext) {
    super(serviceContext);
  }

  @Override
  public JPAODataCRUDRequestContext getJPAODataRequestContext() {
    return (JPAODataCRUDRequestContext) super.getJPAODataRequestContext();
  }

}