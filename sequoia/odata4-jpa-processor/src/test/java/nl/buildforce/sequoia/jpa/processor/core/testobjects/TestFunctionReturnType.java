package nl.buildforce.sequoia.jpa.processor.core.testobjects;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction.ReturnType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.ODataFunction;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeDivision;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AdministrativeInformation;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.ChangeInformation;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.CollectionDeep;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.CollectionFirstLevelComplex;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.CollectionSecondLevelComplex;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.CommunicationData;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.InhouseAddress;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TestFunctionReturnType implements ODataFunction {

  @EdmFunction(name = "PrimitiveValue", returnType = @ReturnType)
  public Integer primitiveValue(@EdmParameter(name = "A") short a) {
    if (a == 0)
      return null;
    return (int) a;
  }

  @EdmFunction(name = "ListOfPrimitiveValues", returnType = @ReturnType(type = Integer.class))
  public List<Integer> listOfPrimitiveValues(@EdmParameter(name = "A") Integer a) {
    return Arrays.asList(a, a / 2);
  }

  @EdmFunction(name = "ComplexType", returnType = @ReturnType)
  public CommunicationData complexType(@EdmParameter(name = "A") int a) {
    if (a == 0)
      return null;
    CommunicationData result = new CommunicationData();
    result.setLandlinePhoneNumber(Integer.valueOf(a).toString());
    return result;
  }

  @EdmFunction(name = "ListOfComplexType", returnType = @ReturnType(type = AdministrativeInformation.class))
  public List<AdministrativeInformation> listOfComplexType(@EdmParameter(name = "A") String user) {
    long milliPerDay = 24 * 60 * 60 * 1000;
    AdministrativeInformation admin1 = new AdministrativeInformation();
    admin1.setCreated(new ChangeInformation(user, new Date(LocalDate.now().toEpochDay() * milliPerDay)));
    AdministrativeInformation admin2 = new AdministrativeInformation();
    admin2.setUpdated(new ChangeInformation(user, new Date(LocalDate.now().toEpochDay() * milliPerDay)));
    return Arrays.asList(admin1, admin2);
  }

  @EdmFunction(name = "EntityType", returnType = @ReturnType)
  public AdministrativeDivision entityType(@EdmParameter(name = "A") int a) {

    if (a == 0)
      return null;
    AdministrativeDivision result = new AdministrativeDivision();
    result.setArea(a);
    result.setCodePublisher("1");
    result.setCodeID("2");
    result.setDivisionCode("3");
    return result;
  }

  @EdmFunction(name = "ListOfEntityType", returnType = @ReturnType(type = AdministrativeDivision.class))
  public List<AdministrativeDivision> listOfEntityType(@EdmParameter(name = "A") Integer a) {
    return Arrays.asList(entityType(a), entityType(a / 2));
  }

  @EdmFunction(name = "ConvertBirthday", returnType = @ReturnType)
  public Person convertBirthday() {
    Person p = new Person();
    p.setID("1");
    p.setBirthDay(LocalDate.now());
    p.setInhouseAddress(new ArrayList<>());
    return p;
  }

  @EdmFunction(name = "ListOfEntityTypeWithCollection", returnType = @ReturnType(type = Person.class))
  public List<Person> listOfEntityTypeWithCollection(@EdmParameter(name = "A") Integer a) {
    Person person = new Person();
    person.setID("1");
    person.addInhouseAddress(new InhouseAddress("DEV", "7"));
    person.addInhouseAddress(new InhouseAddress("ADMIN", "2"));
    return Collections.singletonList(person);
  }

  @EdmFunction(name = "EntityTypeWithDeepCollection", returnType = @ReturnType(type = CollectionDeep.class))
  public CollectionDeep entityTypeWithDeepCollection(@EdmParameter(name = "A") Integer a) {
    final CollectionDeep deepCollection = new CollectionDeep();
    final CollectionFirstLevelComplex firstLevel = new CollectionFirstLevelComplex();
    final CollectionSecondLevelComplex secondLevel = new CollectionSecondLevelComplex();
    deepCollection.setFirstLevel(firstLevel);
    deepCollection.setID("1");
    firstLevel.setLevelID(10);
    firstLevel.setSecondLevel(secondLevel);
    secondLevel.setNumber(5L);

    secondLevel.addInhouseAddress(new InhouseAddress("DEV", "7"));
    secondLevel.addInhouseAddress(new InhouseAddress("ADMIN", "2"));
    secondLevel.setComment(Arrays.asList("One", "Two", "Three"));
    return deepCollection;
  }
}