package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public interface JPAJavaOperation {
  /**
   * @return The Method that implements a function
   */
  Method getMethod();

  /**
   *

   * @return The constructor to be used to create a new instance
   */
  Constructor<?> getConstructor();
}