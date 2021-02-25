/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.edm.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmParameter;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlComplexType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEnumType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlParameter;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlTypeDefinition;
import nl.buildforce.olingo.commons.core.edm.EdmParameterImpl;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import org.junit.Test;

public class EdmParameterImplTest {

  @Test
  public void initialParameter() {
    EdmParameterImpl parameter = new EdmParameterImpl(mock(Edm.class), new CsdlParameter());

    assertTrue(parameter.isNullable());
    assertFalse(parameter.isCollection());
    assertNull(parameter.getName());
    assertNull(parameter.getMapping());
    assertNull(parameter.getMaxLength());
    assertNull(parameter.getPrecision());
    assertNull(parameter.getScale());
    //assertNull(parameter.getSrid());
    assertNotNull(parameter.getAnnotations());
    assertTrue(parameter.getAnnotations().isEmpty());

    try {
      parameter.getType();
      fail("EdmException expected");
    } catch (EdmException e) {
      assertEquals("Parameter null must hava a full qualified type.", e.getMessage());
    }
  }

  @Test
  public void getTypeReturnsPrimitiveType() {
    EdmProviderImpl edm = new EdmProviderImpl(mock(CsdlEdmProvider.class));
    CsdlParameter parameterProvider = new CsdlParameter();
    parameterProvider.setType(EdmPrimitiveTypeKind.Binary.getFullQualifiedName());
    EdmParameter parameter = new EdmParameterImpl(edm, parameterProvider);
    EdmType type = parameter.getType();
    assertEquals(EdmTypeKind.PRIMITIVE, type.getKind());
    assertEquals(EdmPrimitiveType.EDM_NAMESPACE, type.getNamespace());
    assertEquals(EdmPrimitiveTypeKind.Binary.toString(), type.getName());
  }

  @Test
  public void getTypeReturnsComplexType() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    FullQualifiedName complexTypeName = new FullQualifiedName("ns", "complex");
    CsdlComplexType complexTypeProvider = new CsdlComplexType();
    when(provider.getComplexType(complexTypeName)).thenReturn(complexTypeProvider);
    CsdlParameter parameterProvider = new CsdlParameter();
    parameterProvider.setType(complexTypeName);
    EdmParameter parameter = new EdmParameterImpl(edm, parameterProvider);
    assertFalse(parameter.isCollection());
    EdmType type = parameter.getType();
    assertEquals(EdmTypeKind.COMPLEX, type.getKind());
    assertEquals("ns", type.getNamespace());
    assertEquals("complex", type.getName());
  }

  @Test
  public void getTypeReturnsEnumType() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    FullQualifiedName enumTypeName = new FullQualifiedName("ns", "enum");
    CsdlEnumType enumTypeProvider = new CsdlEnumType();
    when(provider.getEnumType(enumTypeName)).thenReturn(enumTypeProvider);
    CsdlParameter parameterProvider = new CsdlParameter();
    parameterProvider.setType(enumTypeName);
    EdmParameter parameter = new EdmParameterImpl(edm, parameterProvider);
    assertFalse(parameter.isCollection());
    EdmType type = parameter.getType();
    assertEquals(EdmTypeKind.ENUM, type.getKind());
    assertEquals("ns", type.getNamespace());
    assertEquals("enum", type.getName());
  }

  @Test
  public void getTypeReturnsTypeDefinition() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    FullQualifiedName typeName = new FullQualifiedName("ns", "definition");
    CsdlTypeDefinition typeProvider =
        new CsdlTypeDefinition().setUnderlyingType(new FullQualifiedName("Edm", "String"));
    when(provider.getTypeDefinition(typeName)).thenReturn(typeProvider);
    CsdlParameter parameterProvider = new CsdlParameter();
    parameterProvider.setType(typeName);
    EdmParameter parameter = new EdmParameterImpl(edm, parameterProvider);
    EdmType type = parameter.getType();
    assertEquals(EdmTypeKind.DEFINITION, type.getKind());
    assertEquals("ns", type.getNamespace());
    assertEquals("definition", type.getName());
  }

  @Test
  public void facets() {
    EdmProviderImpl edm = new EdmProviderImpl(mock(CsdlEdmProvider.class));
    CsdlParameter parameterProvider = new CsdlParameter();
    parameterProvider.setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
    parameterProvider.setPrecision(42);
    parameterProvider.setScale(12);
    parameterProvider.setMaxLength(128);
    parameterProvider.setNullable(false);
    EdmParameter parameter = new EdmParameterImpl(edm, parameterProvider);
    assertNull(parameter.getMapping());
    assertEquals(Integer.valueOf(42), parameter.getPrecision());
    assertEquals(Integer.valueOf(12), parameter.getScale());
    assertEquals(Integer.valueOf(128), parameter.getMaxLength());
    assertFalse(parameter.isNullable());
    // assertNull(parameter.getSrid());
  }

  @Test(expected = EdmException.class)
  public void getTypeWithInvalidSimpleType() {
    EdmProviderImpl edm = new EdmProviderImpl(mock(CsdlEdmProvider.class));
    CsdlParameter parameterProvider = new CsdlParameter();
    parameterProvider.setType(new FullQualifiedName("Edm", "wrong"));
    EdmParameter parameter = new EdmParameterImpl(edm, parameterProvider);
    parameter.getType();
  }

  @Test(expected = EdmException.class)
  public void getTypeWithNonexistingType() {
    EdmProviderImpl edm = new EdmProviderImpl(mock(CsdlEdmProvider.class));
    CsdlParameter parameterProvider = new CsdlParameter();
    parameterProvider.setType(new FullQualifiedName("wrong", "wrong"));
    EdmParameter parameter = new EdmParameterImpl(edm, parameterProvider);
    parameter.getType();
  }

}