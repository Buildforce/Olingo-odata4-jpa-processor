/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmActionImport;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlActionImport;
import nl.buildforce.olingo.commons.core.edm.EdmActionImportImpl;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import org.junit.Before;
import org.junit.Test;

public class EdmActionImportImplTest {

  EdmEntityContainer container;

  EdmActionImport actionImport;

  private EdmAction action;

  private EdmEntitySet entitySet;

  @Before
  public void setup() {
    FullQualifiedName actionFqn = new FullQualifiedName("namespace", "actionName");
    FullQualifiedName entityContainerFqn = new FullQualifiedName("namespace", "containerName");
    String target = entityContainerFqn.getFullQualifiedNameAsString() + "/entitySetName";
    CsdlActionImport providerActionImport =
        new CsdlActionImport().setName("actionImportName").setAction(actionFqn).setEntitySet(target);

    EdmProviderImpl edm = mock(EdmProviderImpl.class);
    container = mock(EdmEntityContainer.class);
    when(edm.getEntityContainer(entityContainerFqn)).thenReturn(container);
    action = mock(EdmAction.class);
    when(edm.getUnboundAction(actionFqn)).thenReturn(action);

    entitySet = mock(EdmEntitySet.class);
    when(container.getEntitySet("entitySetName")).thenReturn(entitySet);
    actionImport = new EdmActionImportImpl(edm, container, providerActionImport);
  }

  @Test
  public void simpleActionTest() {
    assertEquals("actionImportName", actionImport.getName());
      assertSame(container, actionImport.getEntityContainer());
      assertSame(action, actionImport.getUnboundAction());
  }

  @Test
  public void getReturnedEntitySet() {
    EdmEntitySet returnedEntitySet = actionImport.getReturnedEntitySet();
    assertNotNull(returnedEntitySet);
      assertSame(returnedEntitySet, entitySet);

    // Chaching
      assertSame(returnedEntitySet, actionImport.getReturnedEntitySet());
  }

  @Test(expected = EdmException.class)
  public void getReturnedEntitySetNonExistingContainer() {
    String target = "alias.nonexisting/Es";
    CsdlActionImport providerActionImport = new CsdlActionImport().setName("actionImportName").setEntitySet(target);
    EdmActionImport actionImport =
        new EdmActionImportImpl(mock(EdmProviderImpl.class), container, providerActionImport);
    actionImport.getReturnedEntitySet();
  }

  @Test(expected = EdmException.class)
  public void getReturnedEntitySetNonExistingEntitySet() {
    String target = "nonExisting";
    CsdlActionImport providerActionImport = new CsdlActionImport().setName("actionImportName").setEntitySet(target);
    EdmProviderImpl edm = mock(EdmProviderImpl.class);
    when(edm.getEntityContainer()).thenReturn(container);
    EdmActionImport actionImport = new EdmActionImportImpl(edm, container, providerActionImport);
    actionImport.getReturnedEntitySet();
  }

}
