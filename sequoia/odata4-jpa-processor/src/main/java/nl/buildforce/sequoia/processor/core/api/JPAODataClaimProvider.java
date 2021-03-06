package nl.buildforce.sequoia.processor.core.api;

import java.util.List;

/**
 * Container that provides claims
 *
 *
 */
public interface JPAODataClaimProvider {

  List<JPAClaimsPair<?>> get(final String attributeName); // NOSONAR

}