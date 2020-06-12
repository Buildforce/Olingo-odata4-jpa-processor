package nl.buildforce.sequoia.jpa.processor.core.api;

import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPATransactionException;

import jakarta.persistence.RollbackException;

/**
 * A wrapper to abstract from various transaction APIs provided by JAVA or e.g. Spring like
 * javax.persistence.EntityTransaction, javax.transaction.UserTransaction, javax.transaction.Transaction or
 * org.springframework.transaction.jta.JtaTransactionManager. </p>
 *

 * JPA Processor needs to be able to create transactions to be able to handle <a
 * href="http://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part1-protocol/odata-v4.0-errata03-os-part1-protocol-complete.html#_Toc453752316">Change
 * Sets</a> correctly. Each Change Set has to be processed in an own transaction. Please note that the batch processor
 * will create a response with http status code <i>501 Not Implemented</i> in case he cannot create a transaction.<p>
 *

 * In case not TransactionFactory is provided the JPA Processor will create an instance of
 * {@link JPAODataDefaultTransactionFactory}, which shall be sufficient for most uses cases.
 *

 * @author Oliver Grande
 * Created: 07.10.2019
 *
 */
public interface JPAODataTransactionFactory {
  /**
   *
   * @return a new transaction
   */
  JPAODataTransaction createTransaction() throws ODataJPATransactionException;

  boolean hasActiveTransaction();

  interface JPAODataTransaction {

    /**
     *

     * @throws ODataJPATransactionException
     * @throws RollbackException
     */
    void commit() throws ODataJPATransactionException, RollbackException; // NOSONAR

    void rollback() throws ODataJPATransactionException;

    boolean isActive() throws ODataJPATransactionException;

    boolean rollbackOnly() throws ODataJPATransactionException;
  }

}