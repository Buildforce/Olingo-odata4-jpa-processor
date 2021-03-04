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

import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmTypeDefinition;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlTypeDefinition;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import nl.buildforce.olingo.commons.core.edm.EdmTypeDefinitionImpl;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmString;
import nl.buildforce.olingo.commons.core.edm.primitivetype.PrimitiveTypeBaseTest;
import org.junit.Test;

public class EdmTypeDefinitionImplTest extends PrimitiveTypeBaseTest {

  private final EdmPrimitiveType instance = new EdmTypeDefinitionImpl(null,
      new FullQualifiedName("namespace", "def"),
      new CsdlTypeDefinition().setName("def")
          .setUnderlyingType(EdmString.getInstance().getFullQualifiedName())
          .setMaxLength(5)
          .setUnicode(false));

  @Test
  public void defaultType() {
    assertEquals(String.class, instance.getDefaultType());
  }

  @Test
  public void compatibility() {
    assertTrue(instance.isCompatible(instance));
    for (EdmPrimitiveTypeKind kind : EdmPrimitiveTypeKind.values()) {
      if (kind != EdmPrimitiveTypeKind.String) {
        assertFalse(instance.isCompatible(EdmPrimitiveTypeFactory.getInstance(kind)));
      }
    }
  }

  @Test
  public void toUriLiteral() {
    assertEquals("'Value'", instance.toUriLiteral("Value"));
  }

  @Test
  public void fromUriLiteral() throws Exception {
    assertEquals("Value", instance.fromUriLiteral("'Value'"));
  }

  @Test
  public void valueToString() throws Exception {
    assertEquals("text", instance.valueToString("text", null, null, null, null, null));

    expectFacetsErrorInValueToString(instance, "longtext", null, null, null, null, null);
    expectFacetsErrorInValueToString(instance, "text", null, 3, null, null, null);
    expectFacetsErrorInValueToString(instance, "schräg", null, null, null, null, null);
    expectFacetsErrorInValueToString(instance, "schräg", null, null, null, null, false);
  }

  @Test
  public void valueOfString() throws Exception {
    assertEquals("text", instance.valueOfString("text", null, null, null, null, null, String.class));

    expectFacetsErrorInValueOfString(instance, "longtext", null, null, null, null, null);
    expectFacetsErrorInValueOfString(instance, "text", null, 3, null, null, null);
    expectFacetsErrorInValueOfString(instance, "schräg", null, null, null, null, null);
    expectFacetsErrorInValueOfString(instance, "schräg", null, null, null, null, false);

    expectTypeErrorInValueOfString(instance, "text");
  }
  
  @Test
  public void typeDefOnStringNoFacets() throws Exception {
    FullQualifiedName typeDefName = new FullQualifiedName("namespace", "name");
    CsdlTypeDefinition providerTypeDef =
        new CsdlTypeDefinition().setName("typeDef").setUnderlyingType(new FullQualifiedName("Edm", "String"));
    EdmTypeDefinition typeDefImpl =
        new EdmTypeDefinitionImpl(mock(EdmProviderImpl.class), typeDefName, providerTypeDef);

    assertEquals("name", typeDefImpl.getName());
    assertEquals("namespace", typeDefImpl.getNamespace());
    assertEquals(new FullQualifiedName("namespace.name"), typeDefImpl.getFullQualifiedName());
    assertEquals(String.class, typeDefImpl.getDefaultType());
    assertEquals(EdmTypeKind.DEFINITION, typeDefImpl.getKind());
    assertEquals(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.String), typeDefImpl.getUnderlyingType());
    assertTrue(typeDefImpl.isCompatible(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.String)));

    // String validation
    assertEquals("'StringValue'", typeDefImpl.toUriLiteral("StringValue"));
    assertEquals("String''Value", typeDefImpl.fromUriLiteral("'String''''Value'"));
    assertTrue(typeDefImpl.validate("text", null, null, null, null, null));
    assertEquals("text", typeDefImpl.valueToString("text", null, null, null, null, null));
    assertEquals("text", typeDefImpl.valueOfString("text", null, null, null, null, null, String.class));

    // Facets must be initial
    assertNull(typeDefImpl.getMaxLength());
    assertNull(typeDefImpl.getPrecision());
    assertNull(typeDefImpl.getScale());
    assertTrue(typeDefImpl.isUnicode());
    // assertNull(typeDefImpl.getSrid());
  }

  @Test
  public void invalidTypeResultsInEdmException() {
    FullQualifiedName typeDefName = new FullQualifiedName("namespace", "name");
    CsdlTypeDefinition providerTypeDef =
        new CsdlTypeDefinition().setName("typeDef").setUnderlyingType(new FullQualifiedName("wrong", "wrong"));
    EdmTypeDefinitionImpl def = new EdmTypeDefinitionImpl(mock(EdmProviderImpl.class), typeDefName, providerTypeDef);
    try {
      def.getUnderlyingType();
      fail("Expected an EdmException");
    } catch (EdmException e) {
      assertEquals("Invalid underlying type: wrong.wrong", e.getMessage());
    }
  }

  @Test
  public void nullTypeResultsInEdmException() {
    FullQualifiedName typeDefName = new FullQualifiedName("namespace", "name");
    CsdlTypeDefinition providerTypeDef = new CsdlTypeDefinition().setName("typeDef");
    EdmTypeDefinitionImpl def = new EdmTypeDefinitionImpl(mock(EdmProviderImpl.class), typeDefName, providerTypeDef);
    try {
      def.getUnderlyingType();
      fail("Expected an EdmException");
    } catch (EdmException e) {
      assertEquals("Underlying Type for type definition: namespace.name must not be null.", e.getMessage());
    }
  }

}