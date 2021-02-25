/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Data representation for a collection of single entities.
 */
public class EntityCollection extends AbstractEntityCollection {

  private final List<Entity> entities = new ArrayList<>();
  private Integer count;
  private URI next;
  private URI deltaLink;
  private final List<Operation> operations = new ArrayList<>();
  
  /**
   * Sets number of entries.
   *
   * @param count number of entries
   */
  public void setCount(Integer count) {
    this.count = count;
  }

  /**
   * Gets number of entries - if it was required.
   *
   * @return number of entries into the entity set.
   */
  @Override
  public Integer getCount() {
    return count;
  }

  /**
   * Gets entities.
   *
   * @return entries.
   */
  public List<Entity> getEntities() {
    return entities;
  }

  /**
   * Sets next link.
   *
   * @param next next link.
   */
  public void setNext(URI next) {
    this.next = next;
  }

  /**
   * Gets next link if exists.
   *
   * @return next link if exists; null otherwise.
   */
  @Override
  public URI getNext() {
    return next;
  }

  /**
   * Gets delta link if exists.
   *
   * @return delta link if exists; null otherwise.
   */
  @Override
  public URI getDeltaLink() {
    return deltaLink;
  }

  /**
   * Sets delta link.
   *
   * @param deltaLink delta link.
   */
  public void setDeltaLink(URI deltaLink) {
    this.deltaLink = deltaLink;
  }
  
  /**
   * Gets operations.
   *
   * @return operations.
   */
  @Override
  public List<Operation> getOperations() {
    return operations;
  }  

  @Override
  public Iterator<Entity> iterator() {
    return entities.iterator();
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) {
      return false;
    }
    EntityCollection other = (EntityCollection) o;
    return entities.equals(other.entities)
        && (Objects.equals(count, other.count))
        && (Objects.equals(next, other.next))
        && (Objects.equals(deltaLink, other.deltaLink));
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + entities.hashCode();
    result = 31 * result + (count == null ? 0 : count.hashCode());
    result = 31 * result + (next == null ? 0 : next.hashCode());
    result = 31 * result + (deltaLink == null ? 0 : deltaLink.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return entities.toString();
  }
}