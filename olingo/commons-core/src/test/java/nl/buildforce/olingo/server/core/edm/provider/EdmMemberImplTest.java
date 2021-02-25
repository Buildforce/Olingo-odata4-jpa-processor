/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import nl.buildforce.olingo.commons.api.edm.provider.CsdlEnumMember;
import nl.buildforce.olingo.commons.core.edm.EdmMemberImpl;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import org.junit.Test;

public class EdmMemberImplTest {

  @Test
  public void enumMember() {
    CsdlEnumMember member = new CsdlEnumMember().setName("name").setValue("value");
    EdmMemberImpl memberImpl = new EdmMemberImpl(mock(EdmProviderImpl.class), member);

    assertEquals("name", memberImpl.getName());
    assertEquals("value", memberImpl.getValue());
  }

}
