package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction.ReturnType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.ODataFunction;

public class ExampleJavaOneFunction implements ODataFunction {

  @EdmFunction(returnType = @ReturnType)
  public Integer sum(
      @EdmParameter(name = "A") short a, @EdmParameter(name = "B") int b) {
    return a + b;
  }

}