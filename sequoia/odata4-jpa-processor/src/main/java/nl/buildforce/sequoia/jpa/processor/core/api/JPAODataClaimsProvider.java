package nl.buildforce.sequoia.jpa.processor.core.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container that contains the claims that must be fulfilled, so an entity my be modified or read.
 * @author Oliver Grande
 *
 */
public class JPAODataClaimsProvider implements JPAODataClaimProvider {

  private final Map<String, List<JPAClaimsPair<?>>> claims = new HashMap<>();

  public void add(final String attributeName, final JPAClaimsPair<?> claimsPair) {
    if (!claims.containsKey(attributeName))
      claims.put(attributeName, new ArrayList<>());
    claims.get(attributeName).add(claimsPair);

  }

  @Override
  public List<JPAClaimsPair<?>> get(final String attributeName) { // NOSONAR
    if (!claims.containsKey(attributeName))
      return Collections.emptyList();
    return claims.get(attributeName);
  }

  @Override
  public String toString() {
    return "JPAODataClaimsProvider [claims=" + claims + "]";
  }

}
