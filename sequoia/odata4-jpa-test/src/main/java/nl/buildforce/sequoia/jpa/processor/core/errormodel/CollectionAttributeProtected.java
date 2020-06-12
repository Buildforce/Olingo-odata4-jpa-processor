package nl.buildforce.sequoia.jpa.processor.core.errormodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.InhouseAddress;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Version;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "CollectionAttributeProtected")
@DiscriminatorValue(value = "1")
public class CollectionAttributeProtected {

  @Id
  @Column(name = "\"ID\"")
  protected String iD;

  @Version
  @Column(name = "\"ETag\"", nullable = false)
  protected long eTag;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(schema = "\"OLINGO\"", name = "\"InhouseAddress\"",
      joinColumns = @JoinColumn(name = "\"ID\""))
  @EdmProtectedBy(name = "WrongAnnotation")
  private List<InhouseAddress> inhouseAddress = new ArrayList<>();

  public List<InhouseAddress> getInhouseAddress() {
    return inhouseAddress;
  }

  public void setInhouseAddress(final List<InhouseAddress> inhouseAddress) {
    this.inhouseAddress = inhouseAddress;
  }

  public void addInhouseAddress(final InhouseAddress address) {
    inhouseAddress.add(address);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((iD == null) ? 0 : iD.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    CollectionAttributeProtected other = (CollectionAttributeProtected) obj;
    if (iD == null) {
        return other.iD == null;
    } else return iD.equals(other.iD);
  }

}