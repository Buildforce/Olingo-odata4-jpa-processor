package nl.buildforce.sequoia.processor.core.testmodel;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;

import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmAnnotation;
//import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmDescriptionAssociation;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunction;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmFunctions;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmIgnore;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmParameter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Inheritance
@DiscriminatorColumn(name = "\"Type\"")
@Entity(name = "BusinessPartner")
@Table(schema = "\"OLINGO\"", name = "\"BusinessPartner\"")
@EdmFunctions({
    @EdmFunction(
        name = "CountRoles",
        functionName = "COUNT_ROLES",
        returnType = @EdmFunction.ReturnType(isCollection = true),
        parameter = { @EdmParameter(name = "Amount", parameterName = "a", type = String.class)}),

    @EdmFunction(
        name = "max",
        functionName = "MAX",
        isBound = false,
        hasFunctionImport = false,
        returnType = @EdmFunction.ReturnType(type = BigDecimal.class, isCollection = false),
        parameter = { @EdmParameter(name = "Path", parameterName = "path", type = String.class)}),

    @EdmFunction(
        name = "IsPrime",
        functionName = "IS_PRIME",
        isBound = false,
        hasFunctionImport = true,
        returnType = @EdmFunction.ReturnType(type = Boolean.class, isNullable = false),
        parameter = { @EdmParameter(name = "Number", type = BigDecimal.class, precision = 32, scale = 0) })})

public abstract class BusinessPartner implements KeyAccess {
  @Id
  @Column(name = "\"ID\"")
  protected String iD;

  @Version
  @Column(name = "\"ETag\"", nullable = false)
  protected long eTag;

  @Column(name = "\"Type\"", length = 1, insertable = false, updatable = false, nullable = false)
  protected String type;

  @Column(name = "\"CreatedAt\"", precision = 3, insertable = false, updatable = false)
  @Convert(converter = DateTimeConverter.class)
  private LocalDateTime creationDateTime;

  @EdmIgnore
  @Column(name = "\"CustomString1\"")
  @Convert(converter = StringConverter.class)
  protected String customString1;

  @EdmIgnore
  @Column(name = "\"CustomString2\"")
  protected String customString2;

  @EdmIgnore
  @Column(name = "\"CustomNum1\"", precision = 16, scale = 5)
  protected BigDecimal customNum1;

  @EdmIgnore
  @Column(name = "\"CustomNum2\"", precision = 34)
  protected BigDecimal customNum2;

  @Column(name = "\"Country\"", length = 4)
  private String country;

  @EdmAnnotation(term = "Core.IsLanguageDependent",
          constantExpression = @EdmAnnotation.ConstantExpression(type = ConstantExpressionType.Bool, value = "true"))
/*
  @EdmDescriptionAssociation(languageAttribute = "key/language", descriptionAttribute = "name",
      valueAssignments = {
          @EdmDescriptionAssociation.valueAssignment(attribute = "key/codePublisher", value = "ISO"),
          @EdmDescriptionAssociation.valueAssignment(attribute = "key/codeID", value = "3166-1") })
*/

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "\"DivisionCode\"", referencedColumnName = "\"Country\"")
  private Collection<AdministrativeDivisionDescription> locationName;

  @Embedded
  protected CommunicationData communicationData;

  @Embedded
  @AssociationOverride(name = "countryName",
      joinColumns = @JoinColumn(referencedColumnName = "\"Address.Country\"", name = "\"ISOCode\""))
  @AssociationOverride(name = "regionName",
      joinColumns = {
          @JoinColumn(referencedColumnName = "\"Address.RegionCodePublisher\"", name = "\"CodePublisher\""),
          @JoinColumn(referencedColumnName = "\"Address.RegionCodeID\"", name = "\"CodeID\""),
          @JoinColumn(referencedColumnName = "\"Address.Region\"", name = "\"DivisionCode\"") })

  private PostalAddressData address = new PostalAddressData();

  @Embedded
  private AdministrativeInformation administrativeInformation = new AdministrativeInformation();

  @OneToMany(mappedBy = "businessPartner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private Collection<BusinessPartnerRole> roles;

  public BusinessPartner() {
  }

  public String getID() {
    return iD;
  }

  public void setID(final String iD) {
    this.iD = iD;
  }

  public PostalAddressData getAddress() {
    return address;
  }

  public void setAddress(final PostalAddressData address) {
    this.address = address;
  }

  public AdministrativeInformation getAdministrativeInformation() {
    return administrativeInformation;
  }

  public void setAdministrativeInformation(final AdministrativeInformation administrativeInformation) {
    this.administrativeInformation = administrativeInformation;
  }

  public CommunicationData getCommunicationData() {
    return communicationData;
  }

  public void setCommunicationData(final CommunicationData communicationData) {
    this.communicationData = communicationData;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BusinessPartner other = (BusinessPartner) obj;
    if (iD == null) {
        return other.iD == null;
    } else return iD.equals(other.iD);
  }

  public Collection<BusinessPartnerRole> getRoles() {
    if (roles == null)
      roles = new ArrayList<>();
    return roles;
  }

  public LocalDateTime getCreationDateTime() {
    return creationDateTime;
  }

  public BigDecimal getCustomNum1() {
    return customNum1;
  }

  public BigDecimal getCustomNum2() {
    return customNum2;
  }

  public String getCustomString1() {
    return customString1;
  }

  public String getCustomString2() {
    return customString2;
  }

  public long getETag() {
    return eTag;
  }

  public void setCreationDateTime(final LocalDateTime creationDateTime) {
    this.creationDateTime = creationDateTime;
  }

  public void setCustomNum1(final BigDecimal customNum1) {
    this.customNum1 = customNum1;
  }

  public void setCustomNum2(final BigDecimal customNum2) {
    this.customNum2 = customNum2;
  }

  public void setCustomString1(final String customString1) {
    this.customString1 = customString1;
  }

  public void setCustomString2(final String customString2) {
    this.customString2 = customString2;
  }

  public void setETag(final long eTag) {
    this.eTag = eTag;
  }

/*
  public void setRoles(final Collection<BusinessPartnerRole> roles) {
    this.roles = roles;
  }
*/

  public Collection<AdministrativeDivisionDescription> getLocationName() {
    return locationName;
  }

  public void setLocationName(final Collection<AdministrativeDivisionDescription> locationName) {
    this.locationName = locationName;
  }

  @PrePersist
  public void onCreate() {
    administrativeInformation = new AdministrativeInformation();
    ChangeInformation created = new ChangeInformation("99", Date.valueOf(LocalDate.now()));
    administrativeInformation.setCreated(created);
    administrativeInformation.setUpdated(created);
  }

  @Override
  public Object getKey() {
    return iD;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((iD == null) ? 0 : iD.hashCode());
    return result;
  }

}