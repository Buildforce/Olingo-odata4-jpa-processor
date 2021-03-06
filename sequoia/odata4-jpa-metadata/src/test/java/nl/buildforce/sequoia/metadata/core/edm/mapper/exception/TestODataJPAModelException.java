package nl.buildforce.sequoia.metadata.core.edm.mapper.exception;

import nl.buildforce.sequoia.processor.core.testmodel.LocaleEnumeration;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestODataJPAModelException {
  private static final String BUNDLE_NAME = "test-i18n";

  @Test
  public void checkTextInDefaultLocale() {
    try {
      RaiseException();
    } catch (ODataJPAException e) {
      assertEquals("An English message", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void checkTextInGerman() {
    try {
      ArrayList<Locale> localesList = new ArrayList<>();
      localesList.add(Locale.GERMAN);
      Enumeration<Locale> locales = new LocaleEnumeration(localesList);
      TestException.setLocales(locales);
      RaiseException();
    } catch (ODataJPAException e) {
      assertEquals("Ein deutscher Text", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void checkTextInDefaultLocaleWithParameter() {
    try {
      RaiseExceptionParam();
    } catch (ODataJPAException e) {
      assertEquals("Willi looks for Hugo", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void checkTextOnlyCause() {
    try {
      RaiseExceptionCause();
    } catch (ODataJPAException e) {
      assertEquals("Test text from cause", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void checkTextIdAndCause() {
    try {
      RaiseExceptionIDCause();
    } catch (ODataJPAException e) {
      assertEquals("An English message", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void checkTextIdAndCauseAndParameter() {
    try {
      RaiseExceptionIDCause("Willi", "Hugo");
    } catch (ODataJPAException e) {
      assertEquals("Willi looks for Hugo", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void checkTextNullId() {
    try {
      RaiseEmptyIDException();
    } catch (ODataJPAException e) {
      assertEquals("No message text found", e.getMessage());
      return;
    }
    fail();
  }

  private void RaiseExceptionIDCause(String... params) throws TestException {
    try {
      raiseNullPointer();
    } catch (NullPointerException e) {
      if (params.length == 0)
        throw new TestException("FIRST_MESSAGE", e);
      else
        throw new TestException("SECOND_MESSAGE", e, params);
    }
  }

  private void RaiseExceptionCause() throws ODataJPAException {
    try {
      raiseNullPointer();
    } catch (NullPointerException e) {
      throw new TestException(e);
    }
  }

  private void raiseNullPointer() throws NullPointerException {
    throw new NullPointerException("Test text from cause");
  }

  private void RaiseExceptionParam() throws ODataJPAException {
    throw new TestException("SECOND_MESSAGE", "Willi", "Hugo");
  }

  private void RaiseException() throws ODataJPAException {
    throw new TestException("FIRST_MESSAGE");
  }

  private void RaiseEmptyIDException() throws ODataJPAException {
    throw new TestException("");
  }

  private static class TestException extends ODataJPAException {

    public TestException(String id) {
      super(id);
    }

    public TestException(String id, String... params) {
      super(id, params);
    }

    public TestException(Throwable e) {
      super(e);
    }

    public TestException(String id, Throwable e) {
      super(id, e);
    }

    public TestException(String id, Throwable e, String[] params) {
      super(id, e, params);
    }

    @Override
    protected String getBundleName() {
      return BUNDLE_NAME;
    }
  }
}