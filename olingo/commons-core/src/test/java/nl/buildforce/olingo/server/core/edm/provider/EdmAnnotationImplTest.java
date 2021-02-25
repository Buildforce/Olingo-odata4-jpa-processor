/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import nl.buildforce.olingo.commons.core.edm.EdmAnnotationImpl;
import org.junit.Test;

public class EdmAnnotationImplTest {

  @Test
  public void initialAnnotation() {
    EdmAnnotation anno = new EdmAnnotationImpl(mock(Edm.class), new CsdlAnnotation());

    assertNull(anno.getQualifier());
    assertNotNull(anno.getAnnotations());
    assertTrue(anno.getAnnotations().isEmpty());
    assertNull(anno.getExpression());
    try {
      anno.getTerm();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("Term must not be null for an annotation.", e.getMessage());
    }
  }

  @Test
  public void simpleAnnotationNoExpression() {
    Edm mock = mock(Edm.class);
    EdmTerm termMock = mock(EdmTerm.class);
    when(mock.getTerm(new FullQualifiedName("ns", "termName"))).thenReturn(termMock);
    EdmAnnotation anno =
        new EdmAnnotationImpl(mock, new CsdlAnnotation().setQualifier("Qualifier").setTerm("ns.termName"));

    assertEquals("Qualifier", anno.getQualifier());
    assertNotNull(anno.getAnnotations());
    assertTrue(anno.getAnnotations().isEmpty());
    assertNotNull(anno.getTerm());
    assertEquals(termMock, anno.getTerm());
  }

  @Test
  public void simpleAnnotationWitConstantExpression() {
    EdmAnnotation anno =
        new EdmAnnotationImpl(mock(Edm.class), new CsdlAnnotation()
            .setExpression(new CsdlConstantExpression(ConstantExpressionType.String).setValue("value")));

    assertEquals("value", anno.getExpression().asConstant().getValueAsString());
  }

}
