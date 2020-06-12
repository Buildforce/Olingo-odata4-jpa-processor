package nl.buildforce.sequoia.jpa.processor.core.testobjects;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction.ReturnType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.ODataFunction;

import jakarta.persistence.EntityManager;

public class TestFunctionParameter implements ODataFunction {
  public static int calls;
  public static EntityManager em;
  public static int param1;
  public static int param2;

  public TestFunctionParameter(EntityManager em) {
    TestFunctionParameter.em = em;
  }

  @EdmFunction(returnType = @ReturnType)
  public Integer sum(@EdmParameter(name = "A") Integer a, @EdmParameter(name = "B") Integer b) {
    calls += 1;
    param1 = a;
    param2 = b;
    return a + b;
  }

}