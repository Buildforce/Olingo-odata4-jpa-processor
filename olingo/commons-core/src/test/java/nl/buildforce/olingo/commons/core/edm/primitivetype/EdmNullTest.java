/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import static org.junit.Assert.assertNull;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.junit.Test;

public class EdmNullTest extends PrimitiveTypeBaseTest {

  @Test
  public void checkNull() throws Exception {
    for (EdmPrimitiveTypeKind kind : EdmPrimitiveTypeKind.values()) {
      EdmPrimitiveType instance = EdmPrimitiveTypeFactory.getInstance(kind);
      assertNull(instance.valueToString(null, null, null, null, null, null));
      assertNull(instance.valueToString(null, true, null, null, null, null));

      expectNullErrorInValueToString(instance);
    }
  }

  @Test
  public void checkValueOfNull() throws Exception {
    for (EdmPrimitiveTypeKind kind : EdmPrimitiveTypeKind.values()) {
      EdmPrimitiveType instance = EdmPrimitiveTypeFactory.getInstance(kind);
      assertNull(instance.valueOfString(null, null, null, null, null, null, instance.getDefaultType()));
      assertNull(instance.valueOfString(null, true, null, null, null, null, instance.getDefaultType()));

      expectNullErrorInValueOfString(instance);
    }
  }
}
