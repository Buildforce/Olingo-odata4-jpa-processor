package nl.buildforce.sequoia.processor.core.testmodel;

import nl.buildforce.sequoia.metadata.core.edm.annotation.EdmAsEntitySet;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

//Problem with multi-level inheritance hierarchy Inheritance Type SINGLE_TABLE. Therefore inherit also from

//Business Partner
@Entity(name = "BestOrganization")
@DiscriminatorValue(value = "2")
@EdmAsEntitySet
public class BestOrganization extends BusinessPartner {}