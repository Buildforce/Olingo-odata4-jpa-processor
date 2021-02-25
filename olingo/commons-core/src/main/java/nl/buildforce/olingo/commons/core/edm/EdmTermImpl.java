/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmTerm;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;
import nl.buildforce.olingo.commons.api.edm.TargetType;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlTerm;

public class EdmTermImpl extends AbstractEdmNamed implements EdmTerm {

  private final CsdlTerm term;
  private final FullQualifiedName fqn;
  private EdmType termType;
  private EdmTerm baseTerm;
  private List<TargetType> appliesTo;

  public EdmTermImpl(Edm edm, String namespace, CsdlTerm term) {
    super(edm, term.getName(), term);
    this.term = term;
    fqn = new FullQualifiedName(namespace, term.getName());
  }

  @Override
  public FullQualifiedName getFullQualifiedName() {
    return fqn;
  }

  @Override
  public EdmType getType() {
    if (termType == null) {
      if (term.getType() == null) {
        throw new EdmException("Terms must hava a full qualified type.");
      }
      termType = new EdmTypeInfo.Builder().setEdm(edm).setTypeExpression(term.getType()).build().getType();
      if (termType == null) {
        throw new EdmException("Cannot find type with name: " + term.getType());
      }
    }
    return termType;
  }

  @Override
  public EdmTerm getBaseTerm() {
    if (baseTerm == null && term.getBaseTerm() != null) {
      baseTerm = edm.getTerm(new FullQualifiedName(term.getBaseTerm()));
    }
    return baseTerm;
  }

  @Override
  public List<TargetType> getAppliesTo() {
    if (appliesTo == null) {
      ArrayList<TargetType> localAppliesTo = new ArrayList<>();
      for (String apply : term.getAppliesTo()) {
        try {
          localAppliesTo.add(TargetType.valueOf(apply));
        } catch (IllegalArgumentException e) {
          throw new EdmException("Invalid AppliesTo value: " + apply, e);
        }
      }
      appliesTo = Collections.unmodifiableList(localAppliesTo);
    }
    return appliesTo;
  }

  @Override
  public boolean isNullable() {
    return term.isNullable();
  }

  @Override
  public Integer getMaxLength() {
    return term.getMaxLength();
  }

  @Override
  public Integer getPrecision() {
    return term.getPrecision();
  }

  @Override
  public Integer getScale() {
    return term.getScale();
  }

  /*@Override
  public SRID getSrid() {
    return term.getSrid();
  }*/

  @Override
  public String getDefaultValue() {
    return term.getDefaultValue();
  }
}