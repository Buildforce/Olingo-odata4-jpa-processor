package nl.buildforce.olingo.commons.core.edm;
/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
 
 import java.util.Arrays;
 import java.util.Collections;
 import java.util.List;
 
 import nl.buildforce.olingo.commons.api.edm.Edm;
 import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
 import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
 import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
 import nl.buildforce.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
 import nl.buildforce.olingo.commons.api.edm.provider.CsdlAliasInfo;
 import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
 import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainer;
 import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
 import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntitySet;
 import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityType;
 import nl.buildforce.olingo.commons.api.edm.provider.CsdlNavigationProperty;
 import nl.buildforce.olingo.commons.api.edm.provider.CsdlProperty;
 import nl.buildforce.olingo.commons.api.edm.provider.CsdlPropertyRef;
 import nl.buildforce.olingo.commons.api.edm.provider.CsdlSchema;
 import nl.buildforce.olingo.commons.api.ex.ODataException;
 import org.junit.Assert;
 import org.junit.Test;
 
 public class CrossServiceTest {
 
   private final CsdlEdmProvider provider1 = new CsdlProvider("One");
   private final CsdlEdmProvider provider2 = new CsdlProvider("Two");
   private final Edm edm1 = new EdmProviderImpl(provider1);
   private final Edm edm2 = new EdmProviderImpl(provider2);
 
   private class CsdlProvider extends CsdlAbstractEdmProvider {
 
     private static final String NAMESPACE_PREFIX = "Namespace.";
     private static final String ALIAS_PREFIX = "Alias";
     private static final String CONTAINER_PREFIX = "Container";
     private static final String ENTITY_SET_PREFIX = "EntitySet";
     private static final String ENTITY_TYPE_PREFIX = "EntityType";
     private final String name;
     private final String namespace;
     private final String other;
     private final CsdlEntitySet entitySet;
     private final CsdlEntityType entityType;
 
     private CsdlProvider(String name) {
       this.name = name;
    namespace = NAMESPACE_PREFIX + name;
       other = name.equals("One") ? "Two" : "One";
       entitySet = new CsdlEntitySet().setName(ENTITY_SET_PREFIX + name)
           .setType(new FullQualifiedName(namespace, ENTITY_TYPE_PREFIX + name));
       entityType = new CsdlEntityType().setName(ENTITY_TYPE_PREFIX + name)
           .setKey(Collections.singletonList(new CsdlPropertyRef().setName("ID" + name)))
           .setProperties(Collections.singletonList(
               new CsdlProperty().setName("ID" + name).setNullable(false).setType("Edm.Guid")))
           .setNavigationProperties(Collections.singletonList(
               new CsdlNavigationProperty().setName("Navigation" + other)
                   .setType(new FullQualifiedName(ALIAS_PREFIX + other, ENTITY_TYPE_PREFIX + other))));
     }
 
     @Override
     public List<CsdlAliasInfo> getAliasInfos() {
       return Arrays.asList(
           new CsdlAliasInfo().setNamespace(NAMESPACE_PREFIX + name).setAlias(ALIAS_PREFIX + name),
           new CsdlAliasInfo().setNamespace(NAMESPACE_PREFIX + other).setAlias(ALIAS_PREFIX + other));
     }
 
     @Override
     public List<CsdlSchema> getSchemas() throws ODataException {
       return Collections.singletonList(
           new CsdlSchema().setNamespace(namespace).setAlias(ALIAS_PREFIX + name)
               .setEntityContainer(getEntityContainer())
               .setEntityTypes(Collections.singletonList(entityType)));
     }
 
     @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName)
        throws ODataException {
      if (entityContainerName == null) {
        return new CsdlEntityContainerInfo().setContainerName(
            new FullQualifiedName(namespace, CONTAINER_PREFIX + name));
      } else if (namespace.equals(entityContainerName.getNamespace())) {
        if ((CONTAINER_PREFIX + name).equals(entityContainerName.getName())) {
          return new CsdlEntityContainerInfo().setContainerName(entityContainerName);
        }
        return null;
      } else if ((NAMESPACE_PREFIX + other).equals(entityContainerName.getNamespace())) {
        CsdlEdmProvider otherProvider = this == provider1 ? provider2 : provider1;
        return otherProvider.getEntityContainerInfo(entityContainerName);
      }
      return null;
    }

    @Override
    public CsdlEntityContainer getEntityContainer() {
      return new CsdlEntityContainer().setName(CONTAINER_PREFIX + name)
          .setEntitySets(Collections.singletonList(entitySet));
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName)
        throws ODataException {
      if (namespace.equals(entityContainer.getNamespace())) {
        if ((CONTAINER_PREFIX + name).equals(entityContainer.getName())
            && (ENTITY_SET_PREFIX + name).equals(entitySetName)) {
          return entitySet;
        }
        return null;
      } else if ((NAMESPACE_PREFIX + other).equals(entityContainer.getNamespace())) {
        CsdlEdmProvider otherProvider = this == provider1 ? provider2 : provider1;
        return otherProvider.getEntitySet(entityContainer, entitySetName);
      }
      return null;
    }

    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
      if (namespace.equals(entityTypeName.getNamespace())) {
        if ((ENTITY_TYPE_PREFIX + name).equals(entityTypeName.getName())) {
          return entityType;
        }
        return null;
      } else if ((NAMESPACE_PREFIX + other).equals(entityTypeName.getNamespace())) {
        // Requesting a type in a foreign namespace delegates the request to another CSDL provider.
        CsdlEdmProvider otherProvider = this == provider1 ? provider2 : provider1;
        return otherProvider.getEntityType(entityTypeName);
      }
      return null;
    }
  }

  @Test
  public void entityType() {
    FullQualifiedName typeName = new FullQualifiedName("Namespace.One", "EntityTypeOne");
    EdmEntityType entityType = edm1.getEntityType(typeName);
    Assert.assertNotNull(entityType);
    Assert.assertNotNull(entityType.getNavigationProperty("NavigationTwo"));

    // We get an entity type in a foreign namespace if it is used in our namespace.
    EdmEntityType targetType = entityType.getNavigationProperty("NavigationTwo").getType();
    Assert.assertNotNull(targetType);
    FullQualifiedName targetName = new FullQualifiedName("Namespace.Two", "EntityTypeTwo");
    Assert.assertEquals(targetName, targetType.getFullQualifiedName());

    // Directly accessing the foreign type is also possible.
    Assert.assertNotNull(edm1.getEntityType(targetName));
    Assert.assertEquals(targetType, edm1.getEntityType(targetName));

    // However, the schema contains only elements from the own namespace.
    List<EdmEntityType> entityTypes = edm1.getSchema("Namespace.One").getEntityTypes();
    Assert.assertNotNull(entityTypes);
    Assert.assertEquals(1, entityTypes.size());
    Assert.assertEquals(typeName, entityTypes.get(0).getFullQualifiedName());

    // The foreign service has the foreign type available, both directly and in its schema.
    Assert.assertNotNull(edm2.getEntityType(targetName));
    Assert.assertEquals(targetName, edm2.getEntityType(targetName).getFullQualifiedName());
    Assert.assertEquals(targetName,
        edm2.getSchema("AliasTwo").getEntityTypes().get(0).getFullQualifiedName());

    // Alias access is also supported.
    Assert.assertNotNull(edm1.getEntityType(new FullQualifiedName("AliasOne", "EntityTypeOne")));

    // A wrong name leads to null result.
    Assert.assertNull(edm1.getEntityType(new FullQualifiedName("AliasOne", "EntityTypeWrong")));
    Assert.assertNull(edm1.getEntityType(new FullQualifiedName("AliasTwo", "EntityTypeWrong")));
    Assert.assertNull(edm1.getEntityType(new FullQualifiedName("AliasWrong", "EntityTypeOne")));
  }

  @Test
  public void entityContainer() {
    Assert.assertNotNull(edm1.getEntityContainer());
    Assert.assertNotNull(edm1.getEntityContainer(new FullQualifiedName("Namespace.One", "ContainerOne")));
    Assert.assertEquals(edm1.getEntityContainer(),
        edm1.getEntityContainer(new FullQualifiedName("Namespace.One", "ContainerOne")));
    Assert.assertEquals(edm1.getEntityContainer(),
        edm1.getEntityContainer(new FullQualifiedName("AliasOne", "ContainerOne")));
    Assert.assertNotNull(edm1.getEntityContainer(new FullQualifiedName("AliasTwo", "ContainerTwo")));

    // A wrong name leads to null result.
    Assert.assertNull(edm1.getEntityContainer(new FullQualifiedName("AliasTwo", "ContainerOne")));
    Assert.assertNull(edm1.getEntityContainer(new FullQualifiedName("AliasWrong", "ContainerOne")));
  }

  @Test
  public void entitySet() {
    EdmEntitySet entitySet = edm1.getEntityContainer(new FullQualifiedName("AliasTwo", "ContainerTwo"))
        .getEntitySet("EntitySetTwo");
    Assert.assertNotNull(entitySet);
    Assert.assertEquals("EntitySetTwo", entitySet.getName());
    Assert.assertNotNull(entitySet.getEntityType());
    Assert.assertEquals("EntityTypeTwo", entitySet.getEntityType().getName());

    // A wrong name leads to null result.
    Assert.assertNull(edm1.getEntityContainer().getEntitySet("EntitySetTwo"));
    Assert.assertNull(
        edm1.getEntityContainer(new FullQualifiedName("AliasTwo", "ContainerTwo")).getEntitySet("EntitySetOne"));
  }

  @Test
  public void schema() {
    Assert.assertNotNull(edm1.getSchemas());
    Assert.assertEquals(1, edm1.getSchemas().size());
    Assert.assertEquals("AliasOne", edm1.getSchemas().get(0).getAlias());
    Assert.assertNotNull(edm1.getSchemas().get(0).getEntityTypes());
    Assert.assertEquals(1, edm1.getSchemas().get(0).getEntityTypes().size());
    Assert.assertEquals(new FullQualifiedName("Namespace.One", "EntityTypeOne"),
        edm1.getSchemas().get(0).getEntityTypes().get(0).getFullQualifiedName());
  }

  @Test
  public void entitySets() {
    Assert.assertNotNull(edm1.getEntityContainer());
    Assert.assertNotNull(edm1.getEntityContainer().getEntitySets());
    Assert.assertEquals(1, edm1.getEntityContainer().getEntitySets().size());
    Assert.assertEquals("EntitySetOne", edm1.getEntityContainer().getEntitySets().get(0).getName());
  }

 }