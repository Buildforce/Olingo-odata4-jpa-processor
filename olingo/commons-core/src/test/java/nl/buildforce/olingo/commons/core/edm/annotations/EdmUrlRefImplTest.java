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
import nl.buildforce.olingo.commons.api.edm.annotation.EdmUrlRef;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression.EdmExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlNull;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlUrlRef;
import nl.buildforce.olingo.commons.core.edm.annotation.AbstractEdmExpression;
import org.junit.Test;

public class EdmUrlRefImplTest extends AbstractAnnotationTest {

  @Test
  public void initialUrlRef() {
    EdmExpression exp = AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlUrlRef());

    EdmDynamicExpression dynExp = assertDynamic(exp);
    assertTrue(dynExp.isUrlRef());
    assertNotNull(dynExp.asUrlRef());

    assertEquals("UrlRef", dynExp.getExpressionName());
    assertEquals(EdmExpressionType.UrlRef, dynExp.getExpressionType());
    assertSingleKindDynamicExpression(dynExp);

    EdmUrlRef asUrlRef = dynExp.asUrlRef();
    try {
      asUrlRef.getValue();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("URLRef expressions require an expression value.", e.getMessage());
    }

    assertNotNull(asUrlRef.getAnnotations());
    assertTrue(asUrlRef.getAnnotations().isEmpty());
  }

  @Test
  public void urlRefWithValue() {
    CsdlUrlRef csdlUrlRef = new CsdlUrlRef();
    csdlUrlRef.setValue(new CsdlConstantExpression(ConstantExpressionType.String));
    List<CsdlAnnotation> csdlAnnotations = new ArrayList<CsdlAnnotation>();
    csdlAnnotations.add(new CsdlAnnotation().setTerm("ns.term"));
    csdlUrlRef.setAnnotations(csdlAnnotations);
    EdmExpression exp = AbstractEdmExpression.getExpression(mock(Edm.class), csdlUrlRef);
    EdmUrlRef asUrlRef = exp.asDynamic().asUrlRef();
    assertNotNull(asUrlRef.getValue());
    assertTrue(asUrlRef.getValue().isConstant());

    assertNotNull(asUrlRef.getAnnotations());
    assertEquals(1, asUrlRef.getAnnotations().size());
  }

  @Test
  public void urlRefWithInvalidValue() {
    CsdlUrlRef csdlUrlRef = new CsdlUrlRef();
    csdlUrlRef.setValue(new CsdlConstantExpression(ConstantExpressionType.Bool));
    EdmExpression exp = AbstractEdmExpression.getExpression(mock(Edm.class), csdlUrlRef);
    EdmUrlRef asUrlRef = exp.asDynamic().asUrlRef();
    assertNotNull(asUrlRef.getValue());
    assertTrue(asUrlRef.getValue().isConstant());

    csdlUrlRef = new CsdlUrlRef();
    csdlUrlRef.setValue(new CsdlNull());
    exp = AbstractEdmExpression.getExpression(mock(Edm.class), csdlUrlRef);
    asUrlRef = exp.asDynamic().asUrlRef();
    assertNotNull(asUrlRef.getValue());
    assertTrue(asUrlRef.getValue().isDynamic());
    assertTrue(asUrlRef.getValue().asDynamic().isNull());
    assertNotNull(asUrlRef.getValue().asDynamic().asNull());
  }
}
