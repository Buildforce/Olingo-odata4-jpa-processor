package nl.buildforce.sequoia.jpa.processor.core.errormodel;

import nl.buildforce.sequoia.jpa.processor.core.testmodel.AddressDeepProtected;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Version;
import java.util.List;

@Entity(name = "PersonDeepCollectionProtected")

public class PersonDeepCollectionProtected {// #NOSONAR use equal method from
  @Id
  @Column(name = "\"ID\"")
  protected String iD;

  @Version
  @Column(name = "\"ETag\"", nullable = false)
  protected long eTag; // BusinessPartner

  @Column(name = "\"Type\"", length = 1, insertable = false, updatable = false, nullable = false)
  protected final String type;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(schema = "\"OLINGO\"", name = "\"InhouseAddress\"",
      joinColumns = @JoinColumn(name = "\"ID\""))
  private List<AddressDeepProtected> inhouseAddress;

  public PersonDeepCollectionProtected() {
    type = "1";
  }

}