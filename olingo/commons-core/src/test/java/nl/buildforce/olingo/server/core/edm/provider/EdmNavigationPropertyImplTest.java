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
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmReferentialConstraint;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlReferentialConstraint;
import nl.buildforce.olingo.commons.core.edm.EdmNavigationPropertyImpl;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import org.junit.Test;

public class EdmNavigationPropertyImplTest {

  @Test
  public void navigationProperty() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    FullQualifiedName entityTypeName = new FullQualifiedName("ns", "entity");
    CsdlEntityType entityTypeProvider = new CsdlEntityType();
    entityTypeProvider.setKey(Collections.emptyList());
    when(provider.getEntityType(entityTypeName)).thenReturn(entityTypeProvider);
    CsdlNavigationProperty propertyProvider = new CsdlNavigationProperty();
    propertyProvider.setType(entityTypeName);
    propertyProvider.setNullable(false);
    EdmNavigationProperty property = new EdmNavigationPropertyImpl(edm, propertyProvider);
    assertFalse(property.isCollection());
    assertFalse(property.isNullable());
    EdmType type = property.getType();
    assertEquals(EdmTypeKind.ENTITY, type.getKind());
    assertEquals("ns", type.getNamespace());
    assertEquals("entity", type.getName());
    assertNull(property.getReferencingPropertyName("referencedPropertyName"));
    assertNull(property.getPartner());
    assertFalse(property.containsTarget());

    // Test caching
    EdmType cachedType = property.getType();
      assertSame(type, cachedType);
  }

  @Test
  public void navigationPropertyWithReferntialConstraint() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    FullQualifiedName entityTypeName = new FullQualifiedName("ns", "entity");
    CsdlEntityType entityTypeProvider = new CsdlEntityType();
    entityTypeProvider.setKey(Collections.emptyList());
    when(provider.getEntityType(entityTypeName)).thenReturn(entityTypeProvider);
    CsdlNavigationProperty propertyProvider = new CsdlNavigationProperty();
    propertyProvider.setType(entityTypeName);
    propertyProvider.setNullable(false);
    propertyProvider.setContainsTarget(true);
    List<CsdlReferentialConstraint> referentialConstraints = new ArrayList<CsdlReferentialConstraint>();
    referentialConstraints.add(new CsdlReferentialConstraint().setProperty("property").setReferencedProperty(
        "referencedProperty"));
    propertyProvider.setReferentialConstraints(referentialConstraints);
  
    EdmNavigationProperty property = new EdmNavigationPropertyImpl(edm, propertyProvider);
    assertEquals("property", property.getReferencingPropertyName("referencedProperty"));
    assertNull(property.getReferencingPropertyName("wrong"));
    assertTrue(property.containsTarget());
    
    assertNotNull(property.getReferentialConstraints());
    List<EdmReferentialConstraint> edmReferentialConstraints = property.getReferentialConstraints();
    assertEquals(1, edmReferentialConstraints.size());
      assertSame(edmReferentialConstraints, property.getReferentialConstraints());
    
  }

  @Test
  public void navigationPropertyWithPartner() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    FullQualifiedName entityTypeName = new FullQualifiedName("ns", "entity");
    CsdlEntityType entityTypeProvider = new CsdlEntityType();
    entityTypeProvider.setKey(Collections.emptyList());

    List<CsdlNavigationProperty> navigationProperties = new ArrayList<CsdlNavigationProperty>();
    navigationProperties.add(new CsdlNavigationProperty().setName("partnerName").setType(entityTypeName));
    entityTypeProvider.setNavigationProperties(navigationProperties);
    when(provider.getEntityType(entityTypeName)).thenReturn(entityTypeProvider);
    CsdlNavigationProperty propertyProvider = new CsdlNavigationProperty();
    propertyProvider.setType(entityTypeName);
    propertyProvider.setNullable(false);
    propertyProvider.setPartner("partnerName");
    EdmNavigationProperty property = new EdmNavigationPropertyImpl(edm, propertyProvider);
    EdmNavigationProperty partner = property.getPartner();
    assertNotNull(partner);

    // Caching
      assertSame(partner, property.getPartner());
  }

  @Test(expected = EdmException.class)
  public void navigationPropertyWithNonexistentPartner() throws Exception {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    FullQualifiedName entityTypeName = new FullQualifiedName("ns", "entity");
    CsdlEntityType entityTypeProvider = new CsdlEntityType();
    entityTypeProvider.setKey(Collections.emptyList());

    List<CsdlNavigationProperty> navigationProperties = new ArrayList<CsdlNavigationProperty>();
    navigationProperties.add(new CsdlNavigationProperty().setName("partnerName").setType(entityTypeName));
    entityTypeProvider.setNavigationProperties(navigationProperties);
    when(provider.getEntityType(entityTypeName)).thenReturn(entityTypeProvider);
    CsdlNavigationProperty propertyProvider = new CsdlNavigationProperty();
    propertyProvider.setType(entityTypeName);
    propertyProvider.setNullable(false);
    propertyProvider.setPartner("wrong");
    EdmNavigationProperty property = new EdmNavigationPropertyImpl(edm, propertyProvider);
    property.getPartner();
  }

  @Test(expected = EdmException.class)
  public void navigationPropertyWithNonExistentType() {
    EdmProviderImpl edm = mock(EdmProviderImpl.class);
    CsdlNavigationProperty propertyProvider = new CsdlNavigationProperty();
    EdmNavigationProperty property = new EdmNavigationPropertyImpl(edm, propertyProvider);
    property.getType();
  }

}