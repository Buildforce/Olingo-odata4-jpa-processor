package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.sql.Date;
import java.time.LocalDate;

@Embeddable
public class AdministrativeInformation {
  @Embedded

  @AttributeOverride(name = "by", column = @Column(name = "\"CreatedBy\""))
  @AttributeOverride(name = "at", column = @Column(name = "\"CreatedAt\""))

  @AssociationOverride(name = "user",
      joinColumns = @JoinColumn(referencedColumnName = "\"ID\"", name = "\"CreatedBy\"", insertable = false,
          updatable = false))
  private ChangeInformation created;
  @Embedded

  @AttributeOverride(name = "by", column = @Column(name = "\"UpdatedBy\""))
  @AttributeOverride(name = "at", column = @Column(name = "\"UpdatedAt\""))

  @AssociationOverride(name = "user",
      joinColumns = @JoinColumn(referencedColumnName = "\"ID\"", name = "\"UpdatedBy\"", insertable = false,
          updatable = false))
  private ChangeInformation updated;

  public ChangeInformation getCreated() {
    return created;
  }

  public ChangeInformation getUpdated() {
    return updated;
  }

  public void setCreated(ChangeInformation created) {
    this.created = created;
  }

  public void setUpdated(ChangeInformation updated) {
    this.updated = updated;
  }

  @PrePersist
  void onCreate() {
    created = new ChangeInformation("99", Date.valueOf(LocalDate.now()));
  }

  @PreUpdate
  void onUpdate() {
    updated = new ChangeInformation("99", Date.valueOf(LocalDate.now()));
  }

}