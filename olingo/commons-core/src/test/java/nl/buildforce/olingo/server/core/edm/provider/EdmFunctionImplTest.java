/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmReturnType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlFunction;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlReturnType;
import nl.buildforce.olingo.commons.core.edm.EdmFunctionImpl;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import org.junit.Before;
import org.junit.Test;

public class EdmFunctionImplTest {

  private EdmFunction functionImpl1;
  private EdmFunction functionImpl2;

  @Before
  public void setupFunctions() {
    EdmProviderImpl provider = mock(EdmProviderImpl.class);

    CsdlFunction function1 = new CsdlFunction().setReturnType(
        new CsdlReturnType().setType(new FullQualifiedName("Edm", "String")));
    functionImpl1 = new EdmFunctionImpl(provider, new FullQualifiedName("namespace", "name"), function1);
    CsdlFunction function2 = new CsdlFunction().setComposable(true);
    functionImpl2 = new EdmFunctionImpl(provider, new FullQualifiedName("namespace", "name"), function2);
  }

  @Test
  public void isComposableDefaultFalse() {
    assertFalse(functionImpl1.isComposable());
  }

  @Test
  public void isComposableSetToTrue() {
    assertTrue(functionImpl2.isComposable());
  }

  @Test
  public void existingReturnTypeGetsReturned() {
    EdmReturnType returnType = functionImpl1.getReturnType();
    assertNotNull(returnType);
    assertEquals("String", returnType.getType().getName());
  }

  @Test(expected = EdmException.class)
  public void nonExistingReturnTypeResultsInException() {
    functionImpl2.getReturnType();
    fail();
  }

}
