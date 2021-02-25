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
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlPath;
import nl.buildforce.olingo.commons.core.edm.annotation.AbstractEdmExpression;
import org.junit.Test;

public class EdmPathImplTest extends AbstractAnnotationTest{
  @Test
  public void initialPropertyPath() {
    EdmExpression path = AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlPath());

    EdmDynamicExpression dynExp = assertDynamic(path);
    assertTrue(dynExp.isPath());
    assertNotNull(dynExp.asPath());

    assertEquals("Path", dynExp.getExpressionName());
    assertEquals(EdmExpressionType.Path, dynExp.getExpressionType());
    assertNull(dynExp.asPath().getValue());

    assertSingleKindDynamicExpression(dynExp);
  }

  @Test
  public void annotationPathWithValue() {
    EdmExpression exp =
        AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlPath().setValue("value"));
    assertEquals("value", exp.asDynamic().asPath().getValue());
  }
}
