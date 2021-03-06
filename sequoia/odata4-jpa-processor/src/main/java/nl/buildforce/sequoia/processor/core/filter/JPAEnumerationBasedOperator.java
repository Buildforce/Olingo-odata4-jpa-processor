package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.sequoia.processor.core.exception.ODataJPAFilterException;

/**
 * Main purpose of this interface is to increase testability of JPAEnumerationBasedOperator
 */
public interface JPAEnumerationBasedOperator extends JPAPrimitiveTypeOperator {

  Number getValue() throws ODataJPAFilterException;

}