package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.ODataApplicationException;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;

class JPAAggregationOperationImp implements JPAAggregationOperation {

  private final From<?, ?> root;
  private final JPAOperationConverter converter;

  public JPAAggregationOperationImp(final From<?, ?> root, final JPAOperationConverter converter) {
    this.root = root;
    this.converter = converter;
  }

  /*
   * (non-Javadoc)
   *
   * @see nl.buildforce.sequoia.jpa.processor.core.filter.JPAAggregationOperation#get()
   */
  @Override
  public Object get() throws ODataApplicationException {
    return converter.convert(this);
  }

  @Override
  public JPAFilterAggregationType getAggregation() {
    return JPAFilterAggregationType.COUNT;
  }

  Expression<?> getPath() {
    return root;
  }

  @Override
  public String getName() {
    return JPAFilterAggregationType.COUNT.name();
  }

}