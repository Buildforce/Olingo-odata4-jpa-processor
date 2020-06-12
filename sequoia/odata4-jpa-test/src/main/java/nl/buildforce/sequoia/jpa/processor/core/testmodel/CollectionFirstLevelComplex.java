package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class CollectionFirstLevelComplex {

  @Column(name = "\"LevelID\"")
  private Integer levelID;

  @Embedded
  private CollectionSecondLevelComplex secondLevel;

  public void setLevelID(Integer levelID) {
    this.levelID = levelID;
  }

  public void setSecondLevel(CollectionSecondLevelComplex secondLevel) {
    this.secondLevel = secondLevel;
  }

  public Integer getLevelID() {
    return levelID;
  }

  public CollectionSecondLevelComplex getSecondLevel() {
    return secondLevel;
  }

}