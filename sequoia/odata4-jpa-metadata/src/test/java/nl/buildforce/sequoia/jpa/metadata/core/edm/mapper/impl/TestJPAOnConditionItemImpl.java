package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException.MessageKeys;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class TestJPAOnConditionItemImpl {

  @Test
  public void checkThrowsExceptionOnMissingLeft() {
    final JPAPath rightAttribute = mock(JPAPath.class);

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new JPAOnConditionItemImpl(null, rightAttribute));
    assertEquals(MessageKeys.ON_LEFT_ATTRIBUTE_NULL.getKey(), act.getId());
  }

  @Test
  public void checkThrowsExceptionOnMissingRight() {
    final JPAPath leftAttribute = mock(JPAPath.class);

    final ODataJPAModelException act = assertThrows(ODataJPAModelException.class,
        () -> new JPAOnConditionItemImpl(leftAttribute, null));
    assertEquals(MessageKeys.ON_RIGHT_ATTRIBUTE_NULL.getKey(), act.getId());
  }

  @Test
  public void checkReturnProvidedValues() throws ODataJPAModelException {
    final JPAPath leftAttribute = mock(JPAPath.class);
    final JPAPath rightAttribute = mock(JPAPath.class);
    JPAOnConditionItemImpl cut = new JPAOnConditionItemImpl(leftAttribute, rightAttribute);
    assertEquals(leftAttribute, cut.getLeftPath());
    assertEquals(rightAttribute, cut.getRightPath());
  }
}