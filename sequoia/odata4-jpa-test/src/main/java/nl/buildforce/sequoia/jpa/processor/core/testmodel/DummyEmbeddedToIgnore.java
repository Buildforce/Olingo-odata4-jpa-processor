package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;

import java.sql.Blob;
import java.sql.Clob;

@EdmIgnore
@Embeddable
public class DummyEmbeddedToIgnore {

  @Lob
  @Column(name = "\"Command\"")
  @Basic(fetch = FetchType.LAZY)
  private Clob command;

  @Lob
  @Column(name = "\"LargeBytes\"")
  @Basic(fetch = FetchType.LAZY)
  private Blob largeBytes;

}