package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmProtectedBy;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmProtections;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.sql.Timestamp;

@Entity(name = "PersonProtected")
@Table(schema = "\"OLINGO\"", name = "PersonProtected")
public class PersonDeepProtected {// #NOSONAR use equal method from BusinessPartner
//  CREATE VIEW
//  AS

//SELECT b."ID",b."ETag",b."NameLine1",b."NameLine2",b."CreatedBy",b."CreatedAt",b."UpdatedBy",b."UpdatedAt"

  @Id
  @Column(name = "\"ID\"")
  protected String iD;

  @Version
  @Column(name = "\"ETag\"", nullable = false)
  protected long eTag;

  @Column(name = "\"NameLine1\"")
  private String firstName;

  @Column(name = "\"NameLine2\"")
  private String lastName;

  @Column(name = "\"CreatedAt\"", precision = 3, insertable = false, updatable = false)
  private Timestamp creationDateTime;

  @Embedded
  private AddressDeepProtected inhouseAddress;

  @Embedded
  @EdmProtections({
      @EdmProtectedBy(name = "Creator", path = "created/by"),
      @EdmProtectedBy(name = "Updater", path = "updated/by")
  })
  private final AdministrativeInformation protectedAdminInfo = new AdministrativeInformation();

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
    PersonDeepProtected other = (PersonDeepProtected) obj;
    if (iD == null) {
        return other.iD == null;
    } else return iD.equals(other.iD);
  }

}