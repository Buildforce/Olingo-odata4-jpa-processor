/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotations;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotations;
import nl.buildforce.olingo.commons.core.edm.EdmAnnotationsImpl;
import org.junit.Before;
import org.junit.Test;

public class EdmAnnotationsImplTest {

  private Edm edm;

  @Before
  public void setupEdm() {
    edm = mock(Edm.class);
  }

  @Test
  public void initialAnnotationGroup() {
    CsdlAnnotations csdlAnnotationGroup = new CsdlAnnotations();
    EdmAnnotations annotationGroup = new EdmAnnotationsImpl(edm, csdlAnnotationGroup);

    assertNotNull(annotationGroup.getAnnotations());
    assertTrue(annotationGroup.getAnnotations().isEmpty());

    assertNull(annotationGroup.getQualifier());
    assertNull(annotationGroup.getTargetPath());
  }

  @Test
  public void annotationGroupWithQualifierAndPathButNonValidTarget() {
    CsdlAnnotations csdlAnnotationGroup = new CsdlAnnotations();
    csdlAnnotationGroup.setQualifier("qualifier");
    csdlAnnotationGroup.setTarget("invalid.invalid");
    EdmAnnotations annotationGroup = new EdmAnnotationsImpl(edm, csdlAnnotationGroup);

    assertNotNull(annotationGroup.getAnnotations());
    assertTrue(annotationGroup.getAnnotations().isEmpty());

    assertEquals("qualifier", annotationGroup.getQualifier());
    assertEquals("invalid.invalid", annotationGroup.getTargetPath());
  }
}
