/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data representation for a link.
 */
public class Link extends Annotatable {

  private String title;
  private String rel;
  private String href;
  private String type;
  private String mediaETag;
  private Entity entity;
  private EntityCollection entitySet;
  private String bindingLink;
  private List<String> bindingLinks = new ArrayList<>();

  /**
   * Gets title.
   *
   * @return title.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets title.
   *
   * @param title title.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets rel info.
   *
   * @return rel info.
   */
  public String getRel() {
    return rel;
  }

  /**
   * Sets rel info.
   *
   * @param rel rel info.
   */
  public void setRel(String rel) {
    this.rel = rel;
  }

  /**
   * Gets href.
   *
   * @return href.
   */
  public String getHref() {
    return href;
  }

  /**
   * Sets href.
   *
   * @param href href.
   */
  public void setHref(String href) {
    this.href = href;
  }

  /**
   * Gets type.
   *
   * @return type.
   */
  public String getType() {
    return type;
  }

  /**
   * Sets type.
   *
   * @param type type.
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gets Media ETag.
   *
   * @return media ETag
   */
  public String getMediaETag() {
    return mediaETag;
  }

  /**
   * Sets Media ETag.
   *
   * @param mediaETag media ETag
   */
  public void setMediaETag(String mediaETag) {
    this.mediaETag = mediaETag;
  }

  /**
   * Gets in-line entity.
   *
   * @return in-line entity.
   */
  public Entity getInlineEntity() {
    return entity;
  }

  /**
   * Sets in-line entity.
   *
   * @param entity entity.
   */
  public void setInlineEntity(Entity entity) {
    this.entity = entity;
  }

  /**
   * Gets in-line entity set.
   *
   * @return in-line entity set.
   */
  public EntityCollection getInlineEntitySet() {
    return entitySet;
  }

  /**
   * Sets in-line entity set.
   *
   * @param entitySet entity set.
   */
  public void setInlineEntitySet(EntityCollection entitySet) {
    this.entitySet = entitySet;
  }

  /**
   * If this is a "toOne" relationship this method delivers the binding link or <tt>null</tt> if not set.
   * @return String the binding link.
   */
  public String getBindingLink() {
    return bindingLink;
  }

  /**
   * If this is a "toMany" relationship this method delivers the binding links or <tt>emptyList</tt> if not set.
   * @return a list of binding links.
   */
  public List<String> getBindingLinks() {
    return bindingLinks;
  }

  /**
   * Sets the binding link.
   * @param bindingLink name of binding link
   */
  public void setBindingLink(String bindingLink) {
    this.bindingLink = bindingLink;
  }

  /**
   * Sets the binding links. List MUST NOT be <tt>null</tt>.
   * @param bindingLinks list of binding link names
   */
  public void setBindingLinks(List<String> bindingLinks) {
    this.bindingLinks = bindingLinks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Link other = (Link) o;
    return getAnnotations().equals(other.getAnnotations())
        && (Objects.equals(title, other.title))
        && (Objects.equals(rel, other.rel))
        && (Objects.equals(href, other.href))
        && (Objects.equals(type, other.type))
        && (Objects.equals(mediaETag, other.mediaETag))
        && (Objects.equals(entity, other.entity))
        && (Objects.equals(entitySet, other.entitySet))
        && (Objects.equals(bindingLink, other.bindingLink))
        && bindingLinks.equals(other.bindingLinks);
  }

  @Override
  public int hashCode() {
    int result = getAnnotations().hashCode();
    result = 31 * result + (title == null ? 0 : title.hashCode());
    result = 31 * result + (rel == null ? 0 : rel.hashCode());
    result = 31 * result + (href == null ? 0 : href.hashCode());
    result = 31 * result + (type == null ? 0 : type.hashCode());
    result = 31 * result + (mediaETag == null ? 0 : mediaETag.hashCode());
    result = 31 * result + (entity == null ? 0 : entity.hashCode());
    result = 31 * result + (entitySet == null ? 0 : entitySet.hashCode());
    result = 31 * result + (bindingLink == null ? 0 : bindingLink.hashCode());
    result = 31 * result + bindingLinks.hashCode();
    return result;
  }

}