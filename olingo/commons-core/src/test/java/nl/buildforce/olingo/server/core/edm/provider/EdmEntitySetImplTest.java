/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import nl.buildforce.olingo.commons.api.edm.EdmBindingTarget;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntitySet;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlPropertyRef;
import nl.buildforce.olingo.commons.core.edm.EdmEntityContainerImpl;
import nl.buildforce.olingo.commons.core.edm.EdmEntitySetImpl;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import org.junit.Test;

public class EdmEntitySetImplTest {

  @Test
  public void entitySet() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);

    FullQualifiedName typeName = new FullQualifiedName("ns", "entityType");
    CsdlEntityType entityTypeProvider = new CsdlEntityType()
        .setName(typeName.getName())
        .setKey(Collections.singletonList(new CsdlPropertyRef().setName("Id")));
    when(provider.getEntityType(typeName)).thenReturn(entityTypeProvider);

    FullQualifiedName containerName = new FullQualifiedName("ns", "container");
    CsdlEntityContainerInfo containerInfo = new CsdlEntityContainerInfo().setContainerName(containerName);
    when(provider.getEntityContainerInfo(containerName)).thenReturn(containerInfo);
    EdmEntityContainer entityContainer = new EdmEntityContainerImpl(edm, provider, containerInfo);

    final String entitySetName = "entitySet";
    CsdlEntitySet entitySetProvider = new CsdlEntitySet()
        .setName(entitySetName)
        .setTitle("title")
        .setType(typeName)
        .setNavigationPropertyBindings(Collections.singletonList(
                new CsdlNavigationPropertyBinding().setPath("path")
                        .setTarget(containerName.getFullQualifiedNameAsString() + "/" + entitySetName)));
    when(provider.getEntitySet(containerName, entitySetName)).thenReturn(entitySetProvider);

    EdmEntitySet entitySet = new EdmEntitySetImpl(edm, entityContainer, entitySetProvider);
    assertEquals(entitySetName, entityContainer.getEntitySet(entitySetName).getName());
    assertEquals(entitySetName, entitySet.getName());
    assertEquals("title", entitySet.getTitle());
    EdmEntityType entityType = entitySet.getEntityType();
    assertEquals(typeName.getNamespace(), entityType.getNamespace());
    assertEquals(typeName.getName(), entityType.getName());
    assertEquals(entityContainer, entitySet.getEntityContainer());
    assertNull(entitySet.getRelatedBindingTarget(null));
    EdmBindingTarget target = entitySet.getRelatedBindingTarget("path");
    assertEquals(entitySetName, target.getName());
    assertTrue(entitySet.isIncludeInServiceDocument());
  }

  @Test
  public void entitySetIncludeInServiceDocumentFalseAndInvalidType() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);

    FullQualifiedName containerName = new FullQualifiedName("ns", "container");
    CsdlEntityContainerInfo containerInfo = new CsdlEntityContainerInfo().setContainerName(containerName);
    when(provider.getEntityContainerInfo(containerName)).thenReturn(containerInfo);
    EdmEntityContainer entityContainer = new EdmEntityContainerImpl(edm, provider, containerInfo);

    final String entitySetName = "entitySet";
    CsdlEntitySet entitySetProvider = new CsdlEntitySet()
        .setName(entitySetName)
        .setType("invalid.invalid")
        .setIncludeInServiceDocument(false);
    when(provider.getEntitySet(containerName, entitySetName)).thenReturn(entitySetProvider);

    EdmEntitySet entitySet = new EdmEntitySetImpl(edm, entityContainer, entitySetProvider);
    assertFalse(entitySet.isIncludeInServiceDocument());

    try {
      entitySet.getEntityType();
      fail("Expected an EdmException");
    } catch (EdmException e) {
      assertEquals("Can´t find entity type: invalid.invalid for entity set or singleton: " + entitySetName, e
          .getMessage());
    }
  }
}
