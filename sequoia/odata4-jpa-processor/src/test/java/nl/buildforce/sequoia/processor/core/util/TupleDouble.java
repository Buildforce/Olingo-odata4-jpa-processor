package nl.buildforce.sequoia.processor.core.util;

import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TupleDouble implements Tuple {
  public final Map<String, Object> elementMap;

  public TupleDouble(final Map<String, Object> elementList) {
    this.elementMap = elementList;
  }

  @Override
  public <X> X get(final TupleElement<X> tupleElement) {
    return null;
  }

  @Override
  public Object get(String alias) {
    return elementMap.get(alias);
  }

  @Override
  public Object get(int i) {
    return null;
  }

  @Override
  public <X> X get(String alias, Class<X> type) {
    return null;
  }

  @Override
  public <X> X get(int i, Class<X> type) {
    return null;
  }

  @Override
  public List<TupleElement<?>> getElements() {
    List<TupleElement<?>> elementList = new ArrayList<>();
    for (String alias : elementMap.keySet())
      elementList.add(new TupleElementDouble(alias, elementMap.get(alias)));
    return elementList;
  }

  @Override
  public Object[] toArray() {
    List<Object> elementList = new ArrayList<>();
    for (String alias : elementMap.keySet())
      elementList.add(elementMap.get(alias));
    return elementList.toArray();
  }

}