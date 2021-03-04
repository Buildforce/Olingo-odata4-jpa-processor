/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import nl.buildforce.olingo.commons.api.edm.EdmBindingTarget;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmSingleton;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlPropertyRef;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlSingleton;
import nl.buildforce.olingo.commons.core.edm.EdmEntityContainerImpl;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import nl.buildforce.olingo.commons.core.edm.EdmSingletonImpl;
import org.junit.Test;

public class EdmSingletonImplTest {

  @Test
  public void singleton() throws Exception {
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

    final String singletonName = "singleton";
    CsdlSingleton singletonProvider =
        new CsdlSingleton()
            .setName(singletonName)
            .setTitle("title")
            .setType(typeName)
            .setNavigationPropertyBindings(
                    Collections.singletonList(
                            new CsdlNavigationPropertyBinding().setPath("path").setTarget(
                                    containerName.getFullQualifiedNameAsString() + "/" + singletonName)));
    when(provider.getSingleton(containerName, singletonName)).thenReturn(singletonProvider);

    EdmSingleton singleton = new EdmSingletonImpl(edm, entityContainer, singletonProvider);
    assertEquals(singletonName, entityContainer.getSingleton(singletonName).getName());
    assertEquals(singletonName, singleton.getName());
    assertEquals("title", singleton.getTitle());
    EdmEntityType entityType = singleton.getEntityType();
    assertEquals(typeName.getNamespace(), entityType.getNamespace());
    assertEquals(typeName.getName(), entityType.getName());
    assertEquals(entityContainer, singleton.getEntityContainer());
    assertNull(singleton.getRelatedBindingTarget(null));
    EdmBindingTarget target = singleton.getRelatedBindingTarget("path");
    assertEquals(singletonName, target.getName());
  }

  @Test(expected = EdmException.class)
  public void wrongTarget() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);

    FullQualifiedName containerName = new FullQualifiedName("ns", "container");
    CsdlEntityContainerInfo containerInfo = new CsdlEntityContainerInfo().setContainerName(containerName);
    when(provider.getEntityContainerInfo(containerName)).thenReturn(containerInfo);

    final String singletonName = "singleton";
    CsdlSingleton singletonProvider = new CsdlSingleton()
        .setNavigationPropertyBindings(Collections.singletonList(
                new CsdlNavigationPropertyBinding().setPath("path")
                        .setTarget(containerName.getFullQualifiedNameAsString() + "/wrong")));
    when(provider.getSingleton(containerName, singletonName)).thenReturn(singletonProvider);

    EdmSingleton singleton = new EdmSingletonImpl(edm, null, singletonProvider);
    singleton.getRelatedBindingTarget("path");
  }

  @Test(expected = EdmException.class)
  public void wrongTargetContainer() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);

    FullQualifiedName containerName = new FullQualifiedName("ns", "container");
    final String singletonName = "singleton";
    CsdlSingleton singletonProvider = new CsdlSingleton()
        .setNavigationPropertyBindings(Collections.singletonList(
                new CsdlNavigationPropertyBinding().setPath("path").setTarget("ns.wrongContainer/" + singletonName)));
    when(provider.getSingleton(containerName, singletonName)).thenReturn(singletonProvider);

    EdmSingleton singleton = new EdmSingletonImpl(edm, null, singletonProvider);
    singleton.getRelatedBindingTarget("path");
  }

  @Test(expected = EdmException.class)
  public void nonExsistingEntityType() {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);

    CsdlSingleton singleton = new CsdlSingleton().setName("name");
    EdmSingleton edmSingleton = new EdmSingletonImpl(edm, null, singleton);
    edmSingleton.getEntityType();
  }

}