/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmConstantExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmDynamicExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression.EdmExpressionType;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmLogicalOrComparisonExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLogicalOrComparisonExpression;
//CHECKSTYLE:OFF
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlLogicalOrComparisonExpression.LogicalOrComparisonExpressionType;
//CHECKSTYLE:ON
import nl.buildforce.olingo.commons.core.edm.annotation.AbstractEdmExpression;
import org.junit.Test;

public class EdmLogicalOrComparisonImplTest extends AbstractAnnotationTest {

  @Test
  public void initialLogicalOrOperationsClasses() {
    for (LogicalOrComparisonExpressionType type : LogicalOrComparisonExpressionType.values()) {
      EdmExpression path = AbstractEdmExpression.getExpression(
          mock(Edm.class),
          new CsdlLogicalOrComparisonExpression(type));

      EdmDynamicExpression dynExp = assertDynamic(path);
      assertEquals(type.toString(), dynExp.getExpressionName());
      assertEquals(EdmExpressionType.valueOf(type.toString()), dynExp.getExpressionType());
      assertSingleKindDynamicExpression(dynExp);

      EdmLogicalOrComparisonExpression logicOrComparisonExp = (EdmLogicalOrComparisonExpression) dynExp;
      try {
        logicOrComparisonExp.getLeftExpression();
        fail("EdmException expected");
      } catch (EdmException e) {
        assertEquals("Comparison Or Logical expression MUST have a left and right expression.", e.getMessage());
      }

      try {
        logicOrComparisonExp.getRightExpression();
        fail("EdmException expected");
      } catch (EdmException e) {
        assertEquals("Comparison Or Logical expression MUST have a left and right expression.", e.getMessage());
      }
    }
  }

  @Test
  public void logicalOrOperationsClassesWithExpressions() {
    for (LogicalOrComparisonExpressionType type : LogicalOrComparisonExpressionType.values()) {
      EdmExpression path = AbstractEdmExpression.getExpression(
          mock(Edm.class),
          new CsdlLogicalOrComparisonExpression(type)
              .setLeft(new CsdlConstantExpression(ConstantExpressionType.String))
              .setRight(new CsdlLogicalOrComparisonExpression(type)));

      EdmDynamicExpression dynExp = assertDynamic(path);
      assertEquals(type.toString(), dynExp.getExpressionName());
      assertSingleKindDynamicExpression(dynExp);

      EdmLogicalOrComparisonExpression logicOrComparisonExp = (EdmLogicalOrComparisonExpression) dynExp;
      assertNotNull(logicOrComparisonExp.getLeftExpression());
      assertNotNull(logicOrComparisonExp.getRightExpression());
      if (type == LogicalOrComparisonExpressionType.Not) {
          assertSame(logicOrComparisonExp.getLeftExpression(), logicOrComparisonExp.getRightExpression());
      } else {
        assertTrue(logicOrComparisonExp.getLeftExpression() instanceof EdmConstantExpression);
        assertTrue(logicOrComparisonExp.getRightExpression() instanceof EdmDynamicExpression);
      }
    }
  }

}
