package nl.buildforce.sequoia.jpa.processor.core.api;

import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessorException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestJPAAbstractCUDRequestHandler {
  private JPAAbstractCUDRequestHandler cut;

  @BeforeEach
  public void setup() {
    cut = new ExampleJPAAbstractCUDRequestHandler();
  }

  @Test
  public void testThrowsNotImplementedOnDelete() {
    final ODataJPAProcessorException act = assertThrows(ODataJPAProcessorException.class, () -> cut.deleteEntity(null,
        null));
    assertEquals(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), act.getStatusCode());
  }

  @Test
  public void testThrowsNotImplementedOnUpdate() {
    final ODataJPAProcessorException act = assertThrows(ODataJPAProcessorException.class, () -> cut.updateEntity(null,
        null, null));
    assertEquals(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), act.getStatusCode());
  }

  @Test
  public void testThrowsNotImplementedOnCreate() {
    final ODataJPAProcessorException act = assertThrows(ODataJPAProcessorException.class, () -> cut.createEntity(null,
        null));
    assertEquals(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), act.getStatusCode());
  }

  private static class ExampleJPAAbstractCUDRequestHandler extends JPAAbstractCUDRequestHandler {

  }
}