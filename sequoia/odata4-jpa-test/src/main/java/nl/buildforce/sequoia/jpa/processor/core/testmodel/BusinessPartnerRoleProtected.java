package nl.buildforce.sequoia.jpa.processor.core.testmodel;

import nl.buildforce.sequoia.jpa.metadata.core.edm.annotation.EdmProtectedBy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@IdClass(BusinessPartnerRoleKey.class)
//@ReadOnly
@Entity(name = "BusinessPartnerRoleProtected")
@Table(schema = "\"OLINGO\"", name = "\"BusinessPartnerRole\"")
public class BusinessPartnerRoleProtected {

  @Id
  @Column(name = "\"BusinessPartnerID\"")
  private String businessPartnerID;

  @Id
  @EdmProtectedBy(name = "RoleCategory")
  @Column(name = "\"BusinessPartnerRole\"")
  private String roleCategory;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "\"BusinessPartnerID\"", insertable = false, updatable = false)
  private BusinessPartnerProtected bupaPartnerProtected;

  public BusinessPartnerRoleProtected() {
  }

  public BusinessPartnerRoleProtected(final String businessPartnerID, final String roleCategory) {
    this.setBusinessPartnerID(businessPartnerID);
    this.setRoleCategory(roleCategory);
  }

  public String getBusinessPartnerID() {
    return businessPartnerID;
  }

  public String getRoleCategory() {
    return roleCategory;
  }

  public void setBusinessPartnerID(String businessPartnerID) {
    this.businessPartnerID = businessPartnerID;
  }

  public void setRoleCategory(final String roleCategory) {
    this.roleCategory = roleCategory;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((businessPartnerID == null) ? 0 : businessPartnerID.hashCode());
    result = prime * result + ((roleCategory == null) ? 0 : roleCategory.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BusinessPartnerRoleProtected other = (BusinessPartnerRoleProtected) obj;
    if (businessPartnerID == null) {
      if (other.businessPartnerID != null) return false;
    } else if (!businessPartnerID.equals(other.businessPartnerID)) return false;
    if (roleCategory == null) {
        return other.roleCategory == null;
    } else return roleCategory.equals(other.roleCategory);
  }

}