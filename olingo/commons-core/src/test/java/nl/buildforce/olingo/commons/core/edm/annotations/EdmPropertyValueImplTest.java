/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmPropertyValue;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlPropertyValue;
import nl.buildforce.olingo.commons.core.edm.annotation.EdmPropertyValueImpl;
import org.junit.Test;

public class EdmPropertyValueImplTest extends AbstractAnnotationTest {
  @Test
  public void initialPropertyValue() {
    EdmPropertyValue asPropValue = new EdmPropertyValueImpl(mock(Edm.class), new CsdlPropertyValue());
    try {
      asPropValue.getProperty();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("PropertyValue expressions require a referenced property value.", e.getMessage());
    }

    try {
      asPropValue.getValue();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("PropertyValue expressions require an expression value.", e.getMessage());
    }

    assertNotNull(asPropValue.getAnnotations());
    assertTrue(asPropValue.getAnnotations().isEmpty());
  }

  @Test
  public void propertyValue() {
    CsdlPropertyValue csdlPropertyValue = new CsdlPropertyValue();
    csdlPropertyValue.setProperty("property");
    csdlPropertyValue.setValue(new CsdlConstantExpression(ConstantExpressionType.String));
    List<CsdlAnnotation> csdlAnnotations = new ArrayList<CsdlAnnotation>();
    csdlAnnotations.add(new CsdlAnnotation().setTerm("ns.term"));
    csdlPropertyValue.setAnnotations(csdlAnnotations);
    EdmPropertyValue asPropValue = new EdmPropertyValueImpl(mock(Edm.class), csdlPropertyValue);

    assertNotNull(asPropValue.getProperty());
    assertEquals("property", asPropValue.getProperty());
    assertNotNull(asPropValue.getValue());
    assertTrue(asPropValue.getValue().isConstant());

    assertNotNull(asPropValue.getAnnotations());
    assertEquals(1, asPropValue.getAnnotations().size());
  }
}
