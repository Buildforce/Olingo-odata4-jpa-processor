package nl.buildforce.sequoia.jpa.processor.core.api.example;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAMessageKey;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessException;
import org.apache.olingo.commons.api.http.HttpStatusCode;

public class JPAExampleModifyException extends ODataJPAProcessException { // NOSONAR

  private static final long serialVersionUID = 121932494074522961L;
  private static final String BUNDLE_NAME = "example-exceptions-i18n";

  public enum MessageKeys implements ODataJPAMessageKey {
    ENTITY_NOT_FOUND,
    ENTITY_ALREADY_EXISTS;

    @Override
    public String getKey() {
      return name();
    }

  }

  public JPAExampleModifyException(final MessageKeys messageKey, final HttpStatusCode statusCode) {
    super(messageKey.getKey(), statusCode);
  }

  @Override
  protected String getBundleName() {
    return BUNDLE_NAME;
  }

}