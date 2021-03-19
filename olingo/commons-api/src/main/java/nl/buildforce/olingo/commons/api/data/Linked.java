/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Data representation for a linked object.
 */
public abstract class Linked extends AbstractODataObject {

  private final List<Link> associationLinks = new ArrayList<>();
  private final List<Link> navigationLinks = new ArrayList<>();
  private final List<Link> bindingLinks = new ArrayList<>();

  protected Link getOneByTitle(String name, List<Link> links) {
    Link result = null;

    for (Link link : links) {
      if (name.equals(link.getTitle())) {
        result = link;
      }
    }

    return result;
  }

  /**
   * Gets association link with given name, if available, otherwise <tt>null</tt>.
   *
   * @param name candidate link name
   * @return association link with given name, if available, otherwise <tt>null</tt>
   */
  public Link getAssociationLink(String name) {
    return getOneByTitle(name, associationLinks);
  }

  /**
   * Gets association links.
   *
   * @return association links.
   */
  public List<Link> getAssociationLinks() {
    return associationLinks;
  }

  /**
   * Gets navigation link with given name, if available, otherwise <tt>null</tt>.
   *
   * @param name candidate link name
   * @return navigation link with given name, if available, otherwise <tt>null</tt>
   */
  public Link getNavigationLink(String name) {
    return getOneByTitle(name, navigationLinks);
  }

  /**
   * Gets navigation links.
   *
   * @return links.
   */
  public List<Link> getNavigationLinks() {
    return navigationLinks;
  }

// --Commented out by Inspection START (''21-03-11 19:38):
//  /**
//   * Gets binding link with given name, if available, otherwise <tt>null</tt>.
//   * @param name candidate link name
//   * @return binding link with given name, if available, otherwise <tt>null</tt>
//   */
//  public Link getNavigationBinding(String name) {
//    return getOneByTitle(name, bindingLinks);
//  }
// --Commented out by Inspection STOP (''21-03-11 19:38)

  /**
   * Gets binding links.
   *
   * @return links.
   */
  public List<Link> getNavigationBindings() {
    return bindingLinks;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o)
        && associationLinks.equals(((Linked) o).associationLinks)
        && navigationLinks.equals(((Linked) o).navigationLinks)
        && bindingLinks.equals(((Linked) o).bindingLinks);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + associationLinks.hashCode();
    result = 31 * result + navigationLinks.hashCode();
    result = 31 * result + bindingLinks.hashCode();
    return result;
  }

}