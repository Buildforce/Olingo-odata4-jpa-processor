/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Formatter;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.core.uri.parser.UriParserSemanticException;
import nl.buildforce.olingo.server.core.uri.parser.UriParserSyntaxException;
import nl.buildforce.olingo.server.core.uri.validator.UriValidationException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Generic test for all exceptions which inherit from ODataTranslatedException
 * if their MessageKeys are available in the resource bundle and the parameters are replaced.
 */
public class TranslatedExceptionSubclassesTest {

  private final Properties properties;

  public TranslatedExceptionSubclassesTest() throws IOException {
    properties = new Properties();
    properties.load(Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("server-core-exceptions-i18n.properties"));
    Locale.setDefault(Locale.ENGLISH);
  }

  @Test
  public void messageKeysValid() throws Exception {
    testException(ODataHandlerException.class, ODataHandlerException.MessageKeys.values());
    testException(UriParserSemanticException.class, UriParserSemanticException.MessageKeys.values());
    testException(UriParserSyntaxException.class, UriParserSyntaxException.MessageKeys.values());
    testException(ContentNegotiatorException.class, ContentNegotiatorException.MessageKeys.values());
    testException(SerializerException.class, SerializerException.MessageKeys.values());
    testException(UriValidationException.class, UriValidationException.MessageKeys.values());
    testException(UriParserSyntaxException.class, UriParserSyntaxException.MessageKeys.values());
  }

  private void testException(Class<? extends ODataLibraryException> clazz,
                             ODataLibraryException.MessageKey[] messageKeys) throws Exception {

    for (ODataLibraryException.MessageKey messageKey : messageKeys) {
      String propKey = clazz.getSimpleName() + "." + messageKey.toString();
      String value = properties.getProperty(propKey);
      Assert.assertNotNull("No value found for message key '" + propKey + "'", value);
      //
      int paraCount = countParameters(value);
      Constructor<? extends ODataLibraryException> ctor =
          clazz.getConstructor(String.class, ODataLibraryException.MessageKey.class, String[].class);
      String[] paras = new String[paraCount];
      for (int i = 0; i < paras.length; i++) {
        paras[i] = "470" + i;
      }
      String developerMessage = UUID.randomUUID().toString();
      ODataLibraryException e = ctor.newInstance(developerMessage, messageKey, paras);
      try {
        throw e;
      } catch (ODataLibraryException translatedException) {
        Formatter formatter = new Formatter();
        String formattedValue = formatter.format(value, (Object[]) paras).toString();
        formatter.close();
        Assert.assertEquals(formattedValue, translatedException.getTranslatedMessage(null).getMessage());
        Assert.assertEquals(formattedValue, translatedException.getLocalizedMessage());
        Assert.assertEquals(developerMessage, translatedException.getMessage());
      }
    }
  }

  private int countParameters(String value) {
    char[] chars = value.toCharArray();
    int count = 0;
    for (char aChar : chars) {
      if (aChar == '%') {
        count++;
      }
    }
    return count;
  }

}