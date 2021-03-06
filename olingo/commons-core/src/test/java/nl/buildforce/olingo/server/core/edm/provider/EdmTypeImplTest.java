/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
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
