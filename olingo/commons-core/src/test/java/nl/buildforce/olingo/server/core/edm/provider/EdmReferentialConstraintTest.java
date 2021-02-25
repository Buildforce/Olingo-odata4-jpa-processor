/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmReferentialConstraint;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlReferentialConstraint;
import nl.buildforce.olingo.commons.core.edm.EdmReferentialConstraintImpl;
import org.junit.Test;

public class EdmReferentialConstraintTest {

  @Test
  public void initialConstraint() {
    CsdlReferentialConstraint constraint = new CsdlReferentialConstraint();
    EdmReferentialConstraint edmConstraint = new EdmReferentialConstraintImpl(mock(Edm.class), constraint);

    assertNull(edmConstraint.getPropertyName());
    assertNull(edmConstraint.getReferencedPropertyName());
  }

  @Test
  public void basicConstraint() {
    CsdlReferentialConstraint constraint = new CsdlReferentialConstraint();
    constraint.setProperty("PropertyName");
    constraint.setReferencedProperty("referencedProperty");
    EdmReferentialConstraint edmConstraint = new EdmReferentialConstraintImpl(mock(Edm.class), constraint);

    assertEquals("PropertyName", edmConstraint.getPropertyName());
    assertEquals("referencedProperty", edmConstraint.getReferencedPropertyName());
  }

}
