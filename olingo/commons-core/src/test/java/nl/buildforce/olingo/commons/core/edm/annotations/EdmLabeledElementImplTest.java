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
import nl.buildforce.olingo.commons.api.edm.annotation.EdmDynamicExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmLabeledElement;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression.EdmExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLabeledElement;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import nl.buildforce.olingo.commons.core.edm.annotation.AbstractEdmExpression;
import org.junit.Test;

public class EdmLabeledElementImplTest extends AbstractAnnotationTest {

  @Test
  public void initialLabeledElement() {
    EdmExpression exp = AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlLabeledElement());

    EdmDynamicExpression dynExp = assertDynamic(exp);
    assertTrue(dynExp.isLabeledElement());
    assertNotNull(dynExp.asLabeledElement());

    assertEquals("LabeledElement", dynExp.getExpressionName());
    assertEquals(EdmExpressionType.LabeledElement, dynExp.getExpressionType());
    assertSingleKindDynamicExpression(dynExp);

    EdmLabeledElement asLabeled = dynExp.asLabeledElement();

    try {
      asLabeled.getName();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("The LabeledElement expression must have a name attribute.", e.getMessage());
    }

    try {
      asLabeled.getValue();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("The LabeledElement expression must have a child expression", e.getMessage());
    }

    assertNotNull(asLabeled.getAnnotations());
    assertTrue(asLabeled.getAnnotations().isEmpty());
  }

  @Test
  public void labeledElementWithNameAndValue() {
    CsdlLabeledElement csdlLabeledElement = new CsdlLabeledElement();
    csdlLabeledElement.setName("name");
    csdlLabeledElement.setValue(new CsdlConstantExpression(ConstantExpressionType.String));
    List<CsdlAnnotation> csdlAnnotations = new ArrayList<CsdlAnnotation>();
    csdlAnnotations.add(new CsdlAnnotation().setTerm("ns.term"));
    csdlLabeledElement.setAnnotations(csdlAnnotations);
    EdmExpression exp = AbstractEdmExpression.getExpression(mock(Edm.class), csdlLabeledElement);
    EdmLabeledElement asLabeled = exp.asDynamic().asLabeledElement();

    assertEquals("name", asLabeled.getName());
    assertNotNull(asLabeled.getValue());
    assertTrue(asLabeled.getValue().isConstant());

    assertNotNull(asLabeled.getAnnotations());
    assertEquals(1, asLabeled.getAnnotations().size());
  }
}
