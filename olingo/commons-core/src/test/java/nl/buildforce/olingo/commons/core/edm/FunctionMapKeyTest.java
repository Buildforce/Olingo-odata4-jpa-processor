/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import org.junit.Test;

public class FunctionMapKeyTest {

  private final FullQualifiedName fqn = new FullQualifiedName("namespace", "name");

  private final FullQualifiedName fqnType = new FullQualifiedName("namespace2", "name2");

  @Test
  public void testEqualsPositive() {
    FunctionMapKey key = new FunctionMapKey(fqn, null, null, null);
    FunctionMapKey someKey = new FunctionMapKey(fqn, null, null, null);
    assertEquals(key, someKey);

    key = new FunctionMapKey(fqn, null, true, null);
    someKey = new FunctionMapKey(fqn, null, true, null);
    assertEquals(key, someKey);

    key = new FunctionMapKey(fqn, fqnType, true, null);
    someKey = new FunctionMapKey(fqn, fqnType, true, null);
    assertEquals(key, someKey);

    key = new FunctionMapKey(fqn, fqnType, false, null);
    someKey = new FunctionMapKey(fqn, fqnType, false, null);
    assertEquals(key, someKey);

    key = new FunctionMapKey(fqn, fqnType, false, new ArrayList<String>());
    someKey = new FunctionMapKey(fqn, fqnType, false, new ArrayList<String>());
    assertEquals(key, someKey);

    List<String> keyList = new ArrayList<String>();
    keyList.add("Employee");
    List<String> someKeyList = new ArrayList<String>();
    someKeyList.add("Employee");
    key = new FunctionMapKey(fqn, fqnType, false, keyList);
    someKey = new FunctionMapKey(fqn, fqnType, false, someKeyList);
    assertEquals(key, someKey);

    key = new FunctionMapKey(fqn, null, null, new ArrayList<String>());
    someKey = new FunctionMapKey(fqn, null, null, new ArrayList<String>());
    assertEquals(key, someKey);

    keyList = new ArrayList<String>();
    keyList.add("Employee");
    someKeyList = new ArrayList<String>();
    someKeyList.add("Employee");
    key = new FunctionMapKey(fqn, null, null, keyList);
    someKey = new FunctionMapKey(fqn, null, null, someKeyList);
    assertEquals(key, someKey);
  }

  @Test
  public void testEqualsNegative() {
    FunctionMapKey key = new FunctionMapKey(fqn, null, null, null);
    FunctionMapKey someKey = new FunctionMapKey(fqn, null, true, null);
    assertNotSame(key, someKey);

    key = new FunctionMapKey(fqn, null, true, null);
    someKey = new FunctionMapKey(fqn, null, false, null);
    assertNotSame(key, someKey);

    key = new FunctionMapKey(fqn, fqnType, true, null);
    someKey = new FunctionMapKey(fqn, null, true, null);
    assertNotSame(key, someKey);

    key = new FunctionMapKey(fqn, null, false, null);
    someKey = new FunctionMapKey(fqn, fqnType, true, null);
    assertNotSame(key, someKey);

    key = new FunctionMapKey(fqn, fqnType, false, null);
    someKey = new FunctionMapKey(fqn, fqnType, false, new ArrayList<String>());
    assertNotSame(key, someKey);

    List<String> keyList = new ArrayList<String>();
    keyList.add("Employee");
    List<String> someKeyList = new ArrayList<String>();
    someKeyList.add("Employee2");
    key = new FunctionMapKey(fqn, fqnType, false, keyList);
    someKey = new FunctionMapKey(fqn, fqnType, false, someKeyList);
    assertNotSame(key, someKey);

    key = new FunctionMapKey(fqn, null, null, new ArrayList<String>());
    someKey = new FunctionMapKey(fqn, null, null, new ArrayList<String>());
    assertNotSame(key, someKey);

    keyList = new ArrayList<String>();
    keyList.add("Employee");
    someKeyList = new ArrayList<String>();
    someKeyList.add("Employee2");
    key = new FunctionMapKey(fqn, null, null, keyList);
    someKey = new FunctionMapKey(fqn, null, null, someKeyList);
    assertNotSame(key, someKey);

    key = new FunctionMapKey(fqn, null, null, new ArrayList<String>());
    someKey = new FunctionMapKey(fqn, null, null, null);
    assertNotSame(key, someKey);

    keyList = new ArrayList<String>();
    keyList.add("Employee");
    someKeyList = new ArrayList<String>();
    key = new FunctionMapKey(fqn, null, null, keyList);
    someKey = new FunctionMapKey(fqn, null, null, someKeyList);
    assertNotSame(key, someKey);

    keyList = new ArrayList<String>();
    keyList.add("Employee");
    someKeyList = new ArrayList<String>();
    someKeyList.add("EmpLoYeE");
    key = new FunctionMapKey(fqn, null, null, keyList);
    someKey = new FunctionMapKey(fqn, null, null, someKeyList);
    assertNotSame(key, someKey);
  }

