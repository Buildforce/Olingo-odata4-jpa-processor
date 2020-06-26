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
package nl.buildforce.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotation;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;

public abstract class AbstractEdmAnnotatable implements EdmAnnotatable {

  private final CsdlAnnotatable annotatable;
  private List<EdmAnnotation> annotations;
  protected final Edm edm;

  public AbstractEdmAnnotatable(Edm edm, CsdlAnnotatable annotatable) {
    this.edm = edm;
    this.annotatable = annotatable;
  }

  @Override
  public EdmAnnotation getAnnotation(EdmTerm term, String qualifier) {
    EdmAnnotation result = null;
    for (EdmAnnotation annotation : getAnnotations()) {
      if (term.getFullQualifiedName().equals(annotation.getTerm().getFullQualifiedName())
          && qualifierEqual(qualifier, annotation.getQualifier())) {
        result = annotation;
        break;
      }
    }
    return result;
  }

  private boolean qualifierEqual(String qualifier, String annotationQualifier) {
    return (qualifier == null && annotationQualifier == null)
        || (qualifier != null && qualifier.equals(annotationQualifier));
  }

  @Override
  public List<EdmAnnotation> getAnnotations() {
    if (annotations == null) {
      List<EdmAnnotation> annotationsLocal = new ArrayList<EdmAnnotation>();
      if (annotatable != null) {
        for (CsdlAnnotation annotation : annotatable.getAnnotations()) {
          annotationsLocal.add(new EdmAnnotationImpl(edm, annotation));
        }

        annotations = Collections.unmodifiableList(annotationsLocal);
      }
    }
    return annotations;
  }
}