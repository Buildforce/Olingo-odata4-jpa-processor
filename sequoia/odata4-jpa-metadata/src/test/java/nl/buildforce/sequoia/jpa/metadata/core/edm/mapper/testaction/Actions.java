package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testaction;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmAction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.ODataAction;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.Person;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;

public class Actions implements ODataAction {
  public Actions(final EntityManager em) {
  }

  @EdmAction(name = "BoundNoImport", isBound = true)
  public void boundNoImport(
      @EdmParameter(name = "Person") Person person,
      @EdmParameter(name = "A", precision = 34, scale = 10) BigDecimal a) {
    // Do nothing
  }

}