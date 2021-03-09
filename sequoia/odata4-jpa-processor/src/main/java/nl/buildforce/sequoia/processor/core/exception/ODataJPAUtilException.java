package nl.buildforce.sequoia.processor.core.exception;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAMessageKey;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;

/*
 * Copied from org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPAModelException
 * See also org.apache.olingo.odata2.jpa.processor.core.exception.ODataJPAMessageServiceDefault
 */
public class ODataJPAUtilException extends ODataJPAProcessException {
  /**
   *

   */
  // private static final long serialVersionUID = -7188499882306858747L;

  public enum MessageKeys implements ODataJPAMessageKey {
    UNKNOWN_NAVI_PROPERTY,
    UNKNOWN_ENTITY_TYPE;

    @Override
    public String getKey() {
      return name();
    }
  }

  private static final String BUNDLE_NAME = "processor-exceptions-i18n";

  public ODataJPAUtilException(final MessageKeys messageKey, final HttpStatusCode statusCode) {
    super(messageKey.getKey(), statusCode);
  }

// --Commented out by Inspection START (''21-03-09 21:34):
//  public ODataJPAUtilException(final Throwable e, final HttpStatusCode statusCode) {
//    super(e, statusCode);
//  }
//
//  public ODataJPAUtilException(final MessageKeys messageKey, final HttpStatusCode statusCode,
//      final Throwable cause, final String... params) {
//    super(messageKey.getKey(), statusCode, cause, params);
//  }
// --Commented out by Inspection STOP (''21-03-09 21:34)

// --Commented out by Inspection START (''21-03-09 21:34):
//  public ODataJPAUtilException(final MessageKeys messageKey, final HttpStatusCode statusCode,
// --Commented out by Inspection START (''21-03-09 21:34):
////      final String... params) {
////    super(messageKey.getKey(), statusCode, params);
////  }
//
//  public ODataJPAUtilException(final MessageKeys messageKey, final HttpStatusCode statusCode, final Throwable e) {
//    super(messageKey.getKey(), statusCode, e);
//  }
// --Commented out by Inspection STOP (''21-03-09 21:34)

  @Override
  protected String getBundleName() {
    return BUNDLE_NAME;
  }

}