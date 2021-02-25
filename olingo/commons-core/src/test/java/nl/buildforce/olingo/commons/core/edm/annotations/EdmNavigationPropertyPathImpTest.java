/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmDynamicExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression.EdmExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlNavigationPropertyPath;
import nl.buildforce.olingo.commons.core.edm.annotation.AbstractEdmExpression;
import org.junit.Test;

public class EdmNavigationPropertyPathImpTest extends AbstractAnnotationTest {
  @Test
  public void initialPropertyPath() {
    EdmExpression path = AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlNavigationPropertyPath());

    EdmDynamicExpression dynExp = assertDynamic(path);
    assertTrue(dynExp.isNavigationPropertyPath());
    assertNotNull(dynExp.asNavigationPropertyPath());

    assertEquals("NavigationPropertyPath", dynExp.getExpressionName());
    assertEquals(EdmExpressionType.NavigationPropertyPath, dynExp.getExpressionType());
    assertNull(dynExp.asNavigationPropertyPath().getValue());

    assertSingleKindDynamicExpression(dynExp);
  }

  @Test
  public void annotationPathWithValue() {
    EdmExpression exp =
        AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlNavigationPropertyPath().setValue("value"));
    assertEquals("value", exp.asDynamic().asNavigationPropertyPath().getValue());
  }

}