  @Test
  public void testHashCodePositive() {
    FunctionMapKey key = new FunctionMapKey(fqn, null, null, null);
    FunctionMapKey someKey = new FunctionMapKey(fqn, null, null, null);
    assertEquals(key.hashCode(), someKey.hashCode());

    key = new FunctionMapKey(fqn, null, true, null);
    someKey = new FunctionMapKey(fqn, null, true, null);
    assertEquals(key.hashCode(), someKey.hashCode());

    key = new FunctionMapKey(fqn, fqnType, true, null);
    someKey = new FunctionMapKey(fqn, fqnType, true, null);
    assertEquals(key.hashCode(), someKey.hashCode());

    key = new FunctionMapKey(fqn, fqnType, false, null);
    someKey = new FunctionMapKey(fqn, fqnType, false, null);
    assertEquals(key.hashCode(), someKey.hashCode());

    key = new FunctionMapKey(fqn, fqnType, false, new ArrayList<String>());
    someKey = new FunctionMapKey(fqn, fqnType, false, new ArrayList<String>());
    assertEquals(key.hashCode(), someKey.hashCode());

    List<String> keyList = new ArrayList<String>();
    keyList.add("Employee");
    List<String> someKeyList = new ArrayList<String>();
    someKeyList.add("Employee");
    key = new FunctionMapKey(fqn, fqnType, false, keyList);
    someKey = new FunctionMapKey(fqn, fqnType, false, someKeyList);
    assertEquals(key.hashCode(), someKey.hashCode());

    key = new FunctionMapKey(fqn, null, null, new ArrayList<String>());
    someKey = new FunctionMapKey(fqn, null, null, new ArrayList<String>());
    assertEquals(key.hashCode(), someKey.hashCode());

    keyList = new ArrayList<String>();
    keyList.add("Employee");
    someKeyList = new ArrayList<String>();
    someKeyList.add("Employee");
    key = new FunctionMapKey(fqn, null, null, keyList);
    someKey = new FunctionMapKey(fqn, null, null, someKeyList);
    assertEquals(key.hashCode(), someKey.hashCode());

    keyList = new ArrayList<String>();
    keyList.add("Employee");
    keyList.add("employee");
    someKeyList = new ArrayList<String>();
    someKeyList.add("Employee");
    someKeyList.add("employee");
    key = new FunctionMapKey(fqn, null, null, keyList);
    someKey = new FunctionMapKey(fqn, null, null, someKeyList);
    assertEquals(key.hashCode(), someKey.hashCode());

    keyList = new ArrayList<String>();
    keyList.add("Employee");
    keyList.add("Employee2");
    someKeyList = new ArrayList<String>();
    someKeyList.add("Employee2");
    someKeyList.add("Employee");
    key = new FunctionMapKey(fqn, null, null, keyList);
    someKey = new FunctionMapKey(fqn, null, null, someKeyList);
    assertEquals(key.hashCode(), someKey.hashCode());
  }

  @Test
  public void testHashCodeNegative() {
    FunctionMapKey key = new FunctionMapKey(fqn, null, null, null);
    FunctionMapKey someKey = new FunctionMapKey(fqn, null, true, null);
    assertNotSame(key.hashCode(), someKey.hashCode());

    key = new FunctionMapKey(fqn, null, true, null);
    someKey = new FunctionMapKey(fqn, null, false, null);
    assertNotSame(key.hashCode(), someKey.hashCode());

    key = new FunctionMapKey(fqn, fqnType, true, null);
    someKey = new FunctionMapKey(fqn, null, true, null);
    assertNotSame(key.hashCode(), someKey.hashCode());

    key = new FunctionMapKey(fqn, null, false, null);
    someKey = new FunctionMapKey(fqn, fqnType, true, null);
    assertNotSame(key.hashCode(), someKey.hashCode());

    key = new FunctionMapKey(fqn, fqnType, false, null);
    someKey = new FunctionMapKey(fqn, fqnType, false, new ArrayList<String>());
    assertNotSame(key.hashCode(), someKey.hashCode());

    List<String> keyList = new ArrayList<String>();
    keyList.add("Employee");
    List<String> someKeyList = new ArrayList<String>();
    someKeyList.add("Employee2");
    key = new FunctionMapKey(fqn, fqnType, false, keyList);
    someKey = new FunctionMapKey(fqn, fqnType, false, someKeyList);
    assertNotSame(key.hashCode(), someKey.hashCode());

    key = new FunctionMapKey(fqn, null, null, new ArrayList<String>());
    someKey = new FunctionMapKey(fqn, null, null, new ArrayList<String>());
    assertNotSame(key.hashCode(), someKey.hashCode());

    keyList = new ArrayList<String>();
    keyList.add("Employee");
    someKeyList = new ArrayList<String>();
    someKeyList.add("Employee2");
    key = new FunctionMapKey(fqn, null, null, keyList);
    someKey = new FunctionMapKey(fqn, null, null, someKeyList);
    assertNotSame(key.hashCode(), someKey.hashCode());

    key = new FunctionMapKey(fqn, null, null, new ArrayList<String>());
    someKey = new FunctionMapKey(fqn, null, null, null);
    assertNotSame(key.hashCode(), someKey.hashCode());
  }

}
