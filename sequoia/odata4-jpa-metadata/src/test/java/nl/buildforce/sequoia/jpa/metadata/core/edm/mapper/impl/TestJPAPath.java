package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAElement;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartnerWithGroups;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Organization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException.MessageKeys.NOT_SUPPORTED_MIXED_PART_OF_GROUP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestJPAPath extends TestMappingRoot {
  private JPAEntityType organization;
  private JPAEntityType bupaWithGroup;

  @BeforeEach
  public void setup() throws ODataJPAModelException {
    TestHelper helper = new TestHelper(emf.getMetamodel(), PUNIT_NAME);
    organization =
    new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper.getEntityType(Organization.class), helper.schema);
    bupaWithGroup =
      new IntermediateEntityType(new JPADefaultEdmNameBuilder(PUNIT_NAME), helper.getEntityType(BusinessPartnerWithGroups.class), helper.schema);
  }

  @Test
  public void checkOnePathElementAlias() throws ODataJPAModelException {
    JPAPath cut = organization.getPath("Name1");
    assertEquals("Name1", cut.getAlias());
  }

  @Test
  public void checkOnePathElementPathSize() throws ODataJPAModelException {
    JPAPath cut = organization.getPath("Name1");
    assertEquals(1, cut.getPath().size());
  }

  @Test
  public void checkOnePathElementElement() throws ODataJPAModelException {
    JPAPath cut = organization.getPath("Name1");
    assertEquals("name1", cut.getPath().get(0).getInternalName());
  }

  @Test
  public void checkOnePathElementFromSuperTypeAlias() throws ODataJPAModelException {
    JPAPath cut = organization.getPath("Type");
    assertEquals("Type", cut.getAlias());
  }

  @Test
  public void checkTwoPathElementAlias() throws ODataJPAModelException {
    JPAPath cut = organization.getPath("Address/Country");
    assertEquals("Address/Country", cut.getAlias());
  }

  @Test
  public void checkTwoPathElementPathSize() throws ODataJPAModelException {
    JPAPath cut = organization.getPath("Address/Country");
    assertEquals(2, cut.getPath().size());
  }

  @Test
  public void checkTwoPathElementPathElements() throws ODataJPAModelException {
    JPAPath cut = organization.getPath("Address/Country");
    assertEquals("address", cut.getPath().get(0).getInternalName());
    assertEquals("country", cut.getPath().get(1).getInternalName());
  }

  @Test
  public void checkThreePathElementAlias() throws ODataJPAModelException {
    JPAPath cut = organization.getPath("AdministrativeInformation/Created/By");
    assertEquals("AdministrativeInformation/Created/By", cut.getAlias());
  }

  @Test
  public void checkThreePathElementPathSize() throws ODataJPAModelException {
    JPAPath cut = organization.getPath("AdministrativeInformation/Created/By");
    assertEquals(3, cut.getPath().size());
  }

  @Test
  public void checkThreePathElementPathElements() throws ODataJPAModelException {
    JPAPath cut = organization.getPath("AdministrativeInformation/Created/By");
    assertEquals("administrativeInformation", cut.getPath().get(0).getInternalName());
    assertEquals("created", cut.getPath().get(1).getInternalName());
    assertEquals("by", cut.getPath().get(2).getInternalName());
  }

  @Test
  public void checkIsPartOfGroupReturnsTrueOnNotAnnotated() throws ODataJPAModelException {

    final JPAPath act = bupaWithGroup.getPath("Type");
    assertTrue(act.isPartOfGroups(Collections.singletonList("Test")));
  }

  @Test
  public void checkIsPartOfGroupReturnsTrueOnAnnotatedBelongsToIt() throws ODataJPAModelException {

    final JPAPath act = bupaWithGroup.getPath("Country");
    assertTrue(act.isPartOfGroups(Collections.singletonList("Person")));
  }

  @Test
  public void checkIsPartOfGroupCheckTwice() throws ODataJPAModelException {

    final JPAPath act = bupaWithGroup.getPath("Country");
    assertTrue(act.isPartOfGroups(Collections.singletonList("Person")));
    assertTrue(act.isPartOfGroups(Collections.singletonList("Person")));
  }

  @Test
  public void checkIsPartOfGroupReturnsFalseOnAnnotatedDoesNotBelogsToIt() throws ODataJPAModelException {

    final JPAPath act = bupaWithGroup.getPath("Country");
    assertFalse(act.isPartOfGroups(Collections.singletonList("Test")));
  }

  @Test
  public void checkIsPartOfGroupReturnsFalseOnAnnotatedComplex() throws ODataJPAModelException {

    final JPAPath act = bupaWithGroup.getPath("CommunicationData/Email");
    assertFalse(act.isPartOfGroups(Collections.singletonList("Test")));

  }

  @Test
  public void checkIsPartOfGroupReturnsTrueOnNotAnnotatedComplex() throws ODataJPAModelException {

    final JPAPath act = organization.getPath("CommunicationData/Email");
    assertTrue(act.isPartOfGroups(Collections.singletonList("Test")));
  }

  @Test
  public void checkThrowsExceptionOnInconsistentGroups1() {
    final List<JPAElement> attributes = new ArrayList<>(2);

    final IntermediateProperty complex = mock(IntermediateProperty.class);
    when(complex.isPartOfGroup()).thenReturn(true);
    when(complex.getGroups()).thenReturn(Arrays.asList("Test", "Dummy"));
    attributes.add(complex);

    final IntermediateProperty primitive = mock(IntermediateProperty.class);
    when(primitive.isPartOfGroup()).thenReturn(true);
    when(primitive.getGroups()).thenReturn(Collections.singletonList("Dummy"));
    attributes.add(primitive);

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new JPAPathImpl("Communication/Email", "Telecom.Email", attributes));

    assertEquals(NOT_SUPPORTED_MIXED_PART_OF_GROUP.getKey(), act.getId());
    assertFalse(act.getMessage().isEmpty());
  }

  @Test
  public void checkThrowsExceptionOnInconsistentGroups2() {
    final List<JPAElement> attributes = new ArrayList<>(2);

    final IntermediateProperty complex = mock(IntermediateProperty.class);
    when(complex.isPartOfGroup()).thenReturn(true);
    when(complex.getGroups()).thenReturn(Arrays.asList("Test", "Dummy"));
    attributes.add(complex);

    final IntermediateProperty primitive = mock(IntermediateProperty.class);
    when(primitive.isPartOfGroup()).thenReturn(true);
    when(primitive.getGroups()).thenReturn(Arrays.asList("Dummy", "Willi"));
    attributes.add(primitive);

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new JPAPathImpl("Communication/Email", "Telecom.Email", attributes));

    assertEquals(NOT_SUPPORTED_MIXED_PART_OF_GROUP.getKey(), act.getId());
    assertFalse(act.getMessage().isEmpty());
  }

  @Test
  public void checkThrowsExceptionOnInconsistentGroups3() {
    final List<JPAElement> attributes = new ArrayList<>(2);

    final IntermediateProperty complex = mock(IntermediateProperty.class);
    when(complex.isPartOfGroup()).thenReturn(true);
    when(complex.getGroups()).thenReturn(Arrays.asList("Test", "Dummy"));
    attributes.add(complex);

    final IntermediateProperty primitive = mock(IntermediateProperty.class);
    when(primitive.isPartOfGroup()).thenReturn(true);
    when(primitive.getGroups()).thenReturn(Arrays.asList("Dummy", "Test", "Willi"));
    attributes.add(primitive);

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new JPAPathImpl("Communication/Email", "Telecom.Email", attributes));

    assertEquals(NOT_SUPPORTED_MIXED_PART_OF_GROUP.getKey(), act.getId());
    assertFalse(act.getMessage().isEmpty());
  }

  @Test
  public void checkTwoNotEqualIfAliasNotEqual() throws ODataJPAModelException {
    final JPAPath cut = organization.getPath("Address/Country");
    final JPAPath act = new JPAPathImpl("Address", cut.getDBFieldName(), cut.getPath());
    assertNotEquals(act, cut);
  }

  @Test
  public void checkTwoNotEqualIfElementListNotEqual() throws ODataJPAModelException {
    final JPAPath cut = organization.getPath("Address/Country");
    final List<JPAElement> pathList = new ArrayList<>(cut.getPath());
    pathList.remove(0);
    final JPAPath act = new JPAPathImpl("Address/Country", cut.getDBFieldName(), pathList);
    assertNotEquals(act, cut);
  }

  @Test
  public void checkTwoEqualIfSame() throws ODataJPAModelException {
    final JPAPath cut = organization.getPath("Address/Country");
    assertEquals(cut, cut);
  }
}