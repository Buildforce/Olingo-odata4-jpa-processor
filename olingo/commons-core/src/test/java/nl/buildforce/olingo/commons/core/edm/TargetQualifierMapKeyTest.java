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

public class TargetQualifierMapKeyTest {

  private static final FullQualifiedName TARGET_NAME_1 = new FullQualifiedName("namespace", "name");

  @Test
  public void invalidParametersTest() {
    createAndCheckForEdmException(null, null);
    createAndCheckForEdmException(null, "qualifier");
  }

  @Test
  public void validParametersTest() {
    new TargetQualifierMapKey(TARGET_NAME_1, null);
    new TargetQualifierMapKey(TARGET_NAME_1, "qualifier");
  }

  @Test
  public void testEqualsMethod() {
    TargetQualifierMapKey key1 = new TargetQualifierMapKey(TARGET_NAME_1, "qualifier");
    TargetQualifierMapKey key2 = new TargetQualifierMapKey(new FullQualifiedName("namespace", "name"), "qualifier");
    assertEquals(key1, key1);

    key1 = new TargetQualifierMapKey(TARGET_NAME_1, "qualifier");
    key2 = new TargetQualifierMapKey(new FullQualifiedName("namespace", "name"), "qualifier");
    assertEquals(key1, key2);

    key1 = new TargetQualifierMapKey(TARGET_NAME_1, null);
    key2 = new TargetQualifierMapKey(TARGET_NAME_1, null);
    assertEquals(key1, key2);

    key1 = new TargetQualifierMapKey(TARGET_NAME_1, null);
    key2 = new TargetQualifierMapKey(new FullQualifiedName("namespace", "name"), null);
    assertEquals(key1, key2);

    key1 = new TargetQualifierMapKey(TARGET_NAME_1, "qualifier");
    key2 = new TargetQualifierMapKey(TARGET_NAME_1, null);
    assertNotSame(key1, key2);

    key1 = new TargetQualifierMapKey(new FullQualifiedName("namespace", "name"), null);
    key2 = new TargetQualifierMapKey(new FullQualifiedName("namespace", "wrong"), null);
    assertNotSame(key1, key2);
  }

  @Test
  public void testHashMethod() {
    TargetQualifierMapKey key1 = new TargetQualifierMapKey(TARGET_NAME_1, "qualifier");
    TargetQualifierMapKey key2 = new TargetQualifierMapKey(new FullQualifiedName("namespace", "name"), "qualifier");
    assertEquals(key1.hashCode(), key1.hashCode());

    key1 = new TargetQualifierMapKey(TARGET_NAME_1, "qualifier");
    key2 = new TargetQualifierMapKey(new FullQualifiedName("namespace", "name"), "qualifier");
    assertEquals(key1.hashCode(), key2.hashCode());

    key1 = new TargetQualifierMapKey(TARGET_NAME_1, null);
    key2 = new TargetQualifierMapKey(TARGET_NAME_1, null);
    assertEquals(key1.hashCode(), key2.hashCode());

    key1 = new TargetQualifierMapKey(TARGET_NAME_1, null);
    key2 = new TargetQualifierMapKey(new FullQualifiedName("namespace", "name"), null);
    assertEquals(key1.hashCode(), key2.hashCode());

    key1 = new TargetQualifierMapKey(TARGET_NAME_1, "qualifier");
    key2 = new TargetQualifierMapKey(TARGET_NAME_1, null);
    assertNotSame(key1.hashCode(), key2.hashCode());

    key1 = new TargetQualifierMapKey(new FullQualifiedName("namespace", "name"), null);
    key2 = new TargetQualifierMapKey(new FullQualifiedName("namespace", "wrong"), null);
    assertNotSame(key1.hashCode(), key2.hashCode());
  }

  private void createAndCheckForEdmException(FullQualifiedName fqn, String qualifier) {
    try {
      new TargetQualifierMapKey(fqn, qualifier);
    } catch (EdmException e) {
      return;
    }
    fail("EdmException expected for parameters: " + fqn + " " + qualifier);
  }

}
