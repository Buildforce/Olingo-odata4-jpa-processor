package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

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
    cut = new JPADefaultEdmNameBuilder("org.apache.olingo.jpa");
    assertEquals("OrgApacheOlingoJpaContainer", cut.buildContainerName());
  }
}