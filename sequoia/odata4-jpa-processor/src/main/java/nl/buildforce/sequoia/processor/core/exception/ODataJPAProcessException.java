package nl.buildforce.sequoia.processor.core.exception;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAMessageBufferRead;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAMessageTextBuffer;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.ODataApplicationException;

import java.util.Enumeration;
import java.util.Locale;

public abstract class ODataJPAProcessException extends ODataApplicationException {

  /**
   *
   */
  private static final String UNKNOWN_MESSAGE = "No message text found";
  private static Enumeration<Locale> locales;

  protected final String id;
  protected final String[] parameter;
  protected final String messageText;

  public ODataJPAProcessException(final String id, final HttpStatusCode statusCode) {
    this(id, null, statusCode, new String[] {});
  }

  public ODataJPAProcessException(final Throwable cause, final HttpStatusCode statusCode) {
    this(null, null, statusCode, cause, new String[] {});
  }

  public ODataJPAProcessException(final String id, final HttpStatusCode statusCode, final Throwable cause) {
    this(id, null, statusCode, cause, new String[] {});
  }

  public ODataJPAProcessException(final String id, final HttpStatusCode statusCode, final Throwable cause,
      final String[] params) {
    this(id, null, statusCode, cause, params);
  }

  public ODataJPAProcessException(final String id, final HttpStatusCode statusCode, final String[] params) {
    this(id, null, statusCode, params);
  }

  /**
   *

   * @param id
   * @param messageText
   * @param statusCode
   * @param params
   */
  public ODataJPAProcessException(final String id, final String messageText, final HttpStatusCode statusCode,
      final String[] params) {
    this(id, messageText, statusCode, null, params);
  }

  /**
   *

   * @param id
   * @param messageText
   * @param statusCode
   * @param cause
   * @param params
   */
  public ODataJPAProcessException(final String id, final String messageText, final HttpStatusCode statusCode,
      final Throwable cause, final String[] params) {

    super("", statusCode.getStatusCode(), Locale.ENGLISH, cause);
    this.id = id;
    this.parameter = params;
    this.messageText = messageText;
  }

  protected ODataJPAMessageTextBuffer getTextBundle() {
    if (getBundleName() != null)
      return new ODataJPAMessageTextBuffer(getBundleName(), locales);
    else
      return null;
  }

  @Override
  public String getLocalizedMessage() {
    return getMessage();
  }

  @Override
  public String getMessage() {
    ODataJPAMessageBufferRead messageBuffer = getTextBundle();

    if (messageBuffer != null && id != null) {
      String message = messageBuffer.getText(this, id, parameter);
      if (message != null) {
        return message;
      }
      return messageText;
    } else if (getCause() != null) {
      return getCause().getLocalizedMessage();
    } else if (messageText != null && !messageText.isEmpty()) {
      return messageText;
    } else {
      return UNKNOWN_MESSAGE;
    }
  }

// --Commented out by Inspection START (''21-03-09 21:10):
//  public String[] getParameter() {
//    return parameter;
//  }
// --Commented out by Inspection STOP (''21-03-09 21:10)

// --Commented out by Inspection START (''21-03-09 21:10):
//// --Commented out by Inspection START (''21-03-09 21:10):
////// --Commented out by Inspection START (''21-03-09 21:10):
// --Commented out by Inspection STOP (''21-03-09 21:10)
////  public String getId() {
////    return id;
////  }
//// --Commented out by Inspection STOP (''21-03-09 21:10)
//  public static Enumeration<Locale> getLocales() {
//    return locales;
//  }

//  public static void setLocales(final Enumeration<Locale> locales) {
//    ODataJPAProcessException.locales = locales;
//  }
// --Commented out by Inspection STOP (''21-03-09 21:10)

  protected abstract String getBundleName();

}