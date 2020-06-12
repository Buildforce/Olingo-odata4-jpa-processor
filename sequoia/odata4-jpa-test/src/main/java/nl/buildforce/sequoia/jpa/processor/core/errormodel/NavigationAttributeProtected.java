package nl.buildforce.sequoia.jpa.processor.core.errormodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Version;
import java.util.List;

@Entity(name = "NavigationAttributeProtected")
@DiscriminatorValue(value = "1")
public class NavigationAttributeProtected {

  @Id
  @Column(name = "\"ID\"")
  protected String iD;

  @Version
  @Column(name = "\"ETag\"", nullable = false)
  protected long eTag;

  @ManyToMany
  @JoinTable(name = "\"Membership\"", schema = "\"OLINGO\"",
      joinColumns = @JoinColumn(name = "\"PersonID\""),
      inverseJoinColumns = @JoinColumn(name = "\"TeamID\""))
  @EdmProtectedBy(name = "WrongAnnotation")
  private List<Team> teams;

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
    NavigationAttributeProtected other = (NavigationAttributeProtected) obj;
    if (iD == null) {
        return other.iD == null;
    } else return iD.equals(other.iD);
  }

}