package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(schema = "\"OLINGO\"", name = "\"CountryRestriction\"")
public class CountryRestriction {

  @EmbeddedId
  private InstanceRestrictionKey id;

  @Column(name = "\"From\"", length = 4)
  private String fromCountry;

  @Column(name = "\"To\"", length = 4)
  private String toCountry;

  public CountryRestriction() {
    // Needed for JPA
  }

  public CountryRestriction(InstanceRestrictionKey id) {
    this.id = id;
  }

  public String getFromCountry() {
    return fromCountry;
  }

  public String getToCountry() {
    return toCountry;
  }

  public void setFromCountry(String fromCountry) {
    this.fromCountry = fromCountry;
  }

  public void setToCountry(String toCountry) {
    this.toCountry = toCountry;
  }

}