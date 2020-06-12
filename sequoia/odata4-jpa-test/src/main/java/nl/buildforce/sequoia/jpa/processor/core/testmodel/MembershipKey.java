package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;

public class MembershipKey implements Serializable {

  /**
   *

   */
  private static final long serialVersionUID = -2197928070426048826L;

  @Id
  @Column(name = "\"PersonID\"", length = 32)
  private String personID;
  @Id
  @Column(name = "\"TeamID\"", length = 32)
  private String teamID;

  public MembershipKey() {
    // Needed to be used as IdClass
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((personID == null) ? 0 : personID.hashCode());
    result = prime * result + ((teamID == null) ? 0 : teamID.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    MembershipKey other = (MembershipKey) obj;
    if (personID == null) {
      if (other.personID != null) return false;
    } else if (!personID.equals(other.personID)) return false;
    if (teamID == null) {
        return other.teamID == null;
    } else return teamID.equals(other.teamID);
  }

}