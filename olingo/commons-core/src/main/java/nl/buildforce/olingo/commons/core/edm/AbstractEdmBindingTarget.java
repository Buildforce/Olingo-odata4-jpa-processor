/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmBindingTarget;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmException;
import nl.buildforce.olingo.commons.api.edm.EdmMapping;
import nl.buildforce.olingo.commons.api.edm.EdmNavigationPropertyBinding;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlBindingTarget;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;

public abstract class AbstractEdmBindingTarget extends AbstractEdmNamed implements EdmBindingTarget {

  private final CsdlBindingTarget target;
  private final EdmEntityContainer container;

  private List<EdmNavigationPropertyBinding> navigationPropertyBindings;

  public AbstractEdmBindingTarget(Edm edm, EdmEntityContainer container, CsdlBindingTarget target) {
    super(edm, target.getName(), target);
    this.container = container;
    this.target = target;
  }

  @Override
  public List<EdmNavigationPropertyBinding> getNavigationPropertyBindings() {
    if (navigationPropertyBindings == null) {
      List<CsdlNavigationPropertyBinding> providerBindings = target.getNavigationPropertyBindings();
      List<EdmNavigationPropertyBinding> navigationPropertyBindingsLocal =
              new ArrayList<>();
      if (providerBindings != null) {
        for (CsdlNavigationPropertyBinding binding : providerBindings) {
          navigationPropertyBindingsLocal.add(new EdmNavigationPropertyBindingImpl(binding.getPath(),
              binding.getTarget()));
        }
        navigationPropertyBindings = Collections.unmodifiableList(navigationPropertyBindingsLocal);
      }
    }
    return navigationPropertyBindings;
  }

  @Override
  public EdmEntityContainer getEntityContainer() {
    return container;
  }

  @Override
  public EdmEntityType getEntityType() {
    EdmEntityType entityType = edm.getEntityType(target.getTypeFQN());
    if (entityType == null) {
      throw new EdmException("Can´t find entity type: " + target.getTypeFQN() + " for entity set or singleton: "
          + getName());
    }
    return entityType;
  }

  @Override
  public EdmEntityType getEntityTypeWithAnnotations() {
    EdmEntityType entityType = ((AbstractEdm)edm).
        getEntityTypeWithAnnotations(target.getTypeFQN(), true);
    if (entityType == null) {
      throw new EdmException("Can´t find entity type: " + target.getTypeFQN() + " for entity set or singleton: "
          + getName());
    }
    return entityType;
  }
  
  @Override
  public EdmBindingTarget getRelatedBindingTarget(String path) {
    if (path == null) {
      return null;
    }
    EdmBindingTarget bindingTarget = null;
    boolean found = false;
    for (Iterator<EdmNavigationPropertyBinding> itor = getNavigationPropertyBindings().iterator(); itor.hasNext()
        && !found;) {

      EdmNavigationPropertyBinding binding = itor.next();
      if (binding.getPath() == null || binding.getTarget() == null) {
        throw new EdmException("Path or Target in navigation property binding must not be null!");
      }
      if (path.equals(binding.getPath())) {
        Target edmTarget = new Target(binding.getTarget(), container);

        EdmEntityContainer entityContainer = edm.getEntityContainer(edmTarget.getEntityContainer());
        if (entityContainer == null) {
          throw new EdmException("Cannot find entity container with name: " + edmTarget.getEntityContainer());
        }
        try {
          bindingTarget = entityContainer.getEntitySet(edmTarget.getTargetName());

          if (bindingTarget == null) {
            throw new EdmException("Cannot find EntitySet " + edmTarget.getTargetName());
          }
        } catch (EdmException e) {
          // try with singletons ...
          bindingTarget = entityContainer.getSingleton(edmTarget.getTargetName());

          if (bindingTarget == null) {
            throw new EdmException("Cannot find Singleton " + edmTarget.getTargetName(), e);
          }
        } finally {
          found = bindingTarget != null;
        }
      }
    }

    return bindingTarget;
  }

  @Override
  public String getTitle() {
    return target.getTitle();
  }

  @Override
  public EdmMapping getMapping() {
    return target.getMapping();
  }
}
