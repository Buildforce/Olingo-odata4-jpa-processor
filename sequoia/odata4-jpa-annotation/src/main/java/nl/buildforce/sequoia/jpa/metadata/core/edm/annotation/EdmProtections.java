package nl.buildforce.sequoia.jpa.metadata.core.edm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Allows to combine either multiple claims for a simple attribute, which will be combined with AND,
 * or to be able to protect multiple attributes at a complex attribute.
 * @author Oliver Grande
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface EdmProtections {
  EdmProtectedBy[] value();
}