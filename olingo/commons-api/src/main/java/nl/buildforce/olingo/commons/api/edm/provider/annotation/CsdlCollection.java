/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * The edm:Collection expression enables a value to be obtained from zero or more child expressions.
 * The value calculated by the collection expression is the collection of the values calculated
 * by each of the child expressions.
 */
public class CsdlCollection extends CsdlDynamicExpression {

  private List<CsdlExpression> items = new ArrayList<>();

  /**
   * Returns a list of child expression
   * @return List of child expression
   */
  public List<CsdlExpression> getItems() {
    return items;
  }

  /**
   * Returns a list of child expression
   * @return List of child expression
   */
  public CsdlCollection setItems(List<CsdlExpression> items) {
    this.items = items;
    return this;
  }
  
  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlCollection)) {
      return false;
    }
    CsdlCollection annotColl = (CsdlCollection) obj;
    return (getItems() == null ? annotColl.getItems() == null :
      checkItems(annotColl.getItems()));
  }
  
  private boolean checkItems(List<CsdlExpression> annotCollItems) {
    if (annotCollItems == null) {
      return false;
    }
    if (getItems().size() == annotCollItems.size()) {
      for (int i = 0; i < getItems().size(); i++) {
        if (!getItems().get(i).equals(annotCollItems.get(i))) {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((items == null) ? 0 : items.hashCode());
    return result;
  }

}