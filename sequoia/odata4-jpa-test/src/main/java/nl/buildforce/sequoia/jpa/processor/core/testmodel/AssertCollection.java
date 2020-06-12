package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AssertCollection {

  public static <T> void assertListEquals(final List<T> exp, final List<T> act, Class<T> reflection) {
    assertEquals(exp.size(), act.size());
    boolean found;
    for (final T expItem : exp) {
      for (T actItem : act) {
        found = EqualsBuilder.reflectionEquals(expItem, actItem, true, reflection);
        if (found) break;
        else assertFalse(false, "Cloud not find" + expItem.toString());
      }
    }
  }

}