/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.edm.EdmActionImport;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmFunctionImport;
import nl.buildforce.olingo.commons.api.edm.EdmSingleton;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlActionImport;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainer;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntitySet;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlFunctionImport;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlSingleton;
import nl.buildforce.olingo.commons.core.edm.EdmEntityContainerImpl;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class EdmEntityContainerImplTest {

  EdmEntityContainer container;

  @Before
  public void setup() {
    CsdlEdmProvider provider = new CustomProvider();
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    CsdlEntityContainerInfo entityContainerInfo =
        new CsdlEntityContainerInfo().setContainerName(new FullQualifiedName("space", "name"));
    container = new EdmEntityContainerImpl(edm, provider, entityContainerInfo);
  }

  @Test
  public void getAllEntitySetInitial() {
    List<EdmEntitySet> entitySets = container.getEntitySets();
    assertNotNull(entitySets);
    assertEquals(2, entitySets.size());
  }

  @Test
  public void getAllEntitySetsAfterOneWasAlreadyLoaded() {
    container.getEntitySet("entitySetName");
    List<EdmEntitySet> entitySets = container.getEntitySets();
    assertNotNull(entitySets);
    assertEquals(2, entitySets.size());
  }

  @Test
  public void getAllSingletonsInitial() {
    List<EdmSingleton> singletons = container.getSingletons();
    assertNotNull(singletons);
    assertEquals(2, singletons.size());
  }

  @Test
  public void getAllSingletonsAfterOneWasAlreadyLoaded() {
    container.getSingleton("singletonName");
    List<EdmSingleton> singletons = container.getSingletons();
    assertNotNull(singletons);
    assertEquals(2, singletons.size());
  }

  @Test
  public void getAllActionImportsInitial() {
    List<EdmActionImport> actionImports = container.getActionImports();
    assertNotNull(actionImports);
    assertEquals(2, actionImports.size());
  }

  @Test
  public void getAllActionImportsAfterOneWasAlreadyLoaded() {
    container.getActionImport("actionImportName");
    List<EdmActionImport> actionImports = container.getActionImports();
    assertNotNull(actionImports);
    assertEquals(2, actionImports.size());
  }

  @Test
  public void getAllFunctionImportsInitial() {
    List<EdmFunctionImport> functionImports = container.getFunctionImports();
    assertNotNull(functionImports);
    assertEquals(2, functionImports.size());
  }

  @Test
  public void getAllFunctionImportsAfterOneWasAlreadyLoaded() {
    container.getFunctionImport("functionImportName");
    List<EdmFunctionImport> functionImports = container.getFunctionImports();
    assertNotNull(functionImports);
    assertEquals(2, functionImports.size());
  }

  @Ignore
  @Test
  public void checkEdmExceptionConversion() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    FullQualifiedName containerName = new FullQualifiedName("space", "name");
    when(provider.getEntitySet(containerName, null)).thenThrow(new ODataException("msg"));
//    when(provider.getSingleton(containerName, null)).thenThrow(new ODataException("msg"));
    when(provider.getFunctionImport(containerName, null)).thenThrow(new ODataException("msg"));
    when(provider.getActionImport(containerName, null)).thenThrow(new ODataException("msg"));
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    CsdlEntityContainerInfo entityContainerInfo =
        new CsdlEntityContainerInfo().setContainerName(containerName);
    EdmEntityContainer container = new EdmEntityContainerImpl(edm, provider, entityContainerInfo);
    try {
      container.getEntitySet(null);
      fail("Expected EdmException not thrown");
    } catch (EdmException e) {
    }
    try {
      container.getSingleton(null);
      fail("Expected EdmException not thrown");
    } catch (EdmException e) {
    }
    try {
      container.getActionImport(null);
      fail("Expected EdmException not thrown");
    } catch (EdmException e) {
    }
    try {
      container.getFunctionImport(null);
      fail("Expected EdmException not thrown");
    } catch (EdmException e) {
    }
  }

  @Test
  public void simpleContainerGetter() {
    assertEquals("name", container.getName());
    assertEquals("space", container.getNamespace());
    assertEquals(new FullQualifiedName("space.name"), container.getFullQualifiedName());
  }

  @Test
  public void getExistingFunctionImport() {
    EdmFunctionImport functionImport = container.getFunctionImport("functionImportName");
    assertNotNull(functionImport);
    assertEquals("functionImportName", functionImport.getName());
    // Caching
    assertSame(functionImport, container.getFunctionImport("functionImportName"));
  }

  @Test
  public void getNonExistingFunctionImport() {
    assertNull(container.getFunctionImport(null));
  }

  @Test
  public void getExistingActionImport() {
    EdmActionImport actionImport = container.getActionImport("actionImportName");
    assertNotNull(actionImport);
    assertEquals("actionImportName", actionImport.getName());
    // Caching
    assertSame(actionImport, container.getActionImport("actionImportName"));
  }

  @Test
  public void getNonExistingActionImport() {
    assertNull(container.getActionImport(null));
  }

  @Test
  public void getExistingSingleton() {
    EdmSingleton singleton = container.getSingleton("singletonName");
    assertNotNull(singleton);
    assertEquals("singletonName", singleton.getName());
    // Caching
    assertSame(singleton, container.getSingleton("singletonName"));
  }

  @Test
  public void getNonExistingSingleton() {
    assertNull(container.getSingleton(null));
  }

  @Test
  public void getExistingEntitySet() {
    EdmEntitySet entitySet = container.getEntitySet("entitySetName");
    assertNotNull(entitySet);
    assertEquals("entitySetName", entitySet.getName());
    // Caching
    assertSame(entitySet, container.getEntitySet("entitySetName"));
  }

  @Test
  public void getNonExistingEntitySet() {
    assertNull(container.getEntitySet(null));
  }

  private static class CustomProvider extends CsdlAbstractEdmProvider {
    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {
      if (entitySetName != null) {
        return new CsdlEntitySet().setName("entitySetName");
      }
      return null;
    }

    @Override
    public CsdlSingleton getSingleton(FullQualifiedName entityContainer, String singletonName) {
      if (singletonName != null) {
        return new CsdlSingleton().setName("singletonName");
      }
      return null;
    }

    @Override
    public CsdlActionImport getActionImport(FullQualifiedName entityContainer, String actionImportName) {
      if (actionImportName != null) {
        return new CsdlActionImport().setName("actionImportName");
      }
      return null;
    }

    @Override
    public CsdlFunctionImport getFunctionImport(FullQualifiedName entityContainer,
                                                String functionImportName) {
      if (functionImportName != null) {
        return new CsdlFunctionImport().setName("functionImportName");
      }
      return null;
    }

    @Override
    public CsdlEntityContainer getEntityContainer() {
      CsdlEntityContainer container = new CsdlEntityContainer();
      List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
      entitySets.add(new CsdlEntitySet().setName("entitySetName"));
      entitySets.add(new CsdlEntitySet().setName("entitySetName2"));
      container.setEntitySets(entitySets);

      List<CsdlSingleton> singletons = new ArrayList<CsdlSingleton>();
      singletons.add(new CsdlSingleton().setName("singletonName"));
      singletons.add(new CsdlSingleton().setName("singletonName2"));
      container.setSingletons(singletons);

      List<CsdlActionImport> actionImports = new ArrayList<CsdlActionImport>();
      actionImports.add(new CsdlActionImport().setName("actionImportName"));
      actionImports.add(new CsdlActionImport().setName("actionImportName2"));
      container.setActionImports(actionImports);

      List<CsdlFunctionImport> functionImports = new ArrayList<CsdlFunctionImport>();
      functionImports.add(new CsdlFunctionImport().setName("functionImportName"));
      functionImports.add(new CsdlFunctionImport().setName("functionImportName2"));
      container.setFunctionImports(functionImports);

      return container;
    }
  }

}