/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmAnnotatable;
import nl.buildforce.olingo.commons.api.edm.EdmNamed;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotatable;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotation;
import nl.buildforce.olingo.commons.core.edm.AbstractEdmNamed;
import org.junit.Test;

public class AbstractEdmNamedTest {

  @Test
  public void getNameTest() {
    EdmNamed obj = new EdmNamedImplTester("Name");
    assertEquals("Name", obj.getName());
    EdmAnnotatable an = (EdmAnnotatable) obj;
    assertNotNull(an.getAnnotations().get(0));
  }

  private static class EdmNamedImplTester extends AbstractEdmNamed {

    public EdmNamedImplTester(String name) {
      super(null, name, new AnnoTester());
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
