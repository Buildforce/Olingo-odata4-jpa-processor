/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

public class TranslatedExceptionsTest {

  private static final String DEV = "devMessage";

  private static class TestException extends ODataLibraryException {
    //     private static final long serialVersionUID = -7199975861656921724L;

    public static enum Keys implements MessageKey {
      BASIC, ONEPARAM, TWOPARAM, NOMESSAGE, ONLY_ROOT, ONLY_GERMAN;
      @Override
      public String getKey() {
        return name();
      }
    }

    protected TestException(MessageKey messageKey, String... parameters) {
      super(DEV, messageKey, parameters);
    }

    @Override
    protected String getBundleName() {
      return "i18n";
    }
  }

  @Test
  public void basic() {
    TestException exp = new TestException(TestException.Keys.BASIC);
    assertEquals(DEV, exp.getMessage());
    assertEquals(DEV, exp.toString());
    assertEquals("Test Default", exp.getLocalizedMessage());
    assertEquals(TestException.Keys.BASIC, exp.getMessageKey());

    checkTranslatedMessage(exp.getTranslatedMessage(null), "Test Default", Locale.ENGLISH);
    checkTranslatedMessage(exp.getTranslatedMessage(Locale.ENGLISH), "Test Default", Locale.ENGLISH);
    checkTranslatedMessage(exp.getTranslatedMessage(Locale.UK), "Test Default", Locale.ENGLISH);
    checkTranslatedMessage(exp.getTranslatedMessage(Locale.GERMAN), "Test DE", Locale.GERMAN);
    checkTranslatedMessage(exp.getTranslatedMessage(Locale.GERMANY), "Test DE", Locale.GERMAN);
  }

  @Test
  public void unusedParametersMustNotResultInAnException() {
    TestException exp = new TestException(TestException.Keys.BASIC, "unusedParam1", "unusedParam2");
    assertEquals(DEV, exp.getMessage());
    checkTranslatedMessage(exp.getTranslatedMessage(null), "Test Default", Locale.ENGLISH);
  }

  @Test
  public void useOneParameter() {
    TestException exp = new TestException(TestException.Keys.ONEPARAM, "usedParam1");
    assertEquals(DEV, exp.getMessage());
    checkTranslatedMessage(exp.getTranslatedMessage(null), "Param1: usedParam1", Locale.ENGLISH);
  }

  @Test
  public void useOneParameterExpectedButMultipleGiven() {
    TestException exp = new TestException(TestException.Keys.ONEPARAM, "usedParam1", "unusedParam2");
    assertEquals(DEV, exp.getMessage());
    checkTranslatedMessage(exp.getTranslatedMessage(null), "Param1: usedParam1", Locale.ENGLISH);
  }

  @Test
  public void useTwoParameters() {
    TestException exp = new TestException(TestException.Keys.TWOPARAM, "usedParam1", "usedParam2");
    assertEquals(DEV, exp.getMessage());
    checkTranslatedMessage(exp.getTranslatedMessage(null), "Param1: usedParam1 Param2: usedParam2", Locale.ENGLISH);
  }

  @Test
  public void parametersNotGivenAlthoughNeeded() {
    TestException exp = new TestException(TestException.Keys.ONEPARAM);
    assertEquals(DEV, exp.getMessage());

    ODataLibraryException.ODataErrorMessage translatedMessage = exp.getTranslatedMessage(null);
    assertNotNull(translatedMessage);
    assertThat(translatedMessage.getMessage(), containsString("Missing replacement for place holder in message"));
  }

  @Test
  public void noMessageKey() {
    TestException exp = new TestException(null);
    assertEquals(DEV, exp.getMessage());

    ODataLibraryException.ODataErrorMessage translatedMessage = exp.getTranslatedMessage(null);
    assertNotNull(translatedMessage);
    assertEquals(DEV, translatedMessage.getMessage());
  }

  @Test
  public void noMessageForKey() {
    TestException exp = new TestException(TestException.Keys.NOMESSAGE);
    assertEquals(DEV, exp.getMessage());

    ODataLibraryException.ODataErrorMessage translatedMessage = exp.getTranslatedMessage(null);
    assertNotNull(translatedMessage);
    assertThat(translatedMessage.getMessage(), containsString("Missing message for key"));
  }

  @Test
  public void keyForRootBundleButNotPresentInDerivedBundle() {
    TestException exp = new TestException(TestException.Keys.ONLY_ROOT);
    assertEquals(DEV, exp.getMessage());

    checkTranslatedMessage(exp.getTranslatedMessage(Locale.GERMAN), "Root message", Locale.GERMAN);
    checkTranslatedMessage(exp.getTranslatedMessage(Locale.ROOT), "Root message", Locale.ENGLISH);
  }

  @Test
  public void defaultLocale() {
    TestException exp = new TestException(TestException.Keys.ONLY_GERMAN);
    assertEquals(DEV, exp.getMessage());

    Locale.setDefault(Locale.GERMAN);
    ODataLibraryException.ODataErrorMessage translatedMessage = exp.getTranslatedMessage(null);
    assertNotNull(translatedMessage);
    assertThat(translatedMessage.getMessage(), containsString("Missing message for key"));
    assertEquals(Locale.ENGLISH, translatedMessage.getLocale());

    Locale.setDefault(Locale.ENGLISH);
    translatedMessage = exp.getTranslatedMessage(null);
    assertNotNull(translatedMessage);
    assertThat(translatedMessage.getMessage(), containsString("Missing message for key"));
    assertEquals(Locale.ENGLISH, translatedMessage.getLocale());
  }

  private void checkTranslatedMessage(ODataLibraryException.ODataErrorMessage translatedMessage,
                                      String expectedText, Locale expectedLocale) {
    assertNotNull(translatedMessage);
    assertEquals(expectedText, translatedMessage.getMessage());
    assertEquals(expectedLocale, translatedMessage.getLocale());
  }
  
  @Test
  public void testODataApplicationException1() {
    ODataApplicationException exp = new ODataApplicationException("Exception", 500,
        Locale.ENGLISH, new RuntimeException("Error"));
    assertNotNull(exp);
  }
  
  @Test
  public void testODataApplicationException2() {
    ODataApplicationException exp = new ODataApplicationException("Exception", 
        500, Locale.ENGLISH, new RuntimeException("Error"), "500");
    assertNotNull(exp);
  }

}