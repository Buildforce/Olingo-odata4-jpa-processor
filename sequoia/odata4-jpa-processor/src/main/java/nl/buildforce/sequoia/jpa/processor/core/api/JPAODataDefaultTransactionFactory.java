package nl.buildforce.sequoia.jpa.processor.core.api;

import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPATransactionException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;
import java.util.Objects;

import static nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPATransactionException.MessageKeys.CANNOT_CREATE_NEW_TRANSACTION;

public class JPAODataDefaultTransactionFactory implements JPAODataTransactionFactory {

  private final EntityManager em;
  private JPAODataTransaction currentTransaction;

  public JPAODataDefaultTransactionFactory(final EntityManager em) {
    this.em = Objects.requireNonNull(em);
  }

  @Override
  public JPAODataTransaction createTransaction() throws ODataJPATransactionException {
    try {
      if (currentTransaction != null && currentTransaction.isActive())
        throw new ODataJPATransactionException(CANNOT_CREATE_NEW_TRANSACTION);
      currentTransaction = new JPAODataEntityTransaction(em.getTransaction());
      return currentTransaction;
    } catch (Exception e) {
      throw new ODataJPATransactionException(CANNOT_CREATE_NEW_TRANSACTION);
    }
  }

  @Override
  public boolean hasActiveTransaction() {
    try {
      final boolean baseActive = em.getTransaction().isActive();
      if (currentTransaction == null && !baseActive)
        return false;
      else if (currentTransaction == null)
        return true;
      return currentTransaction.isActive();
    } catch (RuntimeException | ODataJPATransactionException e) {
      return true;
    }
  }

  private static class JPAODataEntityTransaction implements JPAODataTransaction {

    private final EntityTransaction et;

    public JPAODataEntityTransaction(final EntityTransaction et) {
      this.et = et;
      this.et.begin();
    }

    @Override
    public void commit() throws ODataJPATransactionException {
      try {
        et.commit();
      } catch (RollbackException e) {
        throw e;
      } catch (RuntimeException e) {
        throw new ODataJPATransactionException(e);
      }
    }

    @Override
    public void rollback() throws ODataJPATransactionException {
      try {
        et.rollback();
      } catch (RuntimeException e) {
        throw new ODataJPATransactionException(e);
      }
    }

    @Override
    public boolean isActive() throws ODataJPATransactionException {
      try {
        return et.isActive();
      } catch (RuntimeException e) {
        throw new ODataJPATransactionException(e);
      }
    }

    @Override
    public boolean rollbackOnly() throws ODataJPATransactionException {
      try {
        return et.getRollbackOnly();
      } catch (RuntimeException e) {
        throw new ODataJPATransactionException(e);
      }
    }
  }

}