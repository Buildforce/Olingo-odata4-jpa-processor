package nl.buildforce.sequoia.jpa.processor.core.api;

import jakarta.persistence.EntityManager;

public interface JPAODataRequestContext {

  void setClaimsProvider(final JPAODataClaimProvider provider);

  void setGroupsProvider(final JPAODataGroupProvider provider);

  void setEntityManager(final EntityManager em);

  void setTransactionFactory(final JPAODataTransactionFactory transactionFactory);

}