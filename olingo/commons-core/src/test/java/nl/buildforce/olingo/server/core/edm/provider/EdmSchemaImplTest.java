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

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmActionImport;
import nl.buildforce.olingo.commons.api.edm.EdmAnnotations;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmEnumType;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmFunctionImport;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmSchema;
import nl.buildforce.olingo.commons.api.edm.EdmSingleton;
import nl.buildforce.olingo.commons.api.edm.EdmTypeDefinition;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAction;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlActionImport;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAliasInfo;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlAnnotations;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlComplexType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainer;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntitySet;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEnumType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlFunction;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlFunctionImport;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlSchema;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlSingleton;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlTerm;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlTypeDefinition;
import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import org.junit.Before;
import org.junit.Test;

public class EdmSchemaImplTest {

  private EdmSchema schema;
  private Edm edm;
  public static final String NAMESPACE = "org.namespace";
  public static final String ALIAS = "alias";

  @Before
  public void before() {
    CsdlEdmProvider provider = new LocalProvider();
    edm = new EdmProviderImpl(provider);
    schema = edm.getSchemas().get(0);

  }

  @Test
  public void initialSchemaTest() {
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    edm = new EdmProviderImpl(provider);
    edm.getSchemas();
  }

  @Test
  public void emptySchemaTest() throws Exception {
    ArrayList<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
    CsdlSchema providerSchema = new CsdlSchema();
    schemas.add(providerSchema);
    CsdlEdmProvider provider = mock(CsdlEdmProvider.class);
    when(provider.getSchemas()).thenReturn(schemas);
    edm = new EdmProviderImpl(provider);
    edm.getSchemas();
  }

  @Test
  public void basicGetters() {
    assertEquals("org.namespace", schema.getNamespace());
    assertEquals("alias", schema.getAlias());
  }

  @Test
  public void getTypeDefinitions() {
    List<EdmTypeDefinition> typeDefinitions = schema.getTypeDefinitions();
    assertNotNull(typeDefinitions);
    assertEquals(2, typeDefinitions.size());

    for (EdmTypeDefinition def : typeDefinitions) {
      assertSame(def, edm.getTypeDefinition(new FullQualifiedName(NAMESPACE, def.getName())));
      assertSame(def, edm.getTypeDefinition(new FullQualifiedName(ALIAS, def.getName())));
    }
  }

  @Test
  public void getEnumTypes() {
    List<EdmEnumType> enumTypes = schema.getEnumTypes();
    assertNotNull(enumTypes);
    assertEquals(2, enumTypes.size());

    for (EdmEnumType enumType : enumTypes) {
      assertSame(enumType, edm.getEnumType(new FullQualifiedName(NAMESPACE, enumType.getName())));
      assertSame(enumType, edm.getEnumType(new FullQualifiedName(ALIAS, enumType.getName())));
    }
  }

  @Test
  public void getEntityTypes() {
    List<EdmEntityType> entityTypes = schema.getEntityTypes();
    assertNotNull(entityTypes);
    assertEquals(2, entityTypes.size());

    for (EdmEntityType entityType : entityTypes) {
      assertSame(entityType, edm.getEntityType(new FullQualifiedName(NAMESPACE, entityType.getName())));
      assertSame(entityType, edm.getEntityType(new FullQualifiedName(ALIAS, entityType.getName())));
    }
  }

  @Test
  public void getComplexTypes() {
    List<EdmComplexType> complexTypes = schema.getComplexTypes();
    assertNotNull(complexTypes);
    assertEquals(2, complexTypes.size());

    for (EdmComplexType complexType : complexTypes) {
      assertSame(complexType, edm.getComplexType(new FullQualifiedName(NAMESPACE, complexType.getName())));
      assertSame(complexType, edm.getComplexType(new FullQualifiedName(ALIAS, complexType.getName())));
    }
  }

  @Test
  public void getActions() {
    List<EdmAction> actions = schema.getActions();
    assertNotNull(actions);
    assertEquals(2, actions.size());

    for (EdmAction action : actions) {
      assertSame(action, edm.getUnboundAction(new FullQualifiedName(NAMESPACE, action.getName())));
      assertSame(action, edm.getUnboundAction(new FullQualifiedName(ALIAS, action.getName())));
    }
  }

  @Test
  public void getFunctions() {
    List<EdmFunction> functions = schema.getFunctions();
    assertNotNull(functions);
    assertEquals(2, functions.size());

    for (EdmFunction function : functions) {
      FullQualifiedName functionName = new FullQualifiedName(NAMESPACE, function.getName());
      assertSame(function, edm.getUnboundFunction(functionName, null));

      functionName = new FullQualifiedName(ALIAS, function.getName());
      assertSame(function, edm.getUnboundFunction(functionName, null));
    }
  }

  @Test
  public void getAnnotationGroups() {
    List<EdmAnnotations> annotationGroups = schema.getAnnotationGroups();
    assertNotNull(annotationGroups);
    assertEquals(2, annotationGroups.size());

    for (EdmAnnotations annotationGroup : annotationGroups) {
      FullQualifiedName targetName = new FullQualifiedName(annotationGroup.getTargetPath());
      assertSame(annotationGroup, edm.getAnnotationGroup(targetName, null));
      targetName = new FullQualifiedName(annotationGroup.getTargetPath().replace(NAMESPACE, ALIAS));
      assertSame(annotationGroup, edm.getAnnotationGroup(targetName, null));
    }
  }

  @Test
  public void getContainer() {
    EdmEntityContainer container = schema.getEntityContainer();
    assertNotNull(container);

    List<EdmEntitySet> entitySets = container.getEntitySets();
    assertNotNull(entitySets);
    assertEquals(2, entitySets.size());
    for (EdmEntitySet obj : entitySets) {
      assertNotNull(obj.getEntityType());
    }

    List<EdmSingleton> singletons = container.getSingletons();
    assertNotNull(singletons);
    assertEquals(2, singletons.size());
    for (EdmSingleton obj : singletons) {
      assertNotNull(obj.getEntityType());
    }

    List<EdmActionImport> actionImports = container.getActionImports();
    assertNotNull(actionImports);
    assertEquals(2, actionImports.size());
    for (EdmActionImport obj : actionImports) {
      assertNotNull(obj.getUnboundAction());
    }

    List<EdmFunctionImport> functionImports = container.getFunctionImports();
    assertNotNull(functionImports);
    assertEquals(2, functionImports.size());
    for (EdmFunctionImport obj : functionImports) {
      assertNotNull(obj.getFunctionFqn());
    }

    assertSame(container, edm.getEntityContainer(new FullQualifiedName(schema.getNamespace(), container.getName())));
    assertSame(container, edm.getEntityContainer(null));
    assertSame(container, edm.getEntityContainer());
  }

  private static class LocalProvider implements CsdlEdmProvider {

    @Override
    public CsdlEnumType getEnumType(FullQualifiedName enumTypeName) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public CsdlTypeDefinition getTypeDefinition(FullQualifiedName typeDefinitionName) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {
      return null;
    }

    @Override
    public CsdlComplexType getComplexType(FullQualifiedName complexTypeName) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public List<CsdlAction> getActions(FullQualifiedName actionName) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public List<CsdlFunction> getFunctions(FullQualifiedName functionName) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public CsdlTerm getTerm(FullQualifiedName termName) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public CsdlSingleton getSingleton(FullQualifiedName entityContainer, String singletonName) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public CsdlActionImport getActionImport(FullQualifiedName entityContainer, String actionImportName) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public CsdlFunctionImport getFunctionImport(FullQualifiedName entityContainer,
                                                String functionImportName) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public List<CsdlAliasInfo> getAliasInfos() {
      return Collections.emptyList();
    }

    @Override
    public List<CsdlSchema> getSchemas() {
      CsdlSchema providerSchema = new CsdlSchema();
      providerSchema.setNamespace(NAMESPACE);
      providerSchema.setAlias(ALIAS);
      CsdlEntityContainer container = new CsdlEntityContainer().setName("container");

      List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
      entitySets.add(new CsdlEntitySet().setName("entitySetName")
          .setType(new FullQualifiedName(NAMESPACE, "entityType1")));
      entitySets
          .add(new CsdlEntitySet().setName("entitySetName2").setType(new FullQualifiedName(NAMESPACE, "entityType2")));
      container.setEntitySets(entitySets);

      List<CsdlSingleton> singletons = new ArrayList<CsdlSingleton>();
      singletons.add(new CsdlSingleton().setName("singletonName")
          .setType(new FullQualifiedName(NAMESPACE, "entityType1")));
      singletons
          .add(new CsdlSingleton().setName("singletonName2").setType(new FullQualifiedName(NAMESPACE, "entityType2")));
      container.setSingletons(singletons);

      List<CsdlActionImport> actionImports = new ArrayList<CsdlActionImport>();
      actionImports.add(new CsdlActionImport().setName("actionImportName").setAction(
          new FullQualifiedName(NAMESPACE, "action1")));
      actionImports.add(new CsdlActionImport().setName("actionImportName2").setAction(
          new FullQualifiedName(NAMESPACE, "action2")));
      container.setActionImports(actionImports);

      List<CsdlFunctionImport> functionImports = new ArrayList<CsdlFunctionImport>();
      functionImports.add(new CsdlFunctionImport().setName("functionImportName").setFunction(
          new FullQualifiedName(NAMESPACE, "function1")));
      functionImports.add(new CsdlFunctionImport().setName("functionImportName2").setFunction(
          new FullQualifiedName(NAMESPACE, "function2")));
      container.setFunctionImports(functionImports);
      providerSchema.setEntityContainer(container);

      List<CsdlTypeDefinition> typeDefinitions = new ArrayList<CsdlTypeDefinition>();
      typeDefinitions.add(new CsdlTypeDefinition().setName("typeDefinition1").setUnderlyingType(
          EdmPrimitiveTypeKind.String.getFullQualifiedName()));
      typeDefinitions.add(new CsdlTypeDefinition().setName("typeDefinition2").setUnderlyingType(
          EdmPrimitiveTypeKind.String.getFullQualifiedName()));
      providerSchema.setTypeDefinitions(typeDefinitions);

      List<CsdlEnumType> enumTypes = new ArrayList<CsdlEnumType>();
      enumTypes.add(new CsdlEnumType().setName("enumType1"));
      enumTypes.add(new CsdlEnumType().setName("enumType2"));
      providerSchema.setEnumTypes(enumTypes);

      List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
      entityTypes.add(new CsdlEntityType().setName("entityType1"));
      entityTypes.add(new CsdlEntityType().setName("entityType2")
          .setBaseType(new FullQualifiedName(NAMESPACE, "entityType1")));
      providerSchema.setEntityTypes(entityTypes);

      List<CsdlComplexType> complexTypes = new ArrayList<CsdlComplexType>();
      complexTypes.add(new CsdlComplexType().setName("complexType1"));
      complexTypes.add(new CsdlComplexType().setName("complexType2").setBaseType(
          new FullQualifiedName(NAMESPACE, "complexType1")));
      providerSchema.setComplexTypes(complexTypes);

      List<CsdlAction> actions = new ArrayList<CsdlAction>();
      actions.add(new CsdlAction().setName("action1"));
      actions.add(new CsdlAction().setName("action2"));
      providerSchema.setActions(actions);

      List<CsdlFunction> functions = new ArrayList<CsdlFunction>();
      functions.add(new CsdlFunction().setName("function1"));
      functions.add(new CsdlFunction().setName("function2"));
      providerSchema.setFunctions(functions);

      List<CsdlAnnotations> annotationGroups = new ArrayList<CsdlAnnotations>();
      annotationGroups.add(new CsdlAnnotations().setTarget(NAMESPACE + ".entityType1"));
      annotationGroups.add(new CsdlAnnotations().setTarget(NAMESPACE + ".entityType2"));
      providerSchema.setAnnotationsGroup(annotationGroups);

      List<CsdlTerm> terms = new ArrayList<CsdlTerm>();
      terms.add(new CsdlTerm().setName("term1").setType("Edm.String"));
      terms.add(new CsdlTerm().setName("term2").setType("Edm.String"));
      providerSchema.setTerms(terms);

      ArrayList<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
      schemas.add(providerSchema);
      return schemas;
    }

    @Override
    public CsdlEntityContainer getEntityContainer() {
      throw new RuntimeException("Provider must not be called in the schema case");
    }

    @Override
    public CsdlAnnotations getAnnotationsGroup(FullQualifiedName targetName, String qualifier) {
      throw new RuntimeException("Provider must not be called in the schema case");
    }
  }

}