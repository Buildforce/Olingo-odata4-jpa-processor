package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivision;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AssertCollection;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartner;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.ChangeInformation;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.DateConverter;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Organization;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Person;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestIntermediateDataBaseFunction extends TestMappingRoot {
  private TestHelper helper;

  @BeforeEach
  public void setup() throws ODataJPAModelException {
    helper = new TestHelper(emf.getMetamodel(), PUNIT_NAME);
  }

  @Test
  public void checkByEntityAnnotationCreate() {
    new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper.getStoredProcedure(helper.getEntityType(
        BusinessPartner.class), "CountRoles"), BusinessPartner.class, helper.schema);
  }

  @Test
  public void checkByEntityAnnotationGetName() throws ODataJPAModelException {
    IntermediateFunction func = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getStoredProcedure(
            helper.getEntityType(BusinessPartner.class), "CountRoles"), BusinessPartner.class, helper.schema);
    assertEquals("CountRoles", func.getEdmItem().getName());
  }

  @Test
  public void checkByEntityAnnotationGetFunctionName() {
    IntermediateFunction func = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getStoredProcedure(
            helper.getEntityType(BusinessPartner.class), "CountRoles"), BusinessPartner.class, helper.schema);
    assertEquals("COUNT_ROLES", func.getUserDefinedFunction());
  }

  @Test
  public void checkByEntityAnnotationInputParameterBound() throws ODataJPAModelException {
    IntermediateFunction func = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getStoredProcedure(
            helper.getEntityType(BusinessPartner.class), "CountRoles"), BusinessPartner.class, helper.schema);

    List<CsdlParameter> expInput = new ArrayList<>();
    CsdlParameter param = new CsdlParameter();
    param.setName("Key");
    param.setType(new FullQualifiedName("nl.buildforce.sequoia.jpa.BusinessPartner"));
    param.setNullable(false);
    expInput.add(param);
    AssertCollection.assertListEquals(expInput, func.getEdmItem().getParameters(), CsdlParameter.class);
  }

  @Test
  public void checkByEntityAnnotationInputParameterBoundCompoundKey() throws ODataJPAModelException {
    IntermediateFunction func = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getStoredProcedure(helper.getEntityType(AdministrativeDivision.class), "SiblingsBound"),
        AdministrativeDivision.class, helper.schema);

    List<CsdlParameter> expInput = new ArrayList<>();
    CsdlParameter param = new CsdlParameter();
    param.setName("Key");
    param.setType(new FullQualifiedName("nl.buildforce.sequoia.jpa.AdministrativeDivision"));
    param.setNullable(false);
    expInput.add(param);
    AssertCollection.assertListEquals(expInput, func.getEdmItem().getParameters(), CsdlParameter.class);
  }

  @Test
  public void checkByEntityAnnotationInputParameter2() throws ODataJPAModelException {
    IntermediateFunction func = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getStoredProcedure(
            helper.getEntityType(BusinessPartner.class), "IsPrime"), BusinessPartner.class, helper.schema);

    List<CsdlParameter> expInput = new ArrayList<>();
    CsdlParameter param = new CsdlParameter();
    param.setName("Number");
    param.setType(EdmPrimitiveTypeKind.Decimal.getFullQualifiedName());
    param.setNullable(false);
    param.setPrecision(32);
    param.setScale(0);
    expInput.add(param);
    AssertCollection.assertListEquals(expInput, func.getEdmItem().getParameters(), CsdlParameter.class);
  }

  @Test
  public void checkByEntityAnnotationInputParameterIsEnumeration() throws ODataJPAModelException {

    IntermediateFunction func = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getStoredProcedure(helper.getEntityType(Person.class), "CheckRights"), BusinessPartner.class, helper.schema);

    assertNotNull(func.getEdmItem().getParameters());
    assertEquals(2, func.getEdmItem().getParameters().size());
    assertEquals(PUNIT_NAME + ".AccessRights", func.getEdmItem().getParameters().get(0).getTypeFQN()
        .getFullQualifiedNameAsString());
    assertEquals("Edm.Int32", func.getEdmItem().getParameters().get(1).getTypeFQN()
        .getFullQualifiedNameAsString());
  }

  @Test
  public void checkByEntityAnnotationResultParameterIsEmpty() throws ODataJPAModelException {
    IntermediateFunction func = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getStoredProcedure(
            helper.getEntityType(BusinessPartner.class), "CountRoles"), BusinessPartner.class, helper.schema);

    assertEquals(PUNIT_NAME + ".BusinessPartner", func.getEdmItem().getReturnType().getType());
  }

  @Test
  public void checkByEntityAnnotationIsBound() throws ODataJPAModelException {
    IntermediateFunction func = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getStoredProcedure(
            helper.getEntityType(BusinessPartner.class), "CountRoles"), BusinessPartner.class, helper.schema);

    assertTrue(func.getEdmItem().isBound());
    assertTrue(func.isBound());
    assertEquals(PUNIT_NAME + ".BusinessPartner", func.getEdmItem().getParameters().get(0).getTypeFQN()
        .getFullQualifiedNameAsString());
  }

  @Test
  public void checkByEntityAnnotationResultParameterSimple() throws ODataJPAModelException {
    IntermediateFunction func = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getStoredProcedure(
            helper.getEntityType(BusinessPartner.class), "IsPrime"), BusinessPartner.class, helper.schema);

    assertEquals(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName().getFullQualifiedNameAsString(), func.getEdmItem()
        .getReturnType().getType());
  }

  @Test
  public void checkByEntityAnnotationResultParameterIsEntity() throws ODataJPAModelException {
    IntermediateFunction func = new IntermediateFunctionFactory().create(new JPADefaultEdmNameBuilder(
        PUNIT_NAME), helper.getEntityType(Organization.class), helper.schema).get("AllCustomersByABC");
    assertEquals(PUNIT_NAME + ".Organization", func.getEdmItem().getReturnType().getType());
  }

  @Test
  public void checkByEntityAnnotationResultParameterIsCollectionFalse() throws ODataJPAModelException {
    IntermediateFunction func = new IntermediateFunctionFactory().create(new JPADefaultEdmNameBuilder(
        PUNIT_NAME), helper.getEntityType(Organization.class), helper.schema).get("AllCustomersByABC");
    assertTrue(func.getEdmItem().getReturnType().isCollection());

    func = new IntermediateFunctionFactory().create(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getEntityType(BusinessPartner.class), helper.schema).get("IsPrime");
    assertFalse(func.getEdmItem().getReturnType().isCollection());
  }

  @Test
  public void checkByEntityAnnotationResultParameterNotGiven() throws ODataJPAModelException {
    IntermediateFunction func = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getStoredProcedure(
            helper.getEntityType(BusinessPartner.class), "CountRoles"), BusinessPartner.class, helper.schema);

    assertTrue(func.getEdmItem().getReturnType().isCollection());
    assertEquals(PUNIT_NAME + ".BusinessPartner", func.getEdmItem().getReturnType().getType());
    assertEquals(BusinessPartner.class, func.getResultParameter().getType());
  }

  @Test
  public void checkByEntityAnnotationResultParameterIsNullable() throws ODataJPAModelException {
    IntermediateFunction func = new IntermediateFunctionFactory().create(new JPADefaultEdmNameBuilder(
        PUNIT_NAME), helper.getEntityType(Organization.class), helper.schema).get("AllCustomersByABC");
    assertTrue(func.getEdmItem().getReturnType().isNullable());

    func = new IntermediateFunctionFactory().create(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getEntityType(BusinessPartner.class), helper.schema).get("IsPrime");
    assertFalse(func.getEdmItem().getReturnType().isNullable());
  }

  @Test
  public void checkByEntityAnnotationResultParameterEnumerationType() throws ODataJPAModelException {

    IntermediateFunction func = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper
        .getStoredProcedure(helper.getEntityType(Person.class), "ReturnRights"), BusinessPartner.class, helper.schema);

    assertNotNull(func.getEdmItem().getReturnType());
    assertEquals(PUNIT_NAME + ".AccessRights", func.getEdmItem().getReturnType().getTypeFQN()
        .getFullQualifiedNameAsString());
  }

  @Test
  public void checkReturnTypeEmbedded() throws ODataJPAModelException {
    EdmFunction func = mock(EdmFunction.class);
    EdmFunction.ReturnType retType = mock(EdmFunction.ReturnType.class);
    // EdmFunctionParameter[] params = new EdmFunctionParameter[0];
    when(func.returnType()).thenReturn(retType);
    when(func.parameter()).thenReturn(new EdmParameter[0]);
    when(retType.type()).thenAnswer((Answer<Class<?>>) invocation -> ChangeInformation.class);

    IntermediateFunction act = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), func,
        BusinessPartner.class, helper.schema);
    assertEquals("nl.buildforce.sequoia.jpa.ChangeInformation", act.getEdmItem().getReturnType().getTypeFQN()
        .getFullQualifiedNameAsString());
  }

  @Test
  public void checkReturnTypeUnknown() {
    EdmFunction func = mock(EdmFunction.class);
    EdmFunction.ReturnType retType = mock(EdmFunction.ReturnType.class);
    // EdmFunctionParameter[] params = new EdmFunctionParameter[0];
    when(func.returnType()).thenReturn(retType);
    when(func.parameter()).thenReturn(new EdmParameter[0]);
    when(retType.type()).thenAnswer((Answer<Class<?>>) invocation -> DateConverter.class);
    IntermediateFunction act;
    try {
      act = new IntermediateDataBaseFunction(new JPADefaultEdmNameBuilder(PUNIT_NAME), func, BusinessPartner.class,
          helper.schema);
      act.getEdmItem();
    } catch (ODataJPAModelException e) {
      assertEquals(ODataJPAModelException.MessageKeys.FUNC_RETURN_TYPE_UNKNOWN.getKey(), e.getId());
      return;
    }
    fail();
  }

}