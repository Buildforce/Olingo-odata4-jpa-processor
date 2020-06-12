package nl.buildforce.sequoia.jpa.processor.core.query;

import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmNavigationPropertyBinding;
import org.apache.olingo.server.api.uri.UriParameter;

import java.util.List;

/**
 * Container to provide result e.g. of target entity set determination
 * @author Oliver Grande
 *
 */
final class EdmEntitySetResult implements EdmEntitySetInfo {

  private final EdmEntitySet edmEntitySet;
  private final List<UriParameter> keyPredicates;
  private final String navigationPath;

  EdmEntitySetResult(final EdmEntitySet edmEntitySet, final List<UriParameter> keyPredicates,
      final String navigationPath) {
    this.edmEntitySet = edmEntitySet;
    this.keyPredicates = keyPredicates;
    this.navigationPath = navigationPath;
  }

  @Override
  public EdmEntitySet getEdmEntitySet() {
    return this.edmEntitySet;
  }

  @Override
  public List<UriParameter> getKeyPredicates() {
    return this.keyPredicates;
  }

  @Override
  public String getName() {
    return edmEntitySet.getName();
  }

  @Override
  public String getNavigationPath() {
    return navigationPath;
  }

  @Override
  public EdmEntitySet getTargetEdmEntitySet() {
    if (navigationPath != null && !navigationPath.isEmpty())
      for (EdmNavigationPropertyBinding navi : this.edmEntitySet.getNavigationPropertyBindings()) {
        if (navi.getPath().equals(navigationPath))
          return edmEntitySet.getEntityContainer().getEntitySet(navi.getTarget());
      }
    return this.edmEntitySet;
  }

}