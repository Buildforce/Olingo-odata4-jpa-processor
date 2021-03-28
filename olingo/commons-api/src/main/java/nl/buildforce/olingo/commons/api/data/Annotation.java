/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.util.Objects;

/**
 * Represents an instance annotation.
 */
public class Annotation extends Valuable {

  private String term;

  /**
   * Get term for Annotation.
   * @return term for Annotation.
   */
  public String getTerm() {
    return term;
  }

// --Commented out by Inspection START (''21-03-28 07:14):
//  /**
//   * Set term for Annotation.
//   * @param term term for Annotation.
//   */
//  public void setTerm(String term) {
//    this.term = term;
//  }
// --Commented out by Inspection STOP (''21-03-28 07:14)

  @Override
  public boolean equals(Object o) {
    return super.equals(o)
        && (Objects.equals(term, ((Annotation) o).term));
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (term == null ? 0 : term.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return term == null ? "null" : term;
  }
}