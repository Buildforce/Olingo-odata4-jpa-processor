package nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception;

public interface ODataJPAMessageBufferRead {

  String getText(Object exception, String ID);

  String getText(Object exception, String ID, String... parameters);

}