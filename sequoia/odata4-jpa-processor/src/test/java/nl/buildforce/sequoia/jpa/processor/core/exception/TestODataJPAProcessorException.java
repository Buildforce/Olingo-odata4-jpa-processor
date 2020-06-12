package nl.buildforce.sequoia.jpa.processor.core.exception;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAMessageKey;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestODataJPAProcessorException {
  // private static String BUNDLE_NAME = "exceptions-i18n";

  public enum MessageKeys implements ODataJPAMessageKey {
    RESULT_NOT_FOUND;

    @Override
    public String getKey() {
      return name();
    }

  }

  @Test
  public void checkSimpleRaiseException() {
    try {
      RaiseException();
    } catch (ODataApplicationException e) {
      assertEquals("No result was fond by Serializer", e.getMessage());
      assertEquals(400, e.getStatusCode());
      return;
    }
    fail();
  }

  @Test
  public void checkSimpleViaMessageKeyRaiseException() {
    try {
      RaiseExceptionParam();
    } catch (ODataApplicationException e) {
      assertEquals("Unable to convert value 'Willi' of parameter 'Hugo'", e.getMessage());
      assertEquals(500, e.getStatusCode());
      return;
    }
    fail();
  }

  private void RaiseExceptionParam() throws ODataJPAProcessException {
    throw new ODataJPADBAdaptorException(ODataJPADBAdaptorException.MessageKeys.PARAMETER_CONVERSION_ERROR,
        HttpStatusCode.INTERNAL_SERVER_ERROR, "Willi", "Hugo");
  }

  private void RaiseException() throws ODataJPAProcessException {
    throw new ODataJPASerializerException(ODataJPASerializerException.MessageKeys.RESULT_NOT_FOUND,
        HttpStatusCode.BAD_REQUEST);
  }

//  private class TestException extends ODataJPAProcessException {
//
//    public TestException(Throwable e, final HttpStatusCode statusCode) {
//      super(e, statusCode);
//    }
//
//    public TestException(final MessageKeys messageKey, final HttpStatusCode statusCode,
//        final Throwable cause, final String... params) {
//      super(messageKey.getKey(), statusCode, cause, params);
//    }
//
//    public TestException(final String id, final HttpStatusCode statusCode) {
//      super(id, statusCode);
//    }
//
//    public TestException(final MessageKeys messageKey, final HttpStatusCode statusCode,
//        final String... params) {
//      super(messageKey.getKey(), statusCode, params);
//    }
//
//    public TestException(final MessageKeys messageKey, final HttpStatusCode statusCode, final Throwable e) {
//      super(messageKey.getKey(), statusCode, e);
//    }
//
//    @Override
//    protected String getBundleName() {
//      return BUNDLE_NAME;
//    }
//  }
}