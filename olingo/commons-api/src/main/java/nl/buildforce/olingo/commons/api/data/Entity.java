/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data representation for a single entity.
 */
public class Entity extends Linked {

  private String eTag;
  private String type;

  private Link readLink;
  private Link editLink;

  private final List<Link> mediaEditLinks = new ArrayList<>();
  private final List<Operation> operations = new ArrayList<>();

  private final List<Property> properties = new ArrayList<>();

  private URI mediaContentSource;
  private String mediaContentType;
  private String mediaETag;

  /**
   * Gets ETag.
   *
   * @return ETag.
   */
  public String getETag() {
    return eTag;
  }
  
  /**
   * Sets ETag
   * @param eTag ETag
   */
  public void setETag(String eTag) {
    this.eTag = eTag;
  }

  /**
   * Gets entity type.
   *
   * @return entity type.
   */
  public String getType() {
    return type;
  }

  /**
   * Sets entity type.
   *
   * @param type entity type.
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gets entity self link.
   *
   * @return self link.
   */
  public Link getSelfLink() {
    return readLink;
  }

  /**
   * Sets entity self link.
   *
   * @param selfLink self link.
   */
  public void setSelfLink(Link selfLink) {
    readLink = selfLink;
  }

  /**
   * Gets entity edit link.
   *
   * @return edit link.
   */
  public Link getEditLink() {
    return editLink;
  }

  /**
   * Sets entity edit link.
   *
   * @param editLink edit link.
   */
  public void setEditLink(Link editLink) {
    this.editLink = editLink;
  }

  /**
   * Gets media entity links.
   *
   * @return links.
   */
  public List<Link> getMediaEditLinks() {
    return mediaEditLinks;
  }

  /**
   * Gets operations.
   *
   * @return operations.
   */
  public List<Operation> getOperations() {
    return operations;
  }

  /**
   * Add property to this Entity.
   *
   * @param property property which is added
   * @return this Entity for fluid/flow adding
   */
  public Entity addProperty(Property property) {
    properties.add(property);
    return this;
  }

  /**
   * Gets properties.
   *
   * @return properties.
   */
  public List<Property> getProperties() {
    return properties;
  }

  /**
   * Gets property with given name.
   *
   * @param name property name
   * @return property with given name if found, null otherwise
   */
  public Property getProperty(String name) {
    Property result = null;

    for (Property property : properties) {
      if (name.equals(property.getName())) {
        result = property;
        break;
      }
    }

    return result;
  }

  /**
   * Gets media content type.
   *
   * @return media content type.
   */
  public String getMediaContentType() {
    return mediaContentType;
  }

  /**
   * Set media content type.
   *
   * @param mediaContentType media content type.
   */
  public void setMediaContentType(String mediaContentType) {
    this.mediaContentType = mediaContentType;
  }

  /**
   * Gets media content resource.
   *
   * @return media content resource.
   */
  public URI getMediaContentSource() {
    return mediaContentSource;
  }

  /**
   * Set media content source.
   *
   * @param mediaContentSource media content source.
   */
  public void setMediaContentSource(URI mediaContentSource) {
    this.mediaContentSource = mediaContentSource;
  }

  /**
   * ETag of the binary stream represented by this media entity or named stream property.
   *
   * @return media ETag value
   */
  public String getMediaETag() {
    return mediaETag;
  }

  /**
   * Set media ETag.
   *
   * @param eTag media ETag value
   */
  public void setMediaETag(String eTag) {
    mediaETag = eTag;
  }

  /**
   * Checks if the current entity is a media entity.
   *
   * @return 'TRUE' if is a media entity; 'FALSE' otherwise.
   */
  public boolean isMediaEntity() {
    return mediaContentSource != null;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o)
        && (Objects.equals(eTag, ((Entity) o).eTag))
        && (Objects.equals(type, ((Entity) o).type))
        && (Objects.equals(readLink, ((Entity) o).readLink))
        && (Objects.equals(editLink, ((Entity) o).editLink))
        && mediaEditLinks.equals(((Entity) o).mediaEditLinks)
        && operations.equals(((Entity) o).operations)
        && properties.equals(((Entity) o).properties)
        && (Objects.equals(mediaContentSource, ((Entity) o).mediaContentSource))
        && (Objects.equals(mediaContentType, ((Entity) o).mediaContentType))
        && (Objects.equals(mediaETag, ((Entity) o).mediaETag));
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (eTag == null ? 0 : eTag.hashCode());
    result = 31 * result + (type == null ? 0 : type.hashCode());
    result = 31 * result + (readLink == null ? 0 : readLink.hashCode());
    result = 31 * result + (editLink == null ? 0 : editLink.hashCode());
    result = 31 * result + mediaEditLinks.hashCode();
    result = 31 * result + operations.hashCode();
    result = 31 * result + properties.hashCode();
    result = 31 * result + (mediaContentSource == null ? 0 : mediaContentSource.hashCode());
    result = 31 * result + (mediaContentType == null ? 0 : mediaContentType.hashCode());
    result = 31 * result + (mediaETag == null ? 0 : mediaETag.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return properties.toString();
  }
}