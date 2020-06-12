package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataGroupsProvider;
import nl.buildforce.sequoia.jpa.processor.core.testmodel.BusinessPartnerWithGroups;
import nl.buildforce.sequoia.jpa.processor.core.util.SelectOptionDouble;
import nl.buildforce.sequoia.jpa.processor.core.util.TestGroupBase;
import nl.buildforce.sequoia.jpa.processor.core.util.UriInfoDouble;
import org.apache.olingo.server.api.ODataApplicationException;
import org.junit.jupiter.api.Test;

import jakarta.persistence.criteria.Selection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class TestJPAQuerySelectWithGroupClause extends TestGroupBase {

  @Test
  public void checkSelectAllWithoutGroupReturnsNotAssigned() throws ODataApplicationException {
    fillJoinTable(root);

    final List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("*"))), root, Collections.emptyList());

    assertContains(selectClause, "ID");
    assertContainsNot(selectClause, "Country");
    assertContainsNot(selectClause, "CommunicationData/LandlinePhoneNumber");
    assertContainsNot(selectClause, "CreationDateTime");
  }

  @Test
  public void checkSelectAllWithOneGroupReturnsAlsoThose() throws ODataApplicationException {
    root = emf.getCriteriaBuilder().createTupleQuery().from(BusinessPartnerWithGroups.class);
    fillJoinTable(root);
    final List<String> groups = new ArrayList<>();
    groups.add("Person");
    final List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("*"))), root, groups);

    assertContains(selectClause, "ID");
    assertContains(selectClause, "Country");
    assertContains(selectClause, "CommunicationData/LandlinePhoneNumber");
    assertContainsNot(selectClause, "CreationDateTime");
  }

  @Test
  public void checkSelectAllWithTwoGroupReturnsAlsoThose() throws ODataApplicationException {
    root = emf.getCriteriaBuilder().createTupleQuery().from(BusinessPartnerWithGroups.class);
    fillJoinTable(root);
    final List<String> groups = new ArrayList<>();
    groups.add("Person");
    groups.add("Company");
    final List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("*"))), root, groups);

    assertContains(selectClause, "ID");
    assertContains(selectClause, "Country");
    assertContains(selectClause, "CommunicationData/LandlinePhoneNumber");
    assertContains(selectClause, "CreationDateTime");
  }

  @Test
  public void checkSelectTwoWithOneGroupReturnsAll() throws ODataApplicationException {
    root = emf.getCriteriaBuilder().createTupleQuery().from(BusinessPartnerWithGroups.class);
    fillJoinTable(root);
    final List<String> groups = new ArrayList<>();
    groups.add("Person");
    groups.add("Company");

    final List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("Type,CreationDateTime"))), root, groups);

    assertContains(selectClause, "ID");
    assertContainsNot(selectClause, "Country");
    assertContainsNot(selectClause, "CommunicationData/LandlinePhoneNumber");
    assertContains(selectClause, "CreationDateTime");
  }

  @Test
  public void checkSelectTwoWithOneGroupReturnsOnlyID() throws ODataApplicationException {
    root = emf.getCriteriaBuilder().createTupleQuery().from(BusinessPartnerWithGroups.class);
    fillJoinTable(root);
    final List<String> groups = new ArrayList<>();
    groups.add("Test");

    final List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("Type,CreationDateTime"))), root, groups);

    assertContains(selectClause, "ID");
    assertContainsNot(selectClause, "Country");
    assertContainsNot(selectClause, "CommunicationData/LandlinePhoneNumber");
    assertContainsNot(selectClause, "CreationDateTime");
  }

  @Test
  public void checkSelectTwoWithoutGroupReturnsOnlyID() throws ODataApplicationException {
    root = emf.getCriteriaBuilder().createTupleQuery().from(BusinessPartnerWithGroups.class);
    fillJoinTable(root);
    final JPAODataGroupsProvider groups = new JPAODataGroupsProvider();
    groups.addGroup("Test");

    final List<Selection<?>> selectClause = cut.createSelectClause(joinTables, cut.buildSelectionPathList(
        new UriInfoDouble(new SelectOptionDouble("Type,CreationDateTime"))), root, Collections.emptyList());

    assertContains(selectClause, "ID");
    assertContainsNot(selectClause, "Country");
    assertContainsNot(selectClause, "CommunicationData/LandlinePhoneNumber");
    assertContainsNot(selectClause, "CreationDateTime");
  }

  private void assertContains(List<Selection<?>> selectClause, String alias) {
    for (Selection<?> selection : selectClause) {
      if (selection.getAlias().equals(alias))
        return;
    }
    fail(alias + " not found");
  }

  private void assertContainsNot(List<Selection<?>> selectClause, String alias) {
    for (Selection<?> selection : selectClause) {
      if (selection.getAlias().equals(alias))
        fail(alias + " was found, but was not expected");
    }
  }

}