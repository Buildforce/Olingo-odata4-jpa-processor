package nl.buildforce.sequoia.metadata.core.edm.mapper.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJPAEdmNameBuilder {
  private JPADefaultEdmNameBuilder cut;

/*
  @BeforeEach
  public void setup() {

  }
*/

  @Test
  public void CheckBuildContainerNameSimple() {
    cut = new JPADefaultEdmNameBuilder("cdw");
    assertEquals("CdwContainer", cut.buildContainerName());
  }

  @Test
  public void CheckBuildContainerNameComplex() {
    cut = new JPADefaultEdmNameBuilder("nl.buildforce.olingo");
    assertEquals("NlBuildforceOlingoContainer", cut.buildContainerName());
  }
}