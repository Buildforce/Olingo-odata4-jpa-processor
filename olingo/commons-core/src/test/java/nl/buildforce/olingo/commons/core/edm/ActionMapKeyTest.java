/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import org.junit.Test;

public class ActionMapKeyTest {

  private final FullQualifiedName fqn = new FullQualifiedName("namespace", "name");
  private final FullQualifiedName fqnType = new FullQualifiedName("namespace2", "name2");

  @Test
  public void invalidParametersTest() {
    createAndCheckForEdmException(null, null, null);
    createAndCheckForEdmException(fqn, null, null);
    createAndCheckForEdmException(fqn, fqnType, null);
    createAndCheckForEdmException(fqn, null, true);
    createAndCheckForEdmException(null, fqnType, true);
    createAndCheckForEdmException(null, fqnType, null);
    createAndCheckForEdmException(null, null, true);

  }

  private void createAndCheckForEdmException(FullQualifiedName fqn, FullQualifiedName typeName,
                                             Boolean collection) {
    try {
      new ActionMapKey(fqn, typeName, collection);
    } catch (EdmException e) {
      return;
    }
    fail("EdmException expected for parameters: " + fqn + " " + typeName + " " + collection);
  }
  
  @Test
  public void testNotEquals() {
    ActionMapKey key;
    ActionMapKey someKey;

    key = new ActionMapKey(fqn, fqnType, false);
    someKey = new ActionMapKey(fqnType, fqnType, false);
    assertNotSame(key, someKey);
    
    key = new ActionMapKey(fqn, fqnType, false);
    someKey = new ActionMapKey(fqnType, fqnType, true);
    assertNotSame(key, someKey);
    
    key = new ActionMapKey(fqn, fqnType, false);
    assertNotSame(key, null);
  }

  @Test
  public void testEqualsMethod() {
    ActionMapKey key;
    ActionMapKey someKey;

    key = new ActionMapKey(fqn, fqnType, false);
    assertEquals(key, key);
    
    someKey = new ActionMapKey(fqn, fqnType, false);
    assertEquals(key, someKey);

    key = new ActionMapKey(fqn, fqnType, Boolean.FALSE);
    someKey = new ActionMapKey(fqn, fqnType, false);
    assertEquals(key, someKey);

    key = new ActionMapKey(fqn, fqnType, true);
    someKey = new ActionMapKey(fqn, fqnType, false);
    assertNotSame(key, someKey);

    key = new ActionMapKey(fqn, fqnType, true);
    someKey = new ActionMapKey(fqn, fqnType, Boolean.FALSE);
    assertNotSame(key, someKey);
  }

  @Test
  public void testHashMethod() {
    ActionMapKey key;
    ActionMapKey someKey;

    key = new ActionMapKey(fqn, fqnType, false);
    someKey = new ActionMapKey(fqn, fqnType, false);
    assertEquals(key.hashCode(), someKey.hashCode());

    key = new ActionMapKey(fqn, fqnType, Boolean.FALSE);
    someKey = new ActionMapKey(fqn, fqnType, false);
    assertEquals(key.hashCode(), someKey.hashCode());

    key = new ActionMapKey(fqn, fqnType, true);
    someKey = new ActionMapKey(fqn, fqnType, false);
    assertNotSame(key.hashCode(), someKey.hashCode());

    key = new ActionMapKey(fqn, fqnType, true);
    someKey = new ActionMapKey(fqn, fqnType, Boolean.FALSE);
    assertNotSame(key.hashCode(), someKey.hashCode());
  }

}