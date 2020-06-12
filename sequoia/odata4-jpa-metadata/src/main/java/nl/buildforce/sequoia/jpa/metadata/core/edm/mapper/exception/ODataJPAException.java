package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception;

import org.apache.olingo.commons.api.ex.ODataException;

import java.util.Enumeration;
import java.util.Locale;

public abstract class ODataJPAException extends ODataException {
  private static final String UNKNOWN_MESSAGE = "No message text found";
  private static Enumeration<Locale> locales;

  public static Enumeration<Locale> getLocales() {
    return locales;
  }

  public static void setLocales(final Enumeration<Locale> locales) {
    ODataJPAException.locales = locales;
  }

  protected String id;
  protected ODataJPAMessageTextBuffer messageBuffer;
  protected String[] parameter;

  public ODataJPAException(final String id) {
    super("");
    this.id = id;
    messageBuffer = new ODataJPAMessageTextBuffer(getBundleName(), locales);
  }

  public ODataJPAException(final String id, final String... params) {
    super("");
    this.id = id;
    this.parameter = params;
    messageBuffer = new ODataJPAMessageTextBuffer(getBundleName(), locales);
  }

  public ODataJPAException(final String id, final Throwable cause, final String... params) {
    super("", cause);
    this.id = id;
    this.parameter = params;
    messageBuffer = new ODataJPAMessageTextBuffer(getBundleName(), locales);
  }

  public ODataJPAException(final String id, final Throwable cause) {
    super("", cause);
    this.id = id;
    this.messageBuffer = new ODataJPAMessageTextBuffer(getBundleName(), locales);
  }

  public ODataJPAException(final Throwable cause) {
    super(cause);
  }

  @Override
  public String getLocalizedMessage() {
    return getMessage();
  }

  @Override
  public String getMessage() {
    return (id == null || id.isEmpty() || messageBuffer == null) ?
            getCause() == null ? UNKNOWN_MESSAGE : getCause().getLocalizedMessage()
            : messageBuffer.getText(this, id, parameter);
  }

  protected abstract String getBundleName();

}