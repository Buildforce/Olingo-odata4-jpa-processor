package nl.buildforce.sequoia.jpa.processor.core.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JPAClaimsPairTest {

  @Test
  public void checkCreateIntegerPairOnlyMin() {
    final JPAClaimsPair<Integer> cut = new JPAClaimsPair<>(7);
    assertEquals((Integer) 7, cut.min);
  }

  @Test
  public void checkCreateIntegerPairNoUpperBoundary() {
    final JPAClaimsPair<Integer> cut = new JPAClaimsPair<>(7);
    assertFalse(cut.hasUpperBoundary);
  }

  @Test
  public void checkCreateIntegerPair() {
    final JPAClaimsPair<Integer> cut = new JPAClaimsPair<>(7, 10);
    assertEquals((Integer) 7, cut.min);
    assertEquals((Integer) 10, cut.max);
  }

  @Test
  public void checkCreateIntegerPairUpperBoundary() {
    final JPAClaimsPair<Integer> cut = new JPAClaimsPair<>(7, 10);
    assertTrue(cut.hasUpperBoundary);
  }
}