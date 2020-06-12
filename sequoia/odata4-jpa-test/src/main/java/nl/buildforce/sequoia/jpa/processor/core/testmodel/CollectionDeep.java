package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "\"OLINGO\"", name = "\"CollectionsDeep\"")
public class CollectionDeep {

  @Id
  @Column(name = "\"ID\"")
  private String iD;

  // Collection as part of nested complex
  @Embedded
  private CollectionFirstLevelComplex firstLevel;

  public void setID(String iD) {
    this.iD = iD;
  }

  public void setFirstLevel(CollectionFirstLevelComplex firstLevel) {
    this.firstLevel = firstLevel;
  }

  public String getID() {
    return iD;
  }

  public CollectionFirstLevelComplex getFirstLevel() {
    return firstLevel;
  }

}