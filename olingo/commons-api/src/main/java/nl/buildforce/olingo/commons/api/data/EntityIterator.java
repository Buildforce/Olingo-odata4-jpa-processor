/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import nl.buildforce.olingo.commons.api.ex.ODataNotSupportedException;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

/**
 * Data representation as an Iterator for a collection of single entities.
 */
public abstract class EntityIterator extends AbstractEntityCollection implements Iterator<Entity> {
  
  private URI next;
  
  private Integer count;
  /**
   * {@inheritDoc}
   */
  public abstract boolean hasNext();
  /**
   * {@inheritDoc}
   * <p/>
   * Which is an Entity for this type of iterator.
   */
  public abstract Entity next();

  /**
   * {@inheritDoc}
   * <p/>
   * <b>ATTENTION:</b> <code>remove</code> is not supported by default.
   */
  @Override
  public void remove() {
    //"Remove is not supported for iteration over Entities."
    throw new ODataNotSupportedException("Entity Iterator does not support remove()");
  }

  /**
   * {@inheritDoc}
   * <p/>
   * <b>ATTENTION:</b> <code>getOperations</code> is not supported by default.
   */
  @Override
  public List<Operation> getOperations() {
    //"Remove is not supported for iteration over Entities."
    throw new ODataNotSupportedException("Entity Iterator does not support getOperations() by default");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<Entity> iterator() {
    return this;
  }

  /**
   * Gets count
   * 
   */
  public Integer getCount() {
    return count;
  }

  /**
   * Gets next link.
   *
   */
  public URI getNext() {
    return next;
  }

  /**
   * {@inheritDoc}
   * <p/>
   * <b>ATTENTION:</b> <code>getDeltaLink</code> is not supported by default.
   */
  public URI getDeltaLink() {
    throw new ODataNotSupportedException("Entity Iterator does not support getDeltaLink()");
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
   * Sets count.
   *
   * @param count count value.
   */
  public void setCount(Integer count) {
    this.count = count;
  }
}
