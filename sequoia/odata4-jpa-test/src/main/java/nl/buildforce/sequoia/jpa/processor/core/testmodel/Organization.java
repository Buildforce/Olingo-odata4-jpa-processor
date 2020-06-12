package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmFunction.ReturnType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmParameter;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.List;

@EdmFunction(
    name = "AllCustomersByABC",
    functionName = "CUSTOMER_BY_ABC",
    returnType = @ReturnType(type = Organization.class, isCollection = true),
    parameter = { @EdmParameter(name = "Class", type = Character.class) })

@Entity(name = "Organization")
@DiscriminatorValue(value = "2")
public class Organization extends BusinessPartner {

  public Organization() {
    type = "2";
  }

  public Organization(final String id) {
    iD = id;
    type = "2";
  }

  @Column(name = "\"NameLine1\"")
  private String name1;

  @Column(name = "\"NameLine2\"")
  private String name2;

  @ElementCollection(fetch = FetchType.LAZY)
  @OrderColumn(name = "\"Order\"")
  @CollectionTable(schema = "\"OLINGO\"", name = "\"Comment\"",
      joinColumns = @JoinColumn(name = "\"BusinessPartnerID\""))
  @Column(name = "\"Text\"")
  private List<String> comment = new ArrayList<>();

  @Enumerated
  @Column(name = "\"ABCClass\"")
  private AbcClassification aBCClass;

  @ManyToMany(mappedBy = "supportedOrganizations")
  private List<Person> supportEngineers;

  public String getName1() {
    return name1;
  }

  public void setName1(String name1) {
    this.name1 = name1;
  }

  public String getName2() {
    return name2;
  }

  public void setName2(String name2) {
    this.name2 = name2;
  }

  public AbcClassification getABCClass() {
    return aBCClass;
  }

  public void setABCClass(AbcClassification aBCClass) {
    this.aBCClass = aBCClass;
  }

  public List<String> getComment() {
    return comment;
  }

  public void setComment(List<String> comment) {
    this.comment = comment;
  }

}