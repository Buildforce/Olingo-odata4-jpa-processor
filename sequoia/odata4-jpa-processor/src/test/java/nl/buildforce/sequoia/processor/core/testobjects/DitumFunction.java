package nl.buildforce.sequoia.processor.core.testobjects;

import jakarta.persistence.EntityManager;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction.ReturnType;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.metadata.core.edm.mapper.extension.ODataFunction;

public class DitumFunction implements ODataFunction {
  public static EntityManager em;

  public DitumFunction(EntityManager em) {
    DitumFunction.em = em;
  }

  @EdmFunction(name = "DitumFunction", returnType = @ReturnType)
  public Integer calculateSum(@EdmParameter(name = "A") short a, @EdmParameter(name = "B") short b) {
    if (a == 0 || b == 0)
      return null;
    return a + b;
  }

}
