package nl.buildforce.sequoia.jpa.processor.core.testobjects;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmAction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction.ReturnType;
// import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmGeospatial;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.ODataAction;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartnerRole;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.ChangeInformation;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Person;
// import org.apache.olingo.commons.api.edm.geo.Geospatial.Dimension;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestJavaActions implements ODataAction {

  public static int constructorCalls = 0;

  public TestJavaActions(EntityManager em) {
    assert em != null;
    constructorCalls++;
  }

  @EdmAction()
  public Integer unboundWithImport(
      @EdmParameter(name = "A") short a, @EdmParameter(name = "B") int b) {
    return a + b;
  }

  @EdmAction(name = "BoundNoImport", isBound = true)
  public void boundNoImport(
      @EdmParameter(name = "Person") Person person,
      @EdmParameter(name = "A", precision = 34, scale = 10) BigDecimal a) {
    // Do nothing
  }

  @EdmAction(returnType = @ReturnType(isNullable = false, precision = 20, scale = 5))
  public BigDecimal unboundReturnFacetNoParameter() {
    return new BigDecimal(7);
  }

  @EdmAction(returnType = @ReturnType(isNullable = false, precision = 20, scale = 5))
  public BigDecimal unboundReturnFacet(
      @EdmParameter(name = "A") short a, @EdmParameter(name = "B") int b) {
    return new BigDecimal(a).add(new BigDecimal(b));
  }

  @EdmAction(isBound = true, entitySetPath = "Person/Roles")
  public BusinessPartnerRole boundWithEntitySetPath(
      @EdmParameter(name = "Person") Person person) {
    return null;
  }

  @EdmAction()
  public ChangeInformation returnEmbeddable() {
    return new ChangeInformation();
  }

  @EdmAction()
  public Person returnEntity() {
    return new Person();
  }

  @EdmAction(returnType = @ReturnType(type = String.class))
  public List<String> returnCollection() {
    return new ArrayList<>();
  }

  @EdmAction(returnType = @ReturnType(type = ChangeInformation.class))
  public List<ChangeInformation> returnEmbeddableCollection() {
    return Collections.singletonList(new ChangeInformation());
  }

  /*@EdmAction(returnType = @ReturnType(maxLength = 60,
      srid = @EdmGeospatial(dimension = Dimension.GEOGRAPHY, srid = "4326")))
  public String calculateLocation(
      @EdmParameter(name = "String", maxLength = 100,
          srid = @EdmGeospatial(dimension = Dimension.GEOGRAPHY, srid = "4326")) String a) {
    return "";
  }*/

  @EdmAction()
  public void unboundWithOutParameter() {
    // Do nothing
  }

}