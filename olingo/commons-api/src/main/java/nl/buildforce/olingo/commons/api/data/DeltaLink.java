/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.net.URI;
import java.util.Objects;

/**
 * A delta link.
 */
public class DeltaLink extends Annotatable {

  private URI source;
  private String relationship;
  private URI target;

  /**
   * Get source of this link.
   * @return source of this link
   */
  public URI getSource() {
    return source;
  }

// --Commented out by Inspection START (''21-03-11 20:28):
//  /**
//   * Set source of this link.
//   * @param source source of this link
//   */
//  public void setSource(URI source) {
//    this.source = source;
//  }
// --Commented out by Inspection STOP (''21-03-11 20:28)

  /**
   * Get relationship of this link.
   * @return relationship of this link
   */
  public String getRelationship() {
    return relationship;
  }

// --Commented out by Inspection START (''21-03-11 20:28):
//  /**
//   * Set relationship of this link.
//   * @param relationship relationship of this link
//   */
////  public void setRelationship(String relationship) {
////    this.relationship = relationship;
////  }
//// --Commented out by Inspection STOP (''21-03-11 20:28)

  /**
   * Get target of this link.
   * @return target of this link
   */
  public URI getTarget() {
    return target;
  }

  /**
   * Set target of this link.
   * @param target target of this link
   */
  public void setTarget(URI target) {
    this.target = target;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DeltaLink other = (DeltaLink) o;
    return getAnnotations().equals(other.getAnnotations())
        && (Objects.equals(source, other.source))
        && (Objects.equals(relationship, other.relationship))
        && (Objects.equals(target, other.target));
  }

  @Override
  public int hashCode() {
    int result = getAnnotations().hashCode();
    result = 31 * result + (source == null ? 0 : source.hashCode());
    result = 31 * result + (relationship == null ? 0 : relationship.hashCode());
    result = 31 * result + (target == null ? 0 : target.hashCode());
    return result;
  }

}