/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmCollection;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmDynamicExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression.EdmExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlCollection;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLogicalOrComparisonExpression;
//CHECKSTYLE:OFF
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLogicalOrComparisonExpression.LogicalOrComparisonExpressionType;
//CHECKSTYLE:ON
import nl.buildforce.olingo.commons.core.edm.annotation.AbstractEdmExpression;
import org.junit.Test;

public class EdmCollectionImplTest extends AbstractAnnotationTest {

  @Test
  public void initialCollection() {
    EdmExpression exp = AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlCollection());

    EdmDynamicExpression dynExp = assertDynamic(exp);
    assertTrue(dynExp.isCollection());
    assertNotNull(dynExp.asCollection());

    assertEquals("Collection", dynExp.getExpressionName());
    assertEquals(EdmExpressionType.Collection, dynExp.getExpressionType());
    assertSingleKindDynamicExpression(dynExp);

    EdmCollection asCollection = dynExp.asCollection();

    assertNotNull(asCollection.getItems());
    assertTrue(asCollection.getItems().isEmpty());
  }

  @Test
  public void collectionWithThreeItems() {
    CsdlCollection csdlCollection = new CsdlCollection();
    List<CsdlExpression> items = new ArrayList<CsdlExpression>();
    items.add(new CsdlConstantExpression(ConstantExpressionType.String));
    items.add(new CsdlLogicalOrComparisonExpression(LogicalOrComparisonExpressionType.And));
    items.add(new CsdlConstantExpression(ConstantExpressionType.Bool));
    csdlCollection.setItems(items);
    EdmExpression exp = AbstractEdmExpression.getExpression(mock(Edm.class), csdlCollection);
    EdmCollection asCollection = exp.asDynamic().asCollection();

    assertNotNull(asCollection.getItems());
    assertEquals(3, asCollection.getItems().size());

    assertTrue(asCollection.getItems().get(0).isConstant());
    assertTrue(asCollection.getItems().get(1).isDynamic());
    assertTrue(asCollection.getItems().get(2).isConstant());
  }

}
