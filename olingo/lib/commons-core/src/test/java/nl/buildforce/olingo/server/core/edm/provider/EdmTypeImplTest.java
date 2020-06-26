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
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.core.edm.EdmTypeImpl;
import org.junit.Test;

public class EdmTypeImplTest {

  @Test
  public void getterTest() {
    EdmType type = new EdmTypeImplTester(new FullQualifiedName("namespace", "name"), EdmTypeKind.PRIMITIVE);
    assertEquals("name", type.getName());
    assertEquals("namespace", type.getNamespace());
    assertEquals(EdmTypeKind.PRIMITIVE, type.getKind());
    EdmAnnotatable an = (EdmAnnotatable) type;
    assertNotNull(an.getAnnotations().get(0));
  }

  private static class EdmTypeImplTester extends EdmTypeImpl {
    public EdmTypeImplTester(FullQualifiedName name, EdmTypeKind kind) {
      super(null, name, kind, new AnnoTester());
    }
  }

  private static class AnnoTester implements CsdlAnnotatable {
    @Override
    public List<CsdlAnnotation> getAnnotations() {
      CsdlAnnotation annotation = new CsdlAnnotation();
      annotation.setTerm("NS.SimpleTerm");
      return Collections.singletonList(annotation);
    }
  }
}