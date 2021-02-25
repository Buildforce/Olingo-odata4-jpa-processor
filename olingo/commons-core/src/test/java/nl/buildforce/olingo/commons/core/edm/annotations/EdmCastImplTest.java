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
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmCast;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmDynamicExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression.EdmExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlCast;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import nl.buildforce.olingo.commons.core.edm.annotation.AbstractEdmExpression;
import org.junit.Test;

public class EdmCastImplTest extends AbstractAnnotationTest{
  
  @Test
  public void initialCast() {
    EdmExpression cast = AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlCast());

    EdmDynamicExpression dynExp = assertDynamic(cast);
    assertTrue(dynExp.isCast());
    assertNotNull(dynExp.asCast());

    assertEquals("Cast", dynExp.getExpressionName());
    assertEquals(EdmExpressionType.Cast, dynExp.getExpressionType());
    assertSingleKindDynamicExpression(dynExp);
    try {
      dynExp.asCast().getValue();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("Cast expressions require an expression value.", e.getMessage());
    }

    EdmCast asCast = dynExp.asCast();
    assertNull(asCast.getMaxLength());
    assertNull(asCast.getPrecision());
    assertNull(asCast.getScale());
    // assertNull(asCast.getSrid());
    try {
      asCast.getType();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("Must specify a type for a Cast expression.", e.getMessage());
    }

    assertNotNull(asCast.getAnnotations());
    assertTrue(asCast.getAnnotations().isEmpty());
  }

  @Test
  public void castWithExpression() {
    CsdlCast csdlExp = new CsdlCast();
    csdlExp.setMaxLength(1);
    csdlExp.setPrecision(2);
    csdlExp.setScale(3);
    csdlExp.setType("Edm.String");
    csdlExp.setValue(new CsdlConstantExpression(ConstantExpressionType.String));
    List<CsdlAnnotation> csdlAnnotations = new ArrayList<CsdlAnnotation>();
    csdlAnnotations.add(new CsdlAnnotation().setTerm("ns.term"));
    csdlExp.setAnnotations(csdlAnnotations);
    EdmExpression isOf = AbstractEdmExpression.getExpression(mock(Edm.class), csdlExp);

    EdmCast asIsOf = isOf.asDynamic().asCast();

    assertEquals(Integer.valueOf(1), asIsOf.getMaxLength());
    assertEquals(Integer.valueOf(2), asIsOf.getPrecision());
    assertEquals(Integer.valueOf(3), asIsOf.getScale());

    assertNotNull(asIsOf.getType());
    assertTrue(asIsOf.getType() instanceof EdmPrimitiveType);

    assertNotNull(asIsOf.getValue());
    assertTrue(asIsOf.getValue().isConstant());

    assertNotNull(asIsOf.getAnnotations());
    assertEquals(1, asIsOf.getAnnotations().size());
  }
}