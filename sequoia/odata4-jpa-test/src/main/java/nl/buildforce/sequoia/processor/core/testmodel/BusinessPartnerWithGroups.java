package nl.buildforce.sequoia.processor.core.testmodel;

import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmAnnotation;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmDescriptionAssociation;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmIgnore;
import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmVisibleFor;
import nl.buildforce.olingo.commons.api.edm.provider.annotation.CsdlConstantExpression.ConstantExpressionType;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.AssociationOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "BusinessPartnerWithGroups")
@Table(schema = "\"OLINGO\"", name = "\"BusinessPartner\"")
public class BusinessPartnerWithGroups implements KeyAccess {

  @Id
  @Column(name = "\"ID\"")
  protected String iD;

  @Version
  @Column(name = "\"ETag\"", nullable = false)
  protected long eTag;

  @Column(name = "\"Type\"", length = 1, insertable = false, updatable = false, nullable = false)
  protected String type;

  @EdmVisibleFor("Company")
  @Column(name = "\"CreatedAt\"", precision = 3, insertable = false, updatable = false)
  private Timestamp creationDateTime;

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

  @EdmVisibleFor("Person")
  @Column(name = "\"Country\"", length = 4)
  private String country;

  @EdmVisibleFor("Company")
  @ElementCollection(fetch = FetchType.LAZY)
  @OrderColumn(name = "\"Order\"")
  @CollectionTable(schema = "\"OLINGO\"", name = "\"Comment\"",
      joinColumns = @JoinColumn(name = "\"BusinessPartnerID\""))
  @Column(name = "\"Text\"")
  private final List<String> comment = new ArrayList<>();

  @EdmAnnotation(term = "Core.IsLanguageDependent", constantExpression = @EdmAnnotation.ConstantExpression(
      type = ConstantExpressionType.Bool, value = "true"))
  @EdmDescriptionAssociation(languageAttribute = "key/language", descriptionAttribute = "name",
      valueAssignments = {
          @EdmDescriptionAssociation.valueAssignment(attribute = "key/codePublisher", value = "ISO"),
          @EdmDescriptionAssociation.valueAssignment(attribute = "key/codeID", value = "3166-1") })
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "\"DivisionCode\"", referencedColumnName = "\"Country\"")
  private Collection<AdministrativeDivisionDescription> locationName;

  @Embedded
  @EdmVisibleFor({ "Company", "Person" })
  protected CommunicationData communicationData;

  @Embedded
  @AssociationOverrides({
      @AssociationOverride(name = "countryName",
          joinColumns = @JoinColumn(referencedColumnName = "\"Address.Country\"", name = "\"ISOCode\"")),
      @AssociationOverride(name = "regionName",
          joinColumns = {
              @JoinColumn(referencedColumnName = "\"Address.RegionCodePublisher\"", name = "\"CodePublisher\""),
              @JoinColumn(referencedColumnName = "\"Address.RegionCodeID\"", name = "\"CodeID\""),
              @JoinColumn(referencedColumnName = "\"Address.Region\"", name = "\"DivisionCode\"") })
  })
  private final PostalAddressDataWithGroup address = new PostalAddressDataWithGroup();

  @Embedded
  private AdministrativeInformation administrativeInformation = new AdministrativeInformation();

  @OneToMany(mappedBy = "businessPartner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private Collection<BusinessPartnerRoleWithGroup> roles;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(schema = "\"OLINGO\"", name = "\"InhouseAddress\"",
      joinColumns = @JoinColumn(name = "\"ID\""))
  private final List<InhouseAddressWithGroup> inhouseAddress = new ArrayList<>();

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BusinessPartnerWithGroups other = (BusinessPartnerWithGroups) obj;
    if (iD == null) {
        return other.iD == null;
    } else return iD.equals(other.iD);
  }

  public AdministrativeInformation getAdministrativeInformation() {
    return administrativeInformation;
  }

  public CommunicationData getCommunicationData() {
    return communicationData;
  }

  public String getCountry() {
    return country;
  }

  public Timestamp getCreationDateTime() {
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

  public String getID() {
    return iD;
  }

  @Override
  public Object getKey() {
    return iD;
  }

  public Collection<BusinessPartnerRoleWithGroup> getRoles() {
    if (roles == null)
      roles = new ArrayList<>();
    return roles;
  }

  public String getType() {
    return type;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((iD == null) ? 0 : iD.hashCode());
    return result;
  }

  public void setAdministrativeInformation(final AdministrativeInformation administrativeInformation) {
    this.administrativeInformation = administrativeInformation;
  }

  public void setCommunicationData(final CommunicationData communicationData) {
    this.communicationData = communicationData;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  public void setCreationDateTime(final Timestamp creationDateTime) {
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

  public void setID(final String iD) {
    this.iD = iD;
  }

  public void setRoles(final Collection<BusinessPartnerRoleWithGroup> roles) {
    this.roles = roles;
  }

  public void setType(final String type) {
    this.type = type;
  }

  @PrePersist
  public void onCreate() {
    administrativeInformation = new AdministrativeInformation();
    ChangeInformation created = new ChangeInformation("99", Date.valueOf(LocalDate.now()));
    administrativeInformation.setCreated(created);
    administrativeInformation.setUpdated(created);
  }

  public Collection<AdministrativeDivisionDescription> getLocationName() {
    return locationName;
  }

}