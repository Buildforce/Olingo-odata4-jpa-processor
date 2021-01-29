package nl.buildforce.sequoia.processor.core.exception;

import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAMessageKey;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;

public class ODataJPATransactionException extends ODataJPAProcessException { // NOSONAR

  //     private static final long serialVersionUID = -3720990003700857965L;

  public enum MessageKeys implements ODataJPAMessageKey {
    CANNOT_CREATE_NEW_TRANSACTION;

    @Override
    public String getKey() {
      return name();
    }
  }

  public ODataJPATransactionException(final Throwable cause) {
    super(cause, HttpStatusCode.INTERNAL_SERVER_ERROR);
  }

  public ODataJPATransactionException(final MessageKeys messageKey) {
    super(messageKey.name(), HttpStatusCode.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected String getBundleName() {
    return null;
  }

}