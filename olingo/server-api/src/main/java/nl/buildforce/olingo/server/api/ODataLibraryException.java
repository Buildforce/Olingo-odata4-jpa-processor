/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;
import java.util.MissingFormatArgumentException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import nl.buildforce.olingo.commons.api.ex.ODataException;

/**
 * Abstract superclass of all translatable server exceptions.
 */
public abstract class ODataLibraryException extends ODataException {

  // private static final long serialVersionUID = -1210541002198287561L;
  private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

  protected static final String DEFAULT_SERVER_BUNDLE_NAME = "server-core-exceptions-i18n";

  /** Key for the exception text in the resource bundle. */
  public interface MessageKey {
    /** Gets this key. */
    String getKey();
  }

  private final MessageKey messageKey;
  private final Object[] parameters;

  protected ODataLibraryException(String developmentMessage, MessageKey messageKey,
                                  String... parameters) {
    super(developmentMessage);
    this.messageKey = messageKey;
    this.parameters = parameters;
  }

  protected ODataLibraryException(String developmentMessage, Throwable cause,
                                  MessageKey messageKey,
                                  String... parameters) {
    super(developmentMessage, cause);
    this.messageKey = messageKey;
    this.parameters = parameters;
  }

  @Override
  public String getLocalizedMessage() {
    return getTranslatedMessage(DEFAULT_LOCALE).getMessage();
  }

  @Override
  public String toString() {
    return getMessage();
  }

  /** Gets the message key. */
  public MessageKey getMessageKey() {
    return messageKey;
  }

  /**
   * Gets the translated message text for a given locale (or the default locale if not available),
   * returning the developer message text if none is found.
   * @param locale the preferred {@link Locale}
   * @return the error message
   */
  public ODataErrorMessage getTranslatedMessage(Locale locale) {
    if (messageKey == null) {
      return new ODataErrorMessage(getMessage(), DEFAULT_LOCALE);
    }
    ResourceBundle bundle = createResourceBundle(locale);
    if (bundle == null) {
      return new ODataErrorMessage(getMessage(), DEFAULT_LOCALE);
    }

    return buildMessage(bundle, locale);
  }

  /**
   * <p>Gets the name of the {@link ResourceBundle} containing the exception texts.</p>
   * <p>The key for an exception text is the concatenation of the exception-class name and
   * the {@link MessageKey}, separated by a dot.</p>
   * @return the name of the resource bundle
   */
  protected abstract String getBundleName();

  private ResourceBundle createResourceBundle(Locale locale) {
    try {
      return ResourceBundle.getBundle(getBundleName(), locale == null ? DEFAULT_LOCALE : locale);
    } catch (MissingResourceException e) {
      return null;
    }
  }

  private ODataErrorMessage buildMessage(ResourceBundle bundle, Locale locale) {
    String message = null;
    StringBuilder builder = new StringBuilder();
    try (Formatter f = new Formatter(builder, locale)) {
      message = bundle.getString(getClass().getSimpleName() + '.' + messageKey.getKey());
      f.format(message, parameters);
      Locale usedLocale = bundle.getLocale();
      if (Locale.ROOT.equals(usedLocale)) {
        usedLocale = DEFAULT_LOCALE;
      }
      return new ODataErrorMessage(builder.toString(), usedLocale);
    } catch (MissingResourceException e) {
      return new ODataErrorMessage("Missing message for key '" + messageKey.getKey() + "'!", DEFAULT_LOCALE);
    } catch (MissingFormatArgumentException e) {
      return new ODataErrorMessage("Missing replacement for place holder in message '" + message +
              "' for following arguments '" + Arrays.toString(parameters) + "'!", DEFAULT_LOCALE);
    }
  }

  /** Error message text and {@link Locale} used for it. */
  public static class ODataErrorMessage {
    private final String message;
    private final Locale locale;

    public ODataErrorMessage(String message, Locale usedLocale) {
      this.message = message;
      locale = usedLocale;
    }

    /** Gets the message text. */
    public String getMessage() {
      return message;
    }

    /** Gets the {@link Locale} used for this message. */
    public Locale getLocale() {
      return locale;
    }
  }

}