package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api;

import java.util.Map;

public interface JPADescriptionAttribute extends JPAAttribute {

  boolean isLocationJoin();

  JPAAttribute getDescriptionAttribute();

  JPAPath getLocaleFieldName();

  Map<JPAPath, String> getFixedValueAssignment();

}