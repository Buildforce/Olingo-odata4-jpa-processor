/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A Delta instance contains all added and deleted links and all deleted entities.
 */
public class Delta extends EntityCollection {

  private final List<DeletedEntity> deletedEntities = new ArrayList<>();
  private final List<DeltaLink> addedLinks = new ArrayList<>();
  private final List<DeltaLink> deletedLinks = new ArrayList<>();

  /**
   * Get list of deleted entities (must not be NULL).
   * @return list of deleted entities (must not be NULL)
   */
  public List<DeletedEntity> getDeletedEntities() {
    return deletedEntities;
  }

  /**
   * Get list of added links (must not be NULL).
   * @return list of added links (must not be NULL)
   */
  public List<DeltaLink> getAddedLinks() {
    return addedLinks;
  }

  /**
   * Get list of deleted links (must not be NULL).
   * @return list of deleted links (must not be NULL)
   */
  public List<DeltaLink> getDeletedLinks() {
    return deletedLinks;
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o)
        && deletedEntities.equals(((Delta) o).deletedEntities)
        && addedLinks.equals(((Delta) o).addedLinks)
        && deletedLinks.equals(((Delta) o).deletedLinks);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + deletedEntities.hashCode();
    result = 31 * result + addedLinks.hashCode();
    result = 31 * result + deletedLinks.hashCode();
    return result;
  }
}
