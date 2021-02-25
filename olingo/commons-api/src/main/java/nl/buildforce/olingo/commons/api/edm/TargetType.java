/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm;

import nl.buildforce.olingo.commons.api.edm.annotation.EdmAnd;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmApply;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmCast;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmEq;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmGe;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmGt;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmIf;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmIsOf;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmLabeledElement;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmLe;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmLt;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmNe;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmNot;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmNull;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmOr;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmPropertyValue;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmRecord;
import nl.buildforce.olingo.commons.api.edm.annotation.EdmUrlRef;
import nl.buildforce.olingo.commons.api.edm.constants.EdmOnDelete;
import nl.buildforce.olingo.commons.api.edmx.EdmxReference;

public enum TargetType {

  // CSDL Types
  Action(EdmAction.class),
  ActionImport(EdmActionImport.class),
  Annotation(EdmAnnotation.class),
  Apply(EdmApply.class),
  Cast(EdmCast.class),
  ComplexType(EdmComplexType.class),
  EntityContainer(EdmEntityContainer.class),
  EntitySet(EdmEntitySet.class),
  EntityType(EdmEntityType.class),
  EnumType(EdmEnumType.class),
  Function(EdmFunction.class),
  FunctionImport(EdmFunctionImport.class),
  If(EdmIf.class),
  IsOf(EdmIsOf.class),
  LabeledElement(EdmLabeledElement.class),
  Member(EdmMember.class),
  NavigationProperty(EdmNavigationProperty.class),
  Null(EdmNull.class),
  OnDelete(EdmOnDelete.class),
  Property(EdmProperty.class),
  PropertyValue(EdmPropertyValue.class),
  Parameter(EdmParameter.class),
  Record(EdmRecord.class),
  ReferentialConstraint(EdmReferentialConstraint.class),
  ReturnType(EdmReturnType.class),
  Schema(EdmSchema.class),
  Singleton(EdmSingleton.class),
  Term(EdmTerm.class),
  TypeDefinition(EdmTypeDefinition.class),
  URLRef(EdmUrlRef.class),
  Reference(EdmxReference.class),
  // Logical Operators
  And(EdmAnd.class),
  Or(EdmOr.class),
  Not(EdmNot.class),
  // ComparisonOperators
  Eq(EdmEq.class),
  Ne(EdmNe.class),
  Gt(EdmGt.class),
  Ge(EdmGe.class),
  Lt(EdmLt.class),
  Le(EdmLe.class);

  private final Class<?> edmClass;

  TargetType(Class<?> edmClass) {
    this.edmClass = edmClass;
  }

  public Class<?> getEdmClass() {
    return edmClass;
  }

}