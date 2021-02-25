/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmElement;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmKeyPropertyRef;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlPropertyRef;
import nl.buildforce.olingo.commons.core.edm.EdmKeyPropertyRefImpl;
import org.junit.Test;

public class EdmKeyPropertyRefImplTest {

  @Test
  public void noAlias() {
    CsdlPropertyRef providerRef = new CsdlPropertyRef().setName("Id");
    EdmEntityType etMock = mock(EdmEntityType.class);
    EdmProperty keyPropertyMock = mock(EdmProperty.class);
    when(etMock.getStructuralProperty("Id")).thenReturn(keyPropertyMock);
    EdmKeyPropertyRef ref = new EdmKeyPropertyRefImpl(etMock, providerRef);
    assertEquals("Id", ref.getName());
    assertNull(ref.getAlias());

    EdmProperty property = ref.getProperty();
    assertNotNull(property);
      assertSame(property, keyPropertyMock);
      assertSame(property, ref.getProperty());
  }

  @Test
  public void aliasForPropertyInComplexPropertyOneLevel() {
    CsdlPropertyRef providerRef = new CsdlPropertyRef().setName("comp/Id").setAlias("alias");
    EdmEntityType etMock = mock(EdmEntityType.class);
    EdmProperty keyPropertyMock = mock(EdmProperty.class);
    EdmProperty compMock = mock(EdmProperty.class);
    EdmComplexType compTypeMock = mock(EdmComplexType.class);
    when(compTypeMock.getStructuralProperty("Id")).thenReturn(keyPropertyMock);
    when(compMock.getType()).thenReturn(compTypeMock);
    when(etMock.getStructuralProperty("comp")).thenReturn(compMock);
    EdmKeyPropertyRef ref = new EdmKeyPropertyRefImpl(etMock, providerRef);
    assertEquals("alias", ref.getAlias());

    EdmProperty property = ref.getProperty();
    assertNotNull(property);
      assertSame(property, keyPropertyMock);
  }

  @Test(expected = EdmException.class)
  public void aliasForPropertyInComplexPropertyButWrongPath() {
    CsdlPropertyRef providerRef = new CsdlPropertyRef().setName("comp/wrong").setAlias("alias");
    EdmEntityType etMock = mock(EdmEntityType.class);
    EdmProperty keyPropertyMock = mock(EdmProperty.class);
    EdmElement compMock = mock(EdmProperty.class);
    EdmComplexType compTypeMock = mock(EdmComplexType.class);
    when(compTypeMock.getProperty("Id")).thenReturn(keyPropertyMock);
    when(compMock.getType()).thenReturn(compTypeMock);
    when(etMock.getProperty("comp")).thenReturn(compMock);
    new EdmKeyPropertyRefImpl(etMock, providerRef).getProperty();
  }

  @Test(expected = EdmException.class)
  public void aliasForPropertyInComplexPropertyButWrongPath2() {
    CsdlPropertyRef providerRef = new CsdlPropertyRef().setName("wrong/Id").setAlias("alias");
    EdmEntityType etMock = mock(EdmEntityType.class);
    EdmProperty keyPropertyMock = mock(EdmProperty.class);
    EdmElement compMock = mock(EdmProperty.class);
    EdmComplexType compTypeMock = mock(EdmComplexType.class);
    when(compTypeMock.getProperty("Id")).thenReturn(keyPropertyMock);
    when(compMock.getType()).thenReturn(compTypeMock);
    when(etMock.getProperty("comp")).thenReturn(compMock);
    new EdmKeyPropertyRefImpl(etMock, providerRef).getProperty();
  }

  @Test
  public void aliasForPropertyInComplexPropertyTwoLevels() {
    CsdlPropertyRef providerRef = new CsdlPropertyRef().setName("comp/comp2/Id").setAlias("alias");
    EdmEntityType etMock = mock(EdmEntityType.class);
    EdmProperty keyPropertyMock = mock(EdmProperty.class);
    EdmProperty compMock = mock(EdmProperty.class);
    EdmComplexType compTypeMock = mock(EdmComplexType.class);
    EdmProperty comp2Mock = mock(EdmProperty.class);
    EdmComplexType comp2TypeMock = mock(EdmComplexType.class);
    when(comp2TypeMock.getStructuralProperty("Id")).thenReturn(keyPropertyMock);
    when(comp2Mock.getType()).thenReturn(comp2TypeMock);
    when(compTypeMock.getStructuralProperty("comp2")).thenReturn(comp2Mock);
    when(compMock.getType()).thenReturn(compTypeMock);
    when(etMock.getStructuralProperty("comp")).thenReturn(compMock);
    EdmKeyPropertyRef ref = new EdmKeyPropertyRefImpl(etMock, providerRef);

    EdmProperty property = ref.getProperty();
    assertNotNull(property);
      assertSame(property, keyPropertyMock);
  }

  @Test(expected = EdmException.class)
  public void oneKeyNoAliasButInvalidProperty() {
    CsdlPropertyRef providerRef = new CsdlPropertyRef().setName("Id");
    EdmKeyPropertyRef ref = new EdmKeyPropertyRefImpl(mock(EdmEntityType.class), providerRef);
    ref.getProperty();
  }

  @Test(expected = EdmException.class)
  public void aliasButNoPath() {
    CsdlPropertyRef providerRef = new CsdlPropertyRef().setName("Id").setAlias("alias");
    EdmKeyPropertyRef ref = new EdmKeyPropertyRefImpl(mock(EdmEntityType.class), providerRef);
    ref.getProperty();
  }

  @Test(expected = EdmException.class)
  public void aliasButEmptyPath() {
    CsdlPropertyRef providerRef = new CsdlPropertyRef().setName("").setAlias("alias");
    EdmKeyPropertyRef ref = new EdmKeyPropertyRefImpl(mock(EdmEntityType.class), providerRef);
    ref.getProperty();
  }
}
