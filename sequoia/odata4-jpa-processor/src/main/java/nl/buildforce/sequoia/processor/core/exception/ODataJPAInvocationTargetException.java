package nl.buildforce.sequoia.processor.core.exception;

import nl.buildforce.olingo.commons.api.http.HttpStatusCode;

/*
 * This exception is thrown when an exception occurs in a jpa pojo method
 */
public class ODataJPAInvocationTargetException extends ODataJPAProcessException {

  private static final String BUNDLE_NAME = "processor-exceptions-i18n";
  private final String path;

  public ODataJPAInvocationTargetException(Throwable e, final String path) {
    super(e, HttpStatusCode.BAD_REQUEST);
    this.path = path;
  }

/*  enum MessageKeys implements ODataJPAMessageKey {
    WRONG_VALUE;
    @Override
    public String getKey() {
      return name();
    }
  }

  public ODataJPAInvocationTargetException(Throwable e) {
    super(e, HttpStatusCode.BAD_REQUEST);
    this.path = null;
  }
*/

  @Override
  protected String getBundleName() { return BUNDLE_NAME; }

  public String getPath() { return path; }

}