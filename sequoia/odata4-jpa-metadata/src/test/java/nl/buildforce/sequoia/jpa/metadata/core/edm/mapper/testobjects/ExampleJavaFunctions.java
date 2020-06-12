package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction.ReturnType;
// import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmGeospatial;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.ODataFunction;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AbcClassification;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.AccessRights;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.ChangeInformation;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Person;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.PostalAddressData;
// import org.apache.olingo.commons.api.edm.geo.Geospatial.Dimension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExampleJavaFunctions implements ODataFunction {

  @EdmFunction(name = "Add", returnType = @ReturnType)
  public Integer sum(
      @EdmParameter(name = "A") short a, @EdmParameter(name = "B") int b) {
    return a + b;
  }

  @EdmFunction(returnType = @ReturnType,
      parameter = {
          @EdmParameter(name = "Dmmy", parameterName = "A",
              type = String.class, maxLength = 10) })
  public Integer div(
      @EdmParameter(name = "A") short a, @EdmParameter(name = "B") int b) {
    return a / b;
  }

  @EdmFunction(returnType = @ReturnType(type = Double.class))
  public Integer errorReturnType(
      @EdmParameter(name = "A") short a, @EdmParameter(name = "B") int b) {
    return a + b;
  }

  @EdmFunction(returnType = @ReturnType(isNullable = false, precision = 9, scale = 3))
  public Timestamp now() {
    return Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));
  }

 /* @EdmFunction(returnType = @ReturnType(maxLength = 60, srid = @EdmGeospatial(
      dimension = Dimension.GEOGRAPHY, srid = "4326")))
  public String determineLocation() {
    return "";
  }*/

  @EdmFunction(returnType = @ReturnType(type = String.class))
  public List<String> returnCollection() {
    return new ArrayList<>();
  }

  @EdmFunction(returnType = @ReturnType())
  public List<String> returnCollectionWithoutReturnType() {
    return new ArrayList<>();
  }

  @EdmFunction(returnType = @ReturnType())
  public ChangeInformation returnEmbeddable() {
    return new ChangeInformation();
  }

  @EdmFunction(returnType = @ReturnType(type = ChangeInformation.class))
  public List<ChangeInformation> returnEmbeddableCollection() {
    return Collections.singletonList(new ChangeInformation());
  }

  @EdmFunction(returnType = @ReturnType())
  public Person returnEntity() {
    return new Person();
  }

  @EdmFunction(returnType = @ReturnType())
  public ExampleJavaOneFunction wrongReturnType() {
    return new ExampleJavaOneFunction();
  }

  @EdmFunction(returnType = @ReturnType())
  public Integer errorNonPrimitiveParameter(
      @EdmParameter(name = "A") PostalAddressData a) {
    return 1;
  }

  @EdmFunction(returnType = @ReturnType())
  public AbcClassification returnEnumerationType(@EdmParameter(name = "Rights") AccessRights rights) {
    return AbcClassification.A;
  }

  @EdmFunction(returnType = @ReturnType(type = AbcClassification.class))
  public List<AbcClassification> returnEnumerationCollection() {
    return new ArrayList<>();
  }
}