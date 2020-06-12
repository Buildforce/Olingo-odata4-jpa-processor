package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "JoinTarget")
@Table(schema = "\"OLINGO\"", name = "\"JoinTarget\"")
public class JoinTarget {
  @Id
  @Column(name = "\"TargetKey\"")
  private Integer targetID;

}