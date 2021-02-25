/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

public class TargetQualifierMapKey {

  private final FullQualifiedName targetName;
  private final String qualifier;

  public TargetQualifierMapKey(FullQualifiedName targetName, String qualifier) {
    if (targetName == null) {
      throw new EdmException("targetName for TargetQualifierMapKey must not be null.");
    }
    this.targetName = targetName;
    this.qualifier = qualifier;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((qualifier == null) ? 0 : qualifier.hashCode());
    result = prime * result + ((targetName == null) ? 0 : targetName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof TargetQualifierMapKey)) {
      return false;
    }
    TargetQualifierMapKey other = (TargetQualifierMapKey) obj;
    if (qualifier == null) {
      if (other.qualifier != null) {
        return false;
      }
    } else if (!qualifier.equals(other.qualifier)) {
      return false;
    }
    if (targetName == null) {
      return other.targetName == null;
    } else return targetName.equals(other.targetName);
  }

}
