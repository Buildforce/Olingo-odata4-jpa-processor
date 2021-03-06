/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.annotation.EdmConstantExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmDynamicExpression;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmExpression;

public class AbstractAnnotationTest {

  protected EdmDynamicExpression assertDynamic(EdmExpression exp) {
    assertNotNull(exp);
    assertTrue(exp.isDynamic());
    assertFalse(exp.isConstant());
    assertNull(exp.asConstant());
    return exp.asDynamic();
  }

  protected EdmConstantExpression assertConstant(EdmExpression exp) {
    assertNotNull(exp);
    assertTrue(exp.isConstant());
    assertFalse(exp.isDynamic());
    assertNull(exp.asDynamic());
    return exp.asConstant();
  }
  
  protected void assertSingleKindDynamicExpression(EdmDynamicExpression dynExpr){
    List<Boolean> allIsMethodValues = new ArrayList<Boolean>();
    //Logical Operators
    allIsMethodValues.add(dynExpr.isAnd());
    allIsMethodValues.add(dynExpr.isOr());
    allIsMethodValues.add(dynExpr.isNot());
    //Comparison Operators
    allIsMethodValues.add(dynExpr.isEq());
    allIsMethodValues.add(dynExpr.isNe());
    allIsMethodValues.add(dynExpr.isGt());
    allIsMethodValues.add(dynExpr.isGe());
    allIsMethodValues.add(dynExpr.isLt());
    allIsMethodValues.add(dynExpr.isLe());
    //Other Dynamic Kinds
    allIsMethodValues.add(dynExpr.isAnnotationPath());
    allIsMethodValues.add(dynExpr.isApply());
    allIsMethodValues.add(dynExpr.isCast());
    allIsMethodValues.add(dynExpr.isCollection());
    allIsMethodValues.add(dynExpr.isIf());
    allIsMethodValues.add(dynExpr.isIsOf());
    allIsMethodValues.add(dynExpr.isLabeledElement());
    allIsMethodValues.add(dynExpr.isLabeledElementReference());
    allIsMethodValues.add(dynExpr.isNull());
    allIsMethodValues.add(dynExpr.isNavigationPropertyPath());
    allIsMethodValues.add(dynExpr.isPath());
    allIsMethodValues.add(dynExpr.isPropertyPath());
    allIsMethodValues.add(dynExpr.isPropertyValue());
    allIsMethodValues.add(dynExpr.isRecord());
    allIsMethodValues.add(dynExpr.isUrlRef());
    
    
    //Remove all false values so that only one "true" value remains
    allIsMethodValues.removeAll(Collections.singletonList(Boolean.FALSE));
    assertFalse(allIsMethodValues.contains(null));
    assertTrue(allIsMethodValues.contains(Boolean.TRUE));
    assertEquals(1, allIsMethodValues.size());
    
    
    
    List<Object> allAsMethodValues = new ArrayList<Object>();
    //Logical Operators
    allAsMethodValues.add(dynExpr.asAnd());
    allAsMethodValues.add(dynExpr.asOr());
    allAsMethodValues.add(dynExpr.asNot());
    //Comparison Operators
    allAsMethodValues.add(dynExpr.asEq());
    allAsMethodValues.add(dynExpr.asNe());
    allAsMethodValues.add(dynExpr.asGt());
    allAsMethodValues.add(dynExpr.asGe());
    allAsMethodValues.add(dynExpr.asLt());
    allAsMethodValues.add(dynExpr.asLe());
    //Other Dynamic Kinds
    allAsMethodValues.add(dynExpr.asAnnotationPath());
    allAsMethodValues.add(dynExpr.asApply());
    allAsMethodValues.add(dynExpr.asCast());
    allAsMethodValues.add(dynExpr.asCollection());
    allAsMethodValues.add(dynExpr.asIf());
    allAsMethodValues.add(dynExpr.asIsOf());
    allAsMethodValues.add(dynExpr.asLabeledElement());
    allAsMethodValues.add(dynExpr.asLabeledElementReference());
    allAsMethodValues.add(dynExpr.asNull());
    allAsMethodValues.add(dynExpr.asNavigationPropertyPath());
    allAsMethodValues.add(dynExpr.asPath());
    allAsMethodValues.add(dynExpr.asPropertyPath());
    allAsMethodValues.add(dynExpr.asPropertyValue());
    allAsMethodValues.add(dynExpr.asRecord());
    allAsMethodValues.add(dynExpr.asUrlRef());
    
    //Remove all false values so that only one "true" value remains
    allAsMethodValues.removeAll(Collections.singletonList(null));
    assertFalse(allAsMethodValues.contains(null));
    assertEquals(1, allAsMethodValues.size());
    assertTrue(allAsMethodValues.get(0) instanceof EdmDynamicExpression);
  }

}