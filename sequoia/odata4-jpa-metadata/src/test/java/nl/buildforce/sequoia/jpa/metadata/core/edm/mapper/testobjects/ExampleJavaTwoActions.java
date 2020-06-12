package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmAction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.ODataAction;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Person;

import java.math.BigDecimal;

public class ExampleJavaTwoActions implements ODataAction {

  @EdmAction(name = "Test")
  public void unbound(
      @EdmParameter(name = "Person") Person person,
      @EdmParameter(name = "A", precision = 34, scale = 10) BigDecimal a) {
    // Do nothing
  }

  @EdmAction(isBound = true)
  public void bound(
      @EdmParameter(name = "Person") Person person,
      @EdmParameter(name = "A", precision = 34, scale = 10) BigDecimal a) {
    // Do nothing
  }
}