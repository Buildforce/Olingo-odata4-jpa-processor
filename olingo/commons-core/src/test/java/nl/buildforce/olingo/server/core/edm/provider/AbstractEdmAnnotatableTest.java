/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainer;
import nl.buildforce.olingo.commons.core.edm.AbstractEdmAnnotatable;
import org.junit.Before;
import org.junit.Test;

public class AbstractEdmAnnotatableTest {

  Edm edm;
  private EdmTerm term;
  
  @Before
  public void setupEdm(){
    edm = mock(Edm.class);
    term = mock(EdmTerm.class);
    FullQualifiedName fullQualifiedName = new FullQualifiedName("namespace", "name");
    when(term.getFullQualifiedName()).thenReturn(fullQualifiedName);
    when(edm.getTerm(fullQualifiedName)).thenReturn(term);
  }
  
  @Test
  public void noAnnotations() {
    EdmAnnotatable anno = new EdmAnnotatableTester(null, new CsdlEntityContainer());

    assertNotNull(anno.getAnnotations());
    assertEquals(0, anno.getAnnotations().size());

    assertNull(anno.getAnnotation(null, null));
    

    assertNull(anno.getAnnotation(term, null));
    assertNull(anno.getAnnotation(term, "qualifier"));
    assertNull(anno.getAnnotation(null, "qualifier"));
  }

  @Test
  public void singleAnnotation() {
    CsdlEntityContainer annotatable = new CsdlEntityContainer();
    CsdlAnnotation annotation = new CsdlAnnotation();
    annotation.setTerm("namespace.name");
    List<CsdlAnnotation> annotations = new ArrayList<CsdlAnnotation>();
    annotations.add(annotation);
    annotatable.setAnnotations(annotations);
    EdmAnnotatable anno = new EdmAnnotatableTester(edm, annotatable);

    assertNotNull(anno.getAnnotations());
    assertEquals(1, anno.getAnnotations().size());

    assertNotNull(anno.getAnnotation(term, null));
    assertNull(anno.getAnnotation(term, "qualifier"));
  }
  
  private static class EdmAnnotatableTester extends AbstractEdmAnnotatable {
    public EdmAnnotatableTester(Edm edm, CsdlAnnotatable annotatable) {
      super(edm, annotatable);
    }
  }

}
