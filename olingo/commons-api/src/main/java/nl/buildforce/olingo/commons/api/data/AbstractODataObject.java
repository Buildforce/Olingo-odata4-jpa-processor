/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.net.URI;
import java.util.Objects;

/**
 * Abstract OData object with basic values (<code>id</code>, <code>baseURI</code>, <code>title</code>).
 */
public abstract class AbstractODataObject extends Annotatable {

  private URI baseURI;
  private URI id;
  private String title;

// --Commented out by Inspection START (''21-03-11 21:10):
//  /**
//   * Gets base URI.
//   * @return base URI
//   */
//  public URI getBaseURI() {
//    return baseURI;
//  }
// --Commented out by Inspection STOP (''21-03-11 21:10)

  /**
   * Sets base URI.
   * @param baseURI new base URI
   */
  public void setBaseURI(URI baseURI) {
    this.baseURI = baseURI;
  }

  /**
   * Gets ID.
   * @return ID.
   */
  public URI getId() {
    return id;
  }

  /**
   * Sets ID.
   * @param id new ID value
   */
  public void setId(URI id) {
    this.id = id;
  }

  /**
   * Gets title.
   * @return title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets property with given key to given value.
   * @param key key of property
   * @param value new value for property
   */
  public void setCommonProperty(String key, String value) {
    if ("id".equals(key)) {
      id = URI.create(value);
    } else if ("title".equals(key)) {
      title = value;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AbstractODataObject other = (AbstractODataObject) o;
    return getAnnotations().equals(other.getAnnotations())
        && (Objects.equals(baseURI, other.baseURI))
        && (Objects.equals(id, other.id))
        && (Objects.equals(title, other.title));
  }

  @Override
  public int hashCode() {
    int result = getAnnotations().hashCode();
    result = 31 * result + (baseURI == null ? 0 : baseURI.hashCode());
    result = 31 * result + (id == null ? 0 : id.hashCode());
    result = 31 * result + (title == null ? 0 : title.hashCode());
    return result;
  }

}