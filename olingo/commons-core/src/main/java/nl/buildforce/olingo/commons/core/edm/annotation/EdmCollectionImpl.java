/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmCollection;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlCollection;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlExpression;

public class EdmCollectionImpl extends AbstractEdmDynamicExpression implements EdmCollection {

  private List<EdmExpression> items;
  private final CsdlCollection csdlCollection;

  public EdmCollectionImpl(Edm edm, CsdlCollection csdlExp) {
    super(edm, "Collection");
      csdlCollection = csdlExp;
  }

  @Override
  public List<EdmExpression> getItems() {
    if (items == null) {
      List<EdmExpression> localItems = new ArrayList<>();
      if (csdlCollection.getItems() != null) {
        for (CsdlExpression item : csdlCollection.getItems()) {
          localItems.add(getExpression(edm, item));
        }
      }
      items = Collections.unmodifiableList(localItems);
    }
    return items;
  }

  @Override
  public EdmExpressionType getExpressionType() {
    return EdmExpressionType.Collection;
  }
}