package nl.buildforce.sequoia.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmBindingTarget;
import nl.buildforce.olingo.commons.api.edm.EdmComplexType;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEntityContainer;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestIntermediateServiceDocument extends TestMappingRoot {

  private JPAServiceDocument cut;

  static Stream<Arguments> getEntityTypeByFqn() {
    return Stream.of(
        arguments(new FullQualifiedName("nl.buildforce.sequoia.BusinessPartner"), false),
        arguments(new FullQualifiedName("nl.buildforce.sequoia.Dummy"), true),
        arguments(new FullQualifiedName("dummy.BusinessPartner"), true));
  }

  static Stream<Arguments> getEntityTypeByEsName() {
    return Stream.of(
        arguments("BusinessPartners", false),
        arguments("Dummy", true));
  }

  static Stream<Arguments> getEnumType() {
    return Stream.of(
        arguments("nl.buildforce.sequoia.AccessRights", false),
        arguments("nl.buildforce.sequoia.Dummy", true),
        arguments("Unknown.AccessRights", true));
  }

  @BeforeEach
  public void setup() throws ODataJPAModelException {
    cut = new IntermediateServiceDocument(PUNIT_NAME, emf.getMetamodel(), /*null,*/
        new String[] { "nl.buildforce.sequoia.processor.core.testmodel" });
  }

  @Test
  public void checkServiceDocumentCanBeCreated() throws ODataJPAModelException {
    new IntermediateServiceDocument(PUNIT_NAME, emf.getMetamodel(), /*null,*/
        new String[] { "nl.buildforce.sequoia.processor.core.testmodel" });
  }

  @Test
  public void checkServiceDocumentGetSchemaList() throws ODataJPAModelException {
    assertEquals(1, cut.getEdmSchemas().size(), "Wrong number of schemas");
  }

  @Test
  public void checkServiceDocumentGetContainer() throws ODataJPAModelException {
    assertNotNull(cut.getEdmEntityContainer(), "Entity Container not found");
  }

  @Test
  public void checkServiceDocumentGetContainerFromSchema() throws ODataJPAModelException {

    List<CsdlSchema> schemas = cut.getEdmSchemas();
    CsdlSchema schema = schemas.get(0);
    assertNotNull(schema.getEntityContainer(), "Entity Container not found");
  }

  @Test
  public void checkServiceDocumentGetEntitySetsFromContainer() throws ODataJPAModelException {
    CsdlEntityContainer container = cut.getEdmEntityContainer();
    assertNotNull(container.getEntitySets(), "Entity Container not found");
  }

  @Test
  public void checkHasEtagReturnsTrueOnVersion() {
    EdmBindingTarget target = mock(EdmBindingTarget.class);
    EdmEntityType et = mock(EdmEntityType.class);
    when(target.getEntityType()).thenReturn(et);
    when(et.getFullQualifiedName()).thenReturn(new FullQualifiedName(PUNIT_NAME, "BusinessPartner"));

    assertTrue(cut.hasETag(target));
  }

  @Test
  public void checkHasEtagReturnsFalseWithoutVersion() throws ODataJPAModelException {
    EdmBindingTarget target = mock(EdmBindingTarget.class);
    EdmEntityType et = mock(EdmEntityType.class);
    when(target.getEntityType()).thenReturn(et);
    when(et.getFullQualifiedName()).thenReturn(new FullQualifiedName(PUNIT_NAME, "Country"));

    JPAServiceDocument svc = new IntermediateServiceDocument(PUNIT_NAME, emf.getMetamodel(), /*null,*/ null);
    assertFalse(svc.hasETag(target));
  }

  @ParameterizedTest
  @MethodSource("getEnumType")
  public void checkGetEnumType(final String enumName, final boolean isNull) {
    if (isNull)
      assertNull(cut.getEnumType(enumName));
    else
      assertNotNull(cut.getEnumType(enumName));
  }

  @ParameterizedTest
  @MethodSource("getEntityTypeByEsName")
  public void checkGetEntityTypeByEsName(final String esName, final boolean isNull) throws ODataJPAModelException {
    if (isNull)
      assertNull(cut.getEntity(esName));
    else
      assertNotNull(cut.getEntity(esName));
  }

  @ParameterizedTest
  @MethodSource("getEntityTypeByFqn")
  public void checkGetEntityTypeByFqn(final FullQualifiedName etFqn, final boolean isNull) {
    if (isNull)
      assertNull(cut.getEntity(etFqn));
    else
      assertNotNull(cut.getEntity(etFqn));
  }

  @Test
  public void checkGetEntityTypeByEdmType() {
    final EdmEntityType type = mock(EdmEntityType.class);
    when(type.getNamespace()).thenReturn("nl.buildforce.sequoia");
    when(type.getName()).thenReturn("BusinessPartner");
    assertNotNull(cut.getEntity(type));
  }

  @Test
  public void checkGetEntityTypeByEdmTypeReturnNullOnUnknown() {
    final EdmEntityType type = mock(EdmEntityType.class);
    when(type.getNamespace()).thenReturn("nl.buildforce.sequoia");
    when(type.getName()).thenReturn("Unknown");
    assertNull(cut.getEntity(type));
  }

  @Test
  public void checkGetEntityTypeByEdmTypeReturnNullOnUnknownSchema() {
    final EdmEntityType type = mock(EdmEntityType.class);
    when(type.getNamespace()).thenReturn("Unknown");
    when(type.getName()).thenReturn("BoundNoImport");
    assertNull(cut.getEntity(type));
  }

  @Test
  public void checkGetComplexTypeByEdmType() {
    final EdmComplexType type = mock(EdmComplexType.class);
    when(type.getNamespace()).thenReturn("nl.buildforce.sequoia");
    when(type.getName()).thenReturn("CommunicationData");
    assertNotNull(cut.getComplexType(type));
  }

  @Test
  public void checkGetComplexTypeByEdmTypeReturnNullOnUnknown() {
    final EdmComplexType type = mock(EdmComplexType.class);
    when(type.getNamespace()).thenReturn("nl.buildforce.sequoia");
    when(type.getName()).thenReturn("Unknown");
    assertNull(cut.getComplexType(type));
  }

  @Test
  public void checkGetComplexTypeByEdmTypeReturnNullOnUnknownSchema() {
    final EdmComplexType type = mock(EdmComplexType.class);
    when(type.getNamespace()).thenReturn("Unknown");
    when(type.getName()).thenReturn("BoundNoImport");
    assertNull(cut.getComplexType(type));
  }

  @Test
  public void checkGetAction() throws ODataJPAModelException {
    final EdmAction action = mock(EdmAction.class);
    when(action.getNamespace()).thenReturn("nl.buildforce.sequoia");
    when(action.getName()).thenReturn("BoundNoImport");
    final JPAServiceDocument svc = new IntermediateServiceDocument(PUNIT_NAME, emf.getMetamodel(), /*null,*/
        new String[] { "nl.buildforce.sequoia.metadata.core.edm.mapper.testaction" });
    assertNotNull(svc.getAction(action));
  }

  @Test
  public void checkGetActionReturnNullOnUnknownAction() {
    final EdmAction action = mock(EdmAction.class);
    when(action.getNamespace()).thenReturn("nl.buildforce.sequoia");
    when(action.getName()).thenReturn("Unknown");

    assertNull(cut.getAction(action));
  }

  @Test
  public void checkGetActionReturnNullOnUnknownSchema() {
    final EdmAction action = mock(EdmAction.class);
    when(action.getNamespace()).thenReturn("Unknown");
    when(action.getName()).thenReturn("BoundNoImport");

    assertNull(cut.getAction(action));
  }

  @Test
  public void checkGetFunction() {
    final EdmFunction function = mock(EdmFunction.class);
    when(function.getNamespace()).thenReturn("nl.buildforce.sequoia");
    when(function.getName()).thenReturn("ConvertToQkm");
    assertNotNull(cut.getFunction(function));
  }

  @Test
  public void checkGetFunctionReturnNullOnUnknownFunction() {
    final EdmFunction function = mock(EdmFunction.class);
    when(function.getNamespace()).thenReturn("nl.buildforce.sequoia");
    when(function.getName()).thenReturn("Unknown");
    assertNull(cut.getFunction(function));
  }

  @Test
  public void checkGetFunctionReturnNullOnUnknownSchema() {
    final EdmFunction function = mock(EdmFunction.class);
    when(function.getNamespace()).thenReturn("Unknown");
    when(function.getName()).thenReturn("BoundNoImport");
    assertNull(cut.getFunction(function));
  }

  @Test
  public void checkHasMediaETagNotSupported() {
    final EdmBindingTarget target = mock(EdmBindingTarget.class);
    assertFalse(cut.hasMediaETag(target));
  }

  @Test
  public void checkGetEntityTypeReturnsSetCustomName() throws ODataJPAModelException {
    cut = createCutWithCustomNameBuilder();
    assertNotNull(cut.getEntity("Business_Partner".toUpperCase()));
  }

  @Test
  public void checkGetEntityTypeReturnsEdmTypCustomName() throws ODataJPAModelException {
    cut = createCutWithCustomNameBuilder();
    final EdmType edmType = mock(EdmType.class);
    when(edmType.getName()).thenReturn("Business_Partner");
    when(edmType.getNamespace()).thenReturn("test");

    assertNotNull(cut.getEntity(edmType));
  }

  @Test
  public void checkGetPropertyReturnsCustomName() throws ODataJPAModelException {
    cut = createCutWithCustomNameBuilder();
    assertEquals("creationDateTime", cut.getEntity("Business_Partner".toUpperCase()).getAttribute("creationDateTime")
        .getExternalName());
  }

  @Test
  public void checkGetComplexTypeReturnsCustomName() throws ODataJPAModelException {
    cut = createCutWithCustomNameBuilder();
    final EdmComplexType type = mock(EdmComplexType.class);
    when(type.getNamespace()).thenReturn("test");
    when(type.getName()).thenReturn("T_CommunicationData");
    assertNotNull(cut.getComplexType(type));
  }

  @Test
  public void checkGetContainerReturnsCustomName() throws ODataJPAModelException {
    cut = createCutWithCustomNameBuilder();
    assertEquals("service_container", cut.getEdmEntityContainer().getName());
  }

  @Test
  public void checkGetEnumReturnsCustomName() throws ODataJPAModelException {
    cut = createCutWithCustomNameBuilder();
    assertNotNull(cut.getEnumType("test.E_AccessRights"));
  }

  @Test
  public void checkGetActionCustomName() throws ODataJPAModelException {
    cut = createCutWithCustomNameBuilder();
    final EdmAction action = mock(EdmAction.class);
    when(action.getNamespace()).thenReturn("test");
    when(action.getName()).thenReturn("O_BoundNoImport");
    assertNotNull(cut.getAction(action));
  }

  @Test
  public void checkGetFunctionCustomName() throws ODataJPAModelException {
    cut = createCutWithCustomNameBuilder();
    final EdmFunction function = mock(EdmFunction.class);
    when(function.getNamespace()).thenReturn("test");
    when(function.getName()).thenReturn("O_sum");
    assertNotNull(cut.getFunction(function));
  }

  private IntermediateServiceDocument createCutWithCustomNameBuilder() throws ODataJPAModelException {
    return new IntermediateServiceDocument(new CustomJPANameBuilder(), emf.getMetamodel(), /*null,*/
        new String[] { "nl.buildforce.sequoia.processor.core.testmodel",
            "nl.buildforce.sequoia.metadata.core.edm.mapper.testaction" });
  }

}