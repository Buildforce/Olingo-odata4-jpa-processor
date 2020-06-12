package nl.buildforce.sequoia.jpa.processor.core.query;

import java.util.Objects;

/**
 *

 *

 * @author Oliver Grande
 * Created: 10.11.2019
 *
 */
public class JPAKeyBoundary {

  private final int noHops;
  private final JPAKeyPair keyBoundary;

  JPAKeyBoundary(int noHops, JPAKeyPair keyBoundary) {
    this.noHops = noHops;
    this.keyBoundary = Objects.requireNonNull(keyBoundary);
  }

  public int getNoHops() {
    return noHops;
  }

  public JPAKeyPair getKeyBoundary() {
    return keyBoundary;
  }

  @Override
  public String toString() {
    return "JPAKeyBoundary [noHops=" + noHops + ", keyBoundary=" + keyBoundary + "]";
  }
}