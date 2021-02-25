/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Super type of all CsdlEdmItems
 */
public abstract class CsdlAbstractEdmItem {

  /**
   * Gets one by name.
   *
   * @param name the name
   * @param items the items
   * @return the one by name
   */
  protected <T extends CsdlNamed> T getOneByName(String name, Collection<T> items) {
    List<T> result = getAllByName(name, items);
    return result.isEmpty() ? null : result.get(0);
  }

  /**
   * Gets all by name.
   *
   * @param name the name
   * @param items the items
   * @return the all by name
   */
  protected <T extends CsdlNamed> List<T> getAllByName(String name, Collection<T> items) {
    List<T> result = new ArrayList<>();
    for (T type : items) {
      if (name.equals(type.getName())) {
        result.add(type);
      }
    }
    return result;
  }

}