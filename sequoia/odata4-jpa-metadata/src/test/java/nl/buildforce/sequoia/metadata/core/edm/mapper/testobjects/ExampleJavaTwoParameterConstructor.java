package nl.buildforce.sequoia.metadata.core.edm.mapper.testobjects;

import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmAction;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction.ReturnType;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.ODataAction;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.ODataFunction;

import jakarta.persistence.EntityManager;

public class ExampleJavaTwoParameterConstructor implements ODataFunction, ODataAction {

  public ExampleJavaTwoParameterConstructor(EntityManager em, Integer a) {
  }

  @EdmFunction(returnType = @ReturnType)
  public Integer sum(
      @EdmParameter(name = "A") short a, @EdmParameter(name = "B") int b) {
    return a + b;
  }

  @EdmAction()
  public void mul(
      @EdmParameter(name = "A") short a, @EdmParameter(name = "B") int b) {}

}