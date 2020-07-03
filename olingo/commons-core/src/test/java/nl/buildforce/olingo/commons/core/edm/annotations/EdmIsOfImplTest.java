/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
import nl.buildforce.olingo.commons.api.edm.annotation.EdmDynamicExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmIsOf;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression.EdmExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlIsOf;
import nl.buildforce.olingo.commons.core.edm.annotation.AbstractEdmExpression;
import org.junit.Test;

public class EdmIsOfImplTest extends AbstractAnnotationTest {

  @Test
  public void initialIsOf() {
    EdmExpression isOf = AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlIsOf());

    EdmDynamicExpression dynExp = assertDynamic(isOf);
    assertTrue(dynExp.isIsOf());
    assertNotNull(dynExp.asIsOf());

    assertEquals("IsOf", dynExp.getExpressionName());
    assertEquals(EdmExpressionType.IsOf, dynExp.getExpressionType());
    assertSingleKindDynamicExpression(dynExp);
    try {
      dynExp.asIsOf().getValue();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("IsOf expressions require an expression value.", e.getMessage());
    }

    EdmIsOf asIsOf = dynExp.asIsOf();
    assertNull(asIsOf.getMaxLength());
    assertNull(asIsOf.getPrecision());
    assertNull(asIsOf.getScale());
    //assertNull(asIsOf.getSrid());
    try {
      asIsOf.getType();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("Must specify a type for an IsOf expression.", e.getMessage());
    }

    assertNotNull(asIsOf.getAnnotations());
    assertTrue(asIsOf.getAnnotations().isEmpty());
  }

  @Test
  public void isOfWithExpression() {
    CsdlIsOf csdlExp = new CsdlIsOf();
    csdlExp.setMaxLength(1);
    csdlExp.setPrecision(2);
    csdlExp.setScale(3);
    csdlExp.setType("Edm.String");
    csdlExp.setValue(new CsdlConstantExpression(ConstantExpressionType.String));
    List<CsdlAnnotation> csdlAnnotations = new ArrayList<CsdlAnnotation>();
    csdlAnnotations.add(new CsdlAnnotation().setTerm("ns.term"));
    csdlExp.setAnnotations(csdlAnnotations);
    EdmExpression isOf = AbstractEdmExpression.getExpression(mock(Edm.class), csdlExp);

    EdmIsOf asIsOf = isOf.asDynamic().asIsOf();

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