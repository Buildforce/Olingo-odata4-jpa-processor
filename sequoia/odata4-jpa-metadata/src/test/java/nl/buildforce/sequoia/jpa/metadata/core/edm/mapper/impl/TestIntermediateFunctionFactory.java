package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.extension.ODataFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects.ExampleJavaOneFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.testobjects.ExampleJavaTwoFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestIntermediateFunctionFactory extends TestMappingRoot {
  private TestHelper helper;

  private Reflections reflections;
  private IntermediateFunctionFactory cut;
  private Set<Class<? extends ODataFunction>> javaFunctions;

  @BeforeEach
  public void setUp() throws ODataJPAModelException {
    helper = new TestHelper(emf.getMetamodel(), PUNIT_NAME);

    reflections = mock(Reflections.class);
    cut = new IntermediateFunctionFactory();
    javaFunctions = new HashSet<>();
    when(reflections.getSubTypesOf(ODataFunction.class)).thenReturn(javaFunctions);
  }

  @Test
  public void checkReturnEmptyMapIfReflectionsNull() throws ODataJPAModelException {
    assertNotNull(cut.create(new JPADefaultEdmNameBuilder(PUNIT_NAME), (Reflections) null, helper.schema));
  }

  @Test
  public void checkReturnEmptyMapIfNoJavaFunctionsFound() throws ODataJPAModelException {
    assertNotNull(cut.create(new JPADefaultEdmNameBuilder(PUNIT_NAME), reflections, helper.schema));
  }

  @Test
  public void checkReturnMapWithOneIfOneJavaFunctionsFound() throws ODataJPAModelException {
    javaFunctions.add(ExampleJavaOneFunction.class);
    Map<? extends String, ? extends IntermediateFunction> act = cut.create(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        reflections, helper.schema);
    assertEquals(1, act.size());
  }

  @Test
  public void checkReturnMapWithTwoIfTwoJavaFunctionsFound() throws ODataJPAModelException {
    javaFunctions.add(ExampleJavaTwoFunctions.class);
    Map<? extends String, ? extends IntermediateFunction> act = cut.create(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        reflections, helper.schema);
    assertEquals(2, act.size());
  }

  @Test
  public void checkReturnMapWithWithJavaFunctionsFromTwoClassesFound() throws ODataJPAModelException {
    javaFunctions.add(ExampleJavaOneFunction.class);
    javaFunctions.add(ExampleJavaTwoFunctions.class);
    Map<? extends String, ? extends IntermediateFunction> act = cut.create(new JPADefaultEdmNameBuilder(PUNIT_NAME),
        reflections, helper.schema);
    assertEquals(3, act.size());
  }

}