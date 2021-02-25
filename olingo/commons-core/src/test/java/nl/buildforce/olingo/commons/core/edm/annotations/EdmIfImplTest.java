/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmDynamicExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmIf;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression.EdmExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlIf;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLogicalOrComparisonExpression;
//CHECKSTYLE:OFF
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLogicalOrComparisonExpression.LogicalOrComparisonExpressionType;
//CHECKSTYLE:ON
import nl.buildforce.olingo.commons.core.edm.annotation.AbstractEdmExpression;
import org.junit.Test;

public class EdmIfImplTest extends AbstractAnnotationTest {

  @Test
  public void initialIf() {
    EdmExpression exp = AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlIf());

    EdmDynamicExpression dynExp = assertDynamic(exp);
    assertTrue(dynExp.isIf());
    assertNotNull(dynExp.asIf());

    assertEquals("If", dynExp.getExpressionName());
    assertEquals(EdmExpressionType.If, dynExp.getExpressionType());
    assertSingleKindDynamicExpression(dynExp);

    EdmIf asIf = dynExp.asIf();

    try {
      asIf.getGuard();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("Guard clause of an if expression must not be null", e.getMessage());
    }

    try {
      asIf.getThen();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("Then clause of an if expression must not be null", e.getMessage());
    }

    assertNull(asIf.getElse());

    assertNotNull(asIf.getAnnotations());
    assertTrue(asIf.getAnnotations().isEmpty());
  }

  @Test
  public void withAllExpressions() {
    CsdlIf csdlIf = new CsdlIf();
    csdlIf.setGuard(new CsdlConstantExpression(ConstantExpressionType.Bool));
    csdlIf.setThen(new CsdlConstantExpression(ConstantExpressionType.String));
    csdlIf.setElse(new CsdlLogicalOrComparisonExpression(LogicalOrComparisonExpressionType.And));
    List<CsdlAnnotation> csdlAnnotations = new ArrayList<CsdlAnnotation>();
    csdlAnnotations.add(new CsdlAnnotation().setTerm("ns.term"));
    csdlIf.setAnnotations(csdlAnnotations);
    EdmExpression exp = AbstractEdmExpression.getExpression(mock(Edm.class), csdlIf);
    EdmIf asIf = exp.asDynamic().asIf();

    assertNotNull(asIf.getGuard());
    assertTrue(asIf.getGuard().isConstant());
    assertNotNull(asIf.getThen());
    assertTrue(asIf.getThen().isConstant());
    assertNotNull(asIf.getElse());
    assertTrue(asIf.getElse().isDynamic());

    assertNotNull(asIf.getAnnotations());
    assertEquals(1, asIf.getAnnotations().size());
  }
}
