package nl.buildforce.sequoia.jpa.processor.core.errormodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(schema = "\"OLINGO\"", name = "\"BusinessPartner\"")
public class ComplexProtectedNoPath {

  @Id
  @Column(name = "\"ID\"")
  protected String iD;

  @Version
  @Column(name = "\"ETag\"", nullable = false)
  protected long eTag;

  @Embedded
  @EdmProtectedBy(name = "UserId")
  private final AdministrativeInformation administrativeInformation = new AdministrativeInformation();

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
    ComplexProtectedNoPath other = (ComplexProtectedNoPath) obj;
    if (iD == null) {
        return other.iD == null;
    } else return iD.equals(other.iD);
  }

  public String getiD() {
    return iD;
  }

  public long geteTag() {
    return eTag;
  }

  public void setiD(String iD) {
    this.iD = iD;
  }

  public void seteTag(long eTag) {
    this.eTag = eTag;
  }

}