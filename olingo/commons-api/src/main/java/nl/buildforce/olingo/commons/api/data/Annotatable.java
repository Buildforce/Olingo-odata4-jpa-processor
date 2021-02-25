/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.util.ArrayList;
import java.util.List;

/**
 * An element with instance annotations.
 */
public abstract class Annotatable {

  private final List<Annotation> annotations = new ArrayList<>();

  /**
   * Get Annotations.
   *
   * @return annotations
   */
  public List<Annotation> getAnnotations() {
    return annotations;
  }
}
