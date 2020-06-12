package nl.buildforce.sequoia.jpa.processor.core.modify;

public final class JPAUpdateResult {
  private final boolean wasCreate;
  private final Object modifiedEntity;

  public JPAUpdateResult(boolean wasCreate, Object modifiedEntity) {
    this.wasCreate = wasCreate;
    this.modifiedEntity = modifiedEntity;
  }

  public boolean wasCreate() {
    return wasCreate;
  }

  public Object getModifiedEntity() {
    return modifiedEntity;
  }

}