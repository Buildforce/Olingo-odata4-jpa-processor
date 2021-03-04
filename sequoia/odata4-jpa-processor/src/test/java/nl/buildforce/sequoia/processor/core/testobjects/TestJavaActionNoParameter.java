package nl.buildforce.sequoia.processor.core.testobjects;

import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmAction;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction.ReturnType;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmParameter;
import nl.buildforce.sequoia.processor.core.testmodel.AdministrativeDivision;

import java.math.BigDecimal;

public class TestJavaActionNoParameter {
  public static int constructorCalls;
  public static Short param1;
  public static Integer param2;
  public static FileAccess enumeration;
  public static AdministrativeDivision bindingParam;

  public TestJavaActionNoParameter() {
    constructorCalls++;
  }

  public static void resetCalls() {
    constructorCalls = 0;
  }

  @EdmAction(returnType = @ReturnType(isNullable = false, precision = 20, scale = 5))
  public BigDecimal unboundReturnPrimitiveNoParameter() {
    return new BigDecimal(7);
  }

  @EdmAction()
  public void unboundVoidOneParameter(@EdmParameter(name = "A") Short a) {
    param1 = a;
  }

  @EdmAction()
  public void unboundVoidTwoParameter(@EdmParameter(name = "A") Short a, @EdmParameter(name = "B") Integer b) {
    param1 = a;
    param2 = b;
  }

  @EdmAction(isBound = true)
  public void boundOnlyBinding(@EdmParameter(name = "Root") AdministrativeDivision root) {
    bindingParam = root;
  }

  @EdmAction(isBound = true)
  public void boundBindingPlus(@EdmParameter(name = "Root") AdministrativeDivision root, @EdmParameter(
      name = "A") Short a, @EdmParameter(name = "B") Integer b) {
    bindingParam = root;
    param1 = a;
    param2 = b;
  }

  @EdmAction()
  public void unboundVoidOneEnumerationParameter(@EdmParameter(name = "AccessRights") FileAccess a) {
    enumeration = a;
  }

}