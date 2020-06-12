package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunctions;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmIgnore;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmParameter;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entity implementation class for Entity: DummyToBeIgnored
 *
 */
@Entity
@EdmFunctions({
    @EdmFunction(
        name = "IsOdd",
        functionName = "IS_ODD",
        returnType = @EdmFunction.ReturnType(isCollection = true),
        parameter = { @EdmParameter(name = "Number", type = BigDecimal.class, precision = 32, scale = 0) }),

})
@Table(schema = "\"OLINGO\"", name = "\"DummyToBeIgnored\"")
@EdmIgnore
public class DummyToBeIgnored implements Serializable {

  @Id
  private String ID;
  private static final long serialVersionUID = 1L;

  @Convert(converter = ByteConverter.class)
  private byte uuid;

  @EdmIgnore
  @OneToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "\"ID\"", insertable = false, updatable = false)
  private BusinessPartner businessPartner;

  public String getID() {
    return this.ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

}