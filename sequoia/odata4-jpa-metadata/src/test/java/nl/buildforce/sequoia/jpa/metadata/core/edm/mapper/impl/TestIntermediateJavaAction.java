package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmAction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAParameter;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.ODataAction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects.ExampleJavaActions;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects.ExampleJavaEmConstructor;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects.ExampleJavaPrivateConstructor;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects.ExampleJavaTwoParameterConstructor;
// import org.apache.olingo.commons.api.edm.geo.Geospatial.Dimension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestIntermediateJavaAction extends TestMappingRoot {
  private TestHelper helper;

  @BeforeEach
  public void setup() throws ODataJPAModelException {
    helper = new TestHelper(emf.getMetamodel(), PUNIT_NAME);
  }

  @Test
  public void checkInternalNameEqualMethodName() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");

    assertEquals("unboundWithImport", act.getInternalName());
  }

  @Test
  public void checkInternalNameGiven() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");

    assertEquals("unboundWithImport", act.getInternalName());
  }

  @Test
  public void checkExternalNameEqualMethodName() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");

    assertEquals("UnboundWithImport", act.getExternalName());
  }

  @Test
  public void checkReturnsFalseForIsBound() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");

    assertNotNull(act.getEdmItem());
    assertFalse(act.getEdmItem().isBound());
  }

  @Test
  public void checkReturnsTrueForIsBound() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "boundNoImport");

    assertNotNull(act.getEdmItem());
    assertTrue(act.getEdmItem().isBound());
    assertEquals(PUNIT_NAME + ".Person", act.getEdmItem().getParameters().get(0).getTypeFQN()
        .getFullQualifiedNameAsString());
  }

  @Test
  public void checkReturnsEntitySetPathForBound() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "boundWithEntitySetPath");

    assertNotNull(act.getEdmItem());
    assertTrue(act.getEdmItem().isBound());
    assertEquals("Person/Roles", act.getEdmItem().getEntitySetPath());
  }

  @Test
  public void checkReturnsGivenEntitySetTypeIfBound() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "boundNoImport");

    assertNotNull(act.getEdmItem());
    assertTrue(act.getEdmItem().isBound());
    assertEquals(PUNIT_NAME + ".Person", act.getEdmItem().getParameters().get(0).getTypeFQN()
        .getFullQualifiedNameAsString());
    assertEquals("Edm.Decimal", act.getEdmItem().getParameters().get(1).getTypeFQN()
        .getFullQualifiedNameAsString());
  }

  @Test
  public void checkReturnsExternalName() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "boundNoImport");

    assertNotNull(act.getEdmItem());
    assertEquals("BoundNoImport", act.getEdmItem().getName());
  }

  @Test
  public void checkReturnsTrueForHasActionImportIfUnbound() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");

    assertTrue(act.hasImport());
  }

  @Test
  public void checkReturnsFalseForHasActionImportIfNotSet() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "boundNoImport");

    assertFalse(act.hasImport());
  }

  @Test
  public void checkReturnsReturnTypeConvertedPrimitiveReturnType() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");

    assertNotNull(act.getEdmItem());
    assertNotNull(act.getEdmItem().getReturnType());
    assertEquals("Edm.Int32", act.getEdmItem().getReturnType().getType());
  }

  @Test
  public void checkReturnsReturnTypeNullForVoid() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "boundNoImport");

    assertNotNull(act.getEdmItem());
    assertNull(act.getEdmItem().getReturnType());
  }

  @Test
  public void checkReturnsReturnTypeEmbeddableType() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "returnEmbeddable");

    assertEquals("nl.buildforce.sequoia.jpa.ChangeInformation", act.getEdmItem().getReturnType().getType());
  }

  @Test
  public void checkReturnsEntityTypeAsReturnType() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "returnEntity");

    assertEquals("nl.buildforce.sequoia.jpa.Person", act.getEdmItem().getReturnType().getType());
  }

  @Test
  public void checkReturnsEnumerationTypeAsReturnType() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "returnEnumeration");

    assertEquals("nl.buildforce.sequoia.jpa.AbcClassification", act.getEdmItem().getReturnType().getType());
  }

  @Test
  public void checkReturnsReturnTypeCollectionOfPrimitive() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "returnCollection");

    assertEquals("Edm.String", act.getEdmItem().getReturnType().getType());
    assertTrue(act.getEdmItem().getReturnType().isCollection());
  }

  @Test
  public void checkReturnsReturnTypeCollectionOfEmbeddable() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "returnEmbeddableCollection");

    assertEquals("nl.buildforce.sequoia.jpa.ChangeInformation", act.getEdmItem().getReturnType().getType());
    assertTrue(act.getEdmItem().getReturnType().isCollection());
  }

  @Test
  public void checkReturnsReturnTypeFacetForNumbers() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundReturnFacet");
    assertFalse(act.getEdmItem().getReturnType().isNullable());
    assertEquals(Integer.valueOf(20), act.getEdmItem().getReturnType().getPrecision());
    assertEquals(Integer.valueOf(5), act.getEdmItem().getReturnType().getScale());
  }

  @Test
  public void checkReturnsReturnTypeFacetForNonNumbers() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");

    assertNull(act.getEdmItem().getReturnType().getPrecision());
    assertNull(act.getEdmItem().getReturnType().getScale());
    assertNull(act.getEdmItem().getReturnType().getMaxLength());
  }

  @Test
  public void checkReturnsReturnTypeFacetForStringsAndGeo() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "calculateLocation");

    // assertEquals(Integer.valueOf(60), act.getEdmItem().getReturnType().getMaxLength());
    // assertEquals(Dimension.GEOGRAPHY, act.getEdmItem().getReturnType().getSrid().getDimension());
    // assertEquals("4326", act.getEdmItem().getReturnType().getSrid().toString());
  }

  @Test
  public void checkReturnsParameterConvertPrimitiveTypes() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");

    assertNotNull(act.getEdmItem());
    assertNotNull(act.getEdmItem().getParameters());
    assertEquals(2, act.getEdmItem().getParameters().size());
    assertNotNull(act.getEdmItem().getParameter("A"));
    assertNotNull(act.getEdmItem().getParameter("B"));
    assertEquals("Edm.Int16", act.getEdmItem().getParameter("A").getType());
    assertEquals("Edm.Int32", act.getEdmItem().getParameter("B").getType());
  }

  @Test
  public void checkReturnsParameterFacetForNumbers() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "boundNoImport");

    assertNotNull(act.getParameter());
    assertEquals(Integer.valueOf(34), act.getParameter().get(1).getPrecision());
    assertEquals(Integer.valueOf(10), act.getParameter().get(1).getScale());

    assertNotNull(act.getEdmItem().getParameters());
    assertEquals(Integer.valueOf(34), act.getEdmItem().getParameters().get(1).getPrecision());
    assertEquals(Integer.valueOf(10), act.getEdmItem().getParameters().get(1).getScale());
  }

  @Test
  public void checkReturnsParameterFacetForNonNumbers() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");

    assertNotNull(act.getEdmItem().getParameters());
    assertNull(act.getEdmItem().getParameters().get(1).getPrecision());
    assertNull(act.getEdmItem().getParameters().get(1).getScale());
    assertNull(act.getEdmItem().getParameters().get(1).getMaxLength());
  }

  @Disabled
  @Test
  public void checkReturnsParameterFacetForStringsAndGeo() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "calculateLocation");

    // assertNotNull(act.getParameter());
    // assertEquals(Integer.valueOf(100), act.getParameter().get(0).getMaxLength());
    // assertEquals(Dimension.GEOGRAPHY, act.getParameter().get(0).getSrid().getDimension());
    // assertEquals("4326", act.getParameter().get(0).getSrid().toString());

    assertNotNull(act.getEdmItem().getParameters());
    assertEquals(Integer.valueOf(100), act.getEdmItem().getParameters().get(0).getMaxLength());
    // assertEquals(Dimension.GEOGRAPHY, act.getEdmItem().getParameters().get(0).getSrid().getDimension());
    // assertEquals("4326", act.getEdmItem().getParameters().get(0).getSrid().toString());
  }

  @Test
  public void checkReturnsEnumerationTypeAsParameter() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "returnEnumeration");

    assertEquals("nl.buildforce.sequoia.jpa.AccessRights", act.getEdmItem().getParameters().get(0).getTypeFQN()
        .getFullQualifiedNameAsString());
  }

  @Test
  public void checkProvidesAllParameter() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");
    List<JPAParameter> actParams = act.getParameter();
    assertEquals(2, actParams.size());
  }

  @Test
  public void checkProvidesParameterByDeclared() throws ODataJPAModelException, NoSuchMethodException,
      SecurityException {

    Method m = ExampleJavaActions.class.getMethod("unboundWithImport", short.class, int.class);
    Parameter[] params = m.getParameters();
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");
    assertNotNull(act.getParameter(params[0]));
    assertEquals("A", act.getParameter(params[0]).getName());
    assertNotNull(act.getParameter(params[1]));
    assertEquals("B", act.getParameter(params[1]).getName());
  }

  @Test
  public void checkExceptConstructorWithoutParameter() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "unboundWithImport");
    act.getEdmItem();
    assertNotNull(act.getConstructor());
  }

  @Test
  public void checkExceptConstructorWithEntityManagerParameter() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaEmConstructor.class, "mul");

    assertNotNull(act.getConstructor());
    assertEquals(1, act.getConstructor().getParameterTypes().length);
  }

  @Test
  public void checkThrowsExceptionForNonPrimitiveParameter() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "errorNonPrimitiveParameter");
    assertThrows(ODataJPAModelException.class, act::getEdmItem);
  }

  @Test
  public void checkThrowsExceptionIfCollectionAndReturnTypeEmpty() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "returnCollectionWithoutReturnType");
    assertThrows(ODataJPAModelException.class, act::getEdmItem);
  }

  @Test
  public void checkThrowsExceptionOnPrivateConstructor() {
    assertThrows(ODataJPAModelException.class, () -> createAction(ExampleJavaPrivateConstructor.class, "mul"));
  }

  @Test
  public void checkThrowsExceptionOnNoConstructorAsSpecified() {
    assertThrows(ODataJPAModelException.class, () -> createAction(ExampleJavaTwoParameterConstructor.class, "mul"));
  }

  @Test
  public void checkThrowsExceptionOnIsBoundWithoutEntityTypeParameter() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "boundWithOutBindingParameter");
    assertThrows(ODataJPAModelException.class, act::getEdmItem);
  }

  @Test
  public void checkThrowsExceptionOnIsBoundWithoutParameter() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "boundWithOutParameter");
    assertThrows(ODataJPAModelException.class, act::getEdmItem);
  }

  @Test
  public void checkThrowsExceptionOnIsBoundBindingParameterNotFirst() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "boundBindingParameterSecondParameter");
    assertThrows(ODataJPAModelException.class, act::getEdmItem);
  }

  @Test
  public void checkThrowsExceptionOnEntitySetGivenUnbound() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "errorUnboundWithEntitySetPath");
    assertThrows(ODataJPAModelException.class, act::getEdmItem);
  }

  @Test
  public void checkThrowsExceptionOnEntitySetGivenNoEntityReturnType() throws ODataJPAModelException {
    IntermediateJavaAction act = createAction(ExampleJavaActions.class, "errorPrimitiveTypeWithEntitySetPath");
    assertThrows(ODataJPAModelException.class, act::getEdmItem);
  }

  private IntermediateJavaAction createAction(Class<? extends ODataAction> clazz, String method)
      throws ODataJPAModelException {
    for (Method m : clazz.getMethods()) {
      EdmAction actionDescription = m.getAnnotation(EdmAction.class);
      if (actionDescription != null && method.equals(m.getName())) {
        return new IntermediateJavaAction(new JPADefaultEdmNameBuilder(PUNIT_NAME), actionDescription, m, helper.schema);
      }
    }
    return null;
  }
}