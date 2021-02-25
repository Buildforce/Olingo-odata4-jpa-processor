/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.data;

import java.net.URI;
import java.util.List;

public abstract class AbstractEntityCollection extends AbstractODataObject implements Iterable<Entity> {
  public abstract Integer getCount();

  public abstract URI getNext();

  public abstract URI getDeltaLink();
  
  public abstract List<Operation> getOperations();
}
