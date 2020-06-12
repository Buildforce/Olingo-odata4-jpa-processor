package nl.buildforce.sequoia.jpa.processor.core.errormodel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Embeddable
public class ChangeInformation {

  @Column
  private String by;
  @Column(precision = 9)
  @Temporal(TemporalType.TIMESTAMP)
  private Date at;

  String user;

  public ChangeInformation() {}

  public ChangeInformation(String by, Date at) {
    this.by = by;
    this.at = at;
  }

}