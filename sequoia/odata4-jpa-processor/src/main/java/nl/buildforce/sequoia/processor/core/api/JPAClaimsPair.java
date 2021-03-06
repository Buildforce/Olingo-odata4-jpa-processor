package nl.buildforce.sequoia.processor.core.api;

/**
 * Allows to provide a single value or a closed interval.
 *
 * @param <T>
 */
public class JPAClaimsPair<T> {
  public final T min;
  public final T max;
  public final boolean hasUpperBoundary;

  public JPAClaimsPair(final T min) {
    this.min = min;
    this.max = null;
    this.hasUpperBoundary = false;

  }

  public JPAClaimsPair(final T min, final T max) {
    this.min = min;
    this.max = max;
    this.hasUpperBoundary = true;
  }

  @Override
  public String toString() {
    return "JPAClaimsPair [min=" + min + ", max=" + max + "]";
  }

  @SuppressWarnings("unchecked")
  public <Y> Y minAs() {
    return (Y) min;
  }

  @SuppressWarnings("unchecked")
  public <Y> Y maxAs() {
    return (Y) max;
  }

}