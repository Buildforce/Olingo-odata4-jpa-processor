package nl.buildforce.sequoia.jpa.processor.core.modify;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.converter.JPATupleChildConverter;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivision;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivisionDescription;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivisionDescriptionKey;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartner;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartnerRole;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.CollectionInnerComplex;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.CollectionNestedComplex;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Collection;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.CollectionDeep;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.CollectionFirstLevelComplex;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.CollectionPartOfComplex;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.CollectionSecondLevelComplex;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.InhouseAddress;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Organization;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Person;
import nl.buildforce.sequoia.jpa.processor.core.util.ServiceMetadataDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.TestHelper;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TestJPAEntityResult extends TestJPACreateResult {
  @BeforeEach
  public void setUp() throws Exception {
    headers = new HashMap<>();
    jpaEntity = new Organization();
    helper = new TestHelper(emf, PUNIT_NAME);
    et = helper.getJPAEntityType("Organizations");
    converter = new JPATupleChildConverter(helper.sd, OData.newInstance()
        .createUriHelper(), new ServiceMetadataDouble(nameBuilder, "Organizations"));
  }

  @Override
  protected void createCutProvidesEmptyMap() throws ODataJPAModelException, ODataApplicationException {
    cut = new JPAEntityResult(et, jpaEntity, headers, converter);
  }

  @Override
  protected void createCutGetResultSimpleEntity() throws ODataJPAModelException, ODataApplicationException {
    jpaEntity = new BusinessPartnerRole();
    ((BusinessPartnerRole) jpaEntity).setBusinessPartnerID("34");
    ((BusinessPartnerRole) jpaEntity).setRoleCategory("A");
    cut = new JPAEntityResult(et, jpaEntity, headers, converter);
  }

  @Override
  protected void createCutGetResultWithOneLevelEmbedded() throws ODataJPAModelException, ODataApplicationException {
    AdministrativeDivisionDescriptionKey key = new AdministrativeDivisionDescriptionKey();
    key.setCodeID("A");
    key.setLanguage("en");
    jpaEntity = new AdministrativeDivisionDescription();
    ((AdministrativeDivisionDescription) jpaEntity).setName("Hugo");
    ((AdministrativeDivisionDescription) jpaEntity).setKey(key);

    cut = new JPAEntityResult(et, jpaEntity, headers, converter);
  }

  @Override
  protected void createCutGetResultWithTwoLevelEmbedded() throws ODataJPAModelException,
      ODataApplicationException {

    jpaEntity = new Organization();
    ((Organization) jpaEntity).onCreate();
    ((Organization) jpaEntity).setID("01");
    ((Organization) jpaEntity).setCustomString1("Dummy");

    cut = new JPAEntityResult(et, jpaEntity, headers, converter);
  }

  @Override
  protected void createCutGetResultWithDescriptionProperty() throws ODataJPAModelException, ODataApplicationException {
    et = helper.getJPAEntityType("Organizations");
    jpaEntity = new Organization();

    final AdministrativeDivisionDescription description = new AdministrativeDivisionDescription();
    description.setKey(new AdministrativeDivisionDescriptionKey("ISO", "3166", "DEU", "en"));
    description.setName("MyDivision");
    ((BusinessPartner) jpaEntity).setLocationName(Collections.singletonList(description));
    ((BusinessPartner) jpaEntity).setID("Willi");
    ((BusinessPartner) jpaEntity).setETag(7L);
    cut = new JPAEntityResult(et, jpaEntity, headers, converter);
  }

  @Override
  protected void createCutGetResultWithWithOneLinked() throws ODataJPAModelException, ODataApplicationException {
    et = helper.getJPAEntityType("AdministrativeDivisions");
    jpaEntity = new AdministrativeDivision();
    AdministrativeDivision child = new AdministrativeDivision();
    List<AdministrativeDivision> children = new ArrayList<>();
    children.add(child);
    ((AdministrativeDivision) jpaEntity).setChildren(children);

    child.setCodeID("NUTS2");
    child.setDivisionCode("BE21");
    child.setCodePublisher("Eurostat");

    ((AdministrativeDivision) jpaEntity).setCodeID("NUTS1");
    ((AdministrativeDivision) jpaEntity).setDivisionCode("BE2");
    ((AdministrativeDivision) jpaEntity).setCodePublisher("Eurostat");

    cut = new JPAEntityResult(et, jpaEntity, headers, converter);
  }

  @Override
  protected void createCutGetResultWithWithTwoLinked() throws ODataJPAModelException, ODataApplicationException {
    createCutGetResultWithWithOneLinked();

    AdministrativeDivision child = new AdministrativeDivision();
    List<AdministrativeDivision> children = ((AdministrativeDivision) jpaEntity).getChildren();
    children.add(child);

    child.setCodeID("NUTS2");
    child.setDivisionCode("BE22");
    child.setCodePublisher("Eurostat");

    cut = new JPAEntityResult(et, jpaEntity, headers, converter);

  }

  @Override
  protected void createCutGetResultEntityWithSimpleCollection() throws ODataJPAModelException,
      ODataApplicationException {

    final Organization org = new Organization();
    final List<String> comment = org.getComment();
    comment.add("First");
    comment.add("Second");
    org.setID("1");
    jpaEntity = org;

    cut = new JPAEntityResult(et, jpaEntity, headers, converter);
  }

  @Override
  protected void createCutGetResultEntityWithComplexCollection() throws ODataJPAModelException,
      ODataApplicationException {
    et = helper.getJPAEntityType("Persons");

    final Person person = new Person();
    final List<InhouseAddress> addresses = person.getInhouseAddress();
    InhouseAddress addr = new InhouseAddress();
    addr.setBuilding("A");
    addr.setTaskID("DEV");
    addresses.add(addr);
    addr = new InhouseAddress();
    addr.setBuilding("C");
    addr.setTaskID("MAIN");
    addresses.add(addr);

    jpaEntity = person;

    cut = new JPAEntityResult(et, jpaEntity, headers, converter);
  }

  @Override
  protected void createCutGetResultEntityWithComplexWithCollection() throws ODataJPAModelException,
      ODataApplicationException {
    et = helper.getJPAEntityType("Collections");

    final Collection collection = new Collection();
    final CollectionPartOfComplex complex = new CollectionPartOfComplex();
    final List<InhouseAddress> addresses = complex.getAddress();
    complex.setNumber(2L);
    collection.setComplex(complex);

    InhouseAddress addr = new InhouseAddress();
    addr.setBuilding("A");
    addr.setTaskID("DEV");
    addresses.add(addr);
    addr = new InhouseAddress();
    addr.setBuilding("C");
    addr.setTaskID("MAIN");
    addresses.add(addr);

    jpaEntity = collection;
    cut = new JPAEntityResult(et, jpaEntity, headers, converter);
  }

  @Override
  protected void createCutGetResultEntityWithNestedComplexCollection() throws ODataJPAModelException,
      ODataApplicationException {

    et = helper.getJPAEntityType("Collections");

    final Collection collection = new Collection();
    final List<CollectionNestedComplex> nested = new ArrayList<>();
    collection.setNested(nested);

    CollectionNestedComplex nestedItem = new CollectionNestedComplex();
    CollectionInnerComplex inner = new CollectionInnerComplex();
    inner.setFigure1(1L);
    inner.setFigure3(3L);
    nestedItem.setInner(inner);
    nestedItem.setNumber(100L);
    nested.add(nestedItem);

    nestedItem = new CollectionNestedComplex();
    inner = new CollectionInnerComplex();
    inner.setFigure1(11L);
    inner.setFigure3(13L);
    nestedItem.setInner(inner);
    nestedItem.setNumber(200L);
    nested.add(nestedItem);
    jpaEntity = collection;
    cut = new JPAEntityResult(et, jpaEntity, headers, converter);

  }

  @Override
  protected void createCutGetResultEntityWithDeepComplexWithCollection() throws ODataJPAModelException,
      ODataApplicationException {
    et = helper.getJPAEntityType("CollectionDeeps");

    final CollectionDeep collection = new CollectionDeep();
    final CollectionFirstLevelComplex firstLevel = new CollectionFirstLevelComplex();
    final CollectionSecondLevelComplex secondLevel = new CollectionSecondLevelComplex();

    final List<InhouseAddress> addresses = secondLevel.getAddress();
    collection.setID("27");
    collection.setFirstLevel(firstLevel);
    firstLevel.setLevelID(3);
    firstLevel.setSecondLevel(secondLevel);

    InhouseAddress addr = new InhouseAddress();
    addr.setBuilding("A");
    addr.setTaskID("DEV");
    addresses.add(addr);
    addr = new InhouseAddress();
    addr.setBuilding("C");
    addr.setTaskID("MAIN");
    addresses.add(addr);

    jpaEntity = collection;
    cut = new JPAEntityResult(et, jpaEntity, headers, converter);

  }

}