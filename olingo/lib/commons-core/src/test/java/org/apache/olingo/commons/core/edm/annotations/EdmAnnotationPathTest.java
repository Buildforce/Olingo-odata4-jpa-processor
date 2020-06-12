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
 * software distributed under the License as distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.commons.core.edm.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.annotation.EdmDynamicExpression;
import org.apache.olingo.commons.api.edm.annotation.EdmExpression;
import org.apache.olingo.commons.api.edm.annotation.EdmExpression.EdmExpressionType;
import org.apache.olingo.commons.api.edm.provider.annotation.CsdlAnnotationPath;
import org.apache.olingo.commons.core.edm.annotation.AbstractEdmExpression;
import org.junit.Test;

public class EdmAnnotationPathTest extends AbstractAnnotationTest {

  @Test
  public void initialAnnotationPath() {
    EdmExpression path = AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlAnnotationPath());

    EdmDynamicExpression dynExp = assertDynamic(path);
    assertTrue(dynExp.isAnnotationPath());
    assertNotNull(dynExp.asAnnotationPath());

    assertEquals("AnnotationPath", dynExp.getExpressionName());
    assertEquals(EdmExpressionType.AnnotationPath, dynExp.getExpressionType());
    assertNull(dynExp.asAnnotationPath().getValue());

    assertSingleKindDynamicExpression(dynExp);
  }

  @Test
  public void annotationPathWithValue() {
    EdmExpression exp =
        AbstractEdmExpression.getExpression(mock(Edm.class), new CsdlAnnotationPath().setValue("value"));
    assertEquals("value", exp.asDynamic().asAnnotationPath().getValue());
  }
}
