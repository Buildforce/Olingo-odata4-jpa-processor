package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmSearchable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(schema = "\"OLINGO\"", name = "\"AdministrativeDivisionDescription\"")
public class AdministrativeDivisionDescription implements KeyAccess {

  @EmbeddedId
  private AdministrativeDivisionDescriptionKey key;

  @EdmSearchable
  @Column(name = "\"Name\"", length = 100, updatable = false)
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public AdministrativeDivisionDescriptionKey getKey() {
    return key;
  }

  public void setKey(AdministrativeDivisionDescriptionKey key) {
    this.key = key;
  }

}