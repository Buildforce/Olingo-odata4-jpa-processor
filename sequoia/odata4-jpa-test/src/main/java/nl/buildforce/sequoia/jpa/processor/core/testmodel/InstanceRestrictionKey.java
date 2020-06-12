package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class InstanceRestrictionKey implements Serializable {

  @Column(name = "\"UserName\"", length = 60)
  private String username;

  @Column(name = "\"SequenceNumber\"")
  private Integer sequenceNumber;

  public InstanceRestrictionKey() {
    // Needed
  }

  public InstanceRestrictionKey(String username, Integer sequenceNumber) {
    this.username = username;
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    InstanceRestrictionKey other = (InstanceRestrictionKey) obj;
    if (sequenceNumber == null) {
      if (other.sequenceNumber != null) return false;
    } else if (!sequenceNumber.equals(other.sequenceNumber)) return false;
    if (username == null) {
        return other.username == null;
    } else return username.equals(other.username);
  }

}