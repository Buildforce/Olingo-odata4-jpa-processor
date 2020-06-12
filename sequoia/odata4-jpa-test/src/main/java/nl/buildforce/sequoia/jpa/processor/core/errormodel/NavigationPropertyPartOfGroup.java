/**
 *
 */
package nl.buildforce.sequoia.jpa.processor.core.errormodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmVisibleFor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Version;
import java.util.List;

/**
 * @author Oliver Grande
 * Created: 29.06.2019
 *
 */
@Entity(name = "NavigationPropertyPartOfGroup")
public class NavigationPropertyPartOfGroup {
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
  @EdmVisibleFor("Person")
  private List<Team> teams;

}