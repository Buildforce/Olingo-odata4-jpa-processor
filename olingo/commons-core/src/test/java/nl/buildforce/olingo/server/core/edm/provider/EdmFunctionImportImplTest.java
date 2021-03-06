/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmFunctionImport;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlFunction;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlFunctionImport;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlReturnType;
import nl.buildforce.olingo.commons.core.edm.EdmEntityContainerImpl;
import nl.buildforce.olingo.commons.core.edm.EdmFunctionImportImpl;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;
import org.junit.Test;

public class EdmFunctionImportImplTest {

  @Test
  public void functionImport() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);

    FullQualifiedName functionName = new FullQualifiedName("ns", "function");
    CsdlFunction functionProvider = new CsdlFunction()
        .setName(functionName.getName())
        .setParameters(Collections.emptyList())
        .setBound(false)
        .setComposable(false)
        .setReturnType(new CsdlReturnType().setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName()));
    when(provider.getFunctions(functionName)).thenReturn(Collections.singletonList(functionProvider));

    FullQualifiedName containerName = new FullQualifiedName("ns", "container");
    CsdlEntityContainerInfo containerInfo = new CsdlEntityContainerInfo().setContainerName(containerName);
    when(provider.getEntityContainerInfo(containerName)).thenReturn(containerInfo);
    EdmEntityContainer entityContainer = new EdmEntityContainerImpl(edm, provider, containerInfo);

    final String functionImportName = "functionImport";
    CsdlFunctionImport functionImportProvider = new CsdlFunctionImport()
        .setName(functionImportName)
        .setTitle("title")
        .setFunction(functionName)
        .setIncludeInServiceDocument(true);
    when(provider.getFunctionImport(containerName, functionImportName)).thenReturn(functionImportProvider);

    EdmFunctionImport functionImport = new EdmFunctionImportImpl(edm, entityContainer, functionImportProvider);
    assertEquals(functionImportName, entityContainer.getFunctionImport(functionImportName).getName());
    assertEquals("functionImport", functionImport.getName());
    assertEquals("title", functionImport.getTitle());
    assertEquals(new FullQualifiedName("ns", functionImportName), functionImport.getFullQualifiedName());
    assertTrue(functionImport.isIncludeInServiceDocument());
    EdmFunction function = functionImport.getUnboundFunction(Collections.emptyList());
    assertEquals(functionName.getNamespace(), function.getNamespace());
    assertEquals(functionName.getName(), function.getName());
    assertEquals(functionName, function.getFullQualifiedName());
    assertFalse(function.isBound());
    assertFalse(function.isComposable());
    assertEquals(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Boolean),
        function.getReturnType().getType());
    assertEquals(entityContainer, functionImport.getEntityContainer());
    assertNull(functionImport.getReturnedEntitySet());
    
    List<EdmFunction> functions = functionImport.getUnboundFunctions();
    assertNotNull(functions);
    assertEquals(1, functions.size());
  }
}
