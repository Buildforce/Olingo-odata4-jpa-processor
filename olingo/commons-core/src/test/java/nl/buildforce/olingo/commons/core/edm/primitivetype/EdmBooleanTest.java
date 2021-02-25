/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import static org.junit.Assert.assertEquals;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.junit.Test;

public class EdmBooleanTest extends PrimitiveTypeBaseTest {

  private final EdmPrimitiveType instance = EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Boolean);

  @Test
  public void toUriLiteral() throws Exception {
    assertEquals("true", instance.toUriLiteral("true"));
    assertEquals("false", instance.toUriLiteral("false"));
  }

  @Test
  public void fromUriLiteral() throws Exception {
    assertEquals("true", instance.fromUriLiteral("true"));
    assertEquals("false", instance.fromUriLiteral("false"));
  }

  @Test
  public void valueToString() throws Exception {
    assertEquals("true", instance.valueToString(true, null, null, null, null, null));
    assertEquals("false", instance.valueToString(Boolean.FALSE, null, null, null, null, null));
    
    expectTypeErrorInValueToString(instance, 0);
  }
  
  @Test
  public void valueOfString() throws Exception {
    assertEquals(true, instance.valueOfString("true", null, null, null, null, null, Boolean.class));
    assertEquals(false, instance.valueOfString("false", null, null, null, null, null, Boolean.class));

    expectContentErrorInValueOfString(instance, "True");
    expectContentErrorInValueOfString(instance, "1");
    expectContentErrorInValueOfString(instance, "0");
    expectContentErrorInValueOfString(instance, "-1");
    expectContentErrorInValueOfString(instance, "FALSE");

    expectTypeErrorInValueOfString(instance, "true");
  }
}
