/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
 */
package nl.buildforce.olingo.commons.api.edm.constants;

import java.util.regex.Pattern;

/**
 * This class is a container for the supported ODataServiceVersions.
 */
public enum ODataServiceVersion {
  /**
   * OData Version 1.0
   */
  V10("1.0"),
  /**
   * OData Version 2.0
   */
  V20("2.0"),
  /**
   * OData Version 3.0
   */
  V30("3.0"),
  /**
   * OData Version 4.0
   */
  V40("4.0"),
  /**
   * OData Version 4.01
   */
  V401("4.01");


  private static final Pattern DATASERVICEVERSIONPATTERN = Pattern.compile("(\\p{Digit}+\\.\\p{Digit}+)(:?;.*)?");

// --Commented out by Inspection START (''21-03-07 11:09):
//  /**
//   * Validates format and range of a data service version string.
//   *
//   * @param version version string
//   * @return <code>true</code> for a valid version
//   */
//  public static boolean validateDataServiceVersion(String version) {
//    Matcher matcher = DATASERVICEVERSIONPATTERN.matcher(version);
//    if (matcher.matches()) {
//      String possibleDataServiceVersion = matcher.group(1);
//      return (V10.toString().equals(possibleDataServiceVersion)
//          || V20.toString().equals(possibleDataServiceVersion)
//          || V30.toString().equals(possibleDataServiceVersion)
//          || V40.toString().equals(possibleDataServiceVersion)
//          || V401.toString().equals(possibleDataServiceVersion));
//    } else {
//      throw new IllegalArgumentException(version);
//    }
//  }
// --Commented out by Inspection STOP (''21-03-07 11:09)

// --Commented out by Inspection START (''21-03-07 11:00):
//  /**
//   * Check if <code>firstValue</code> is bigger then  <code>secondValue</code>
//   *
//   * @param firstValue first value which is compared
//   * @param secondValue second value which is compared
//   * @return <code>true</code> if firstValue is bigger than secondValue
//   */
//  public static boolean isBiggerThan(String firstValue, String secondValue) {
//    if (!validateDataServiceVersion(secondValue) || !validateDataServiceVersion(firstValue)) {
//      throw new IllegalArgumentException("Illegal arguments: " + secondValue + " and " + firstValue);
//    }
//
//    double me = Double.parseDouble(extractDataServiceVersionString(firstValue));
//    double other = Double.parseDouble(extractDataServiceVersionString(secondValue));
//
//    return me > other;
//  }
// --Commented out by Inspection STOP (''21-03-07 11:00)

  public static boolean isValidODataVersion(String value) {
    double version4 = Double.parseDouble(extractDataServiceVersionString(ODataServiceVersion.V40.toString()));
    double version401 = Double.parseDouble(extractDataServiceVersionString(ODataServiceVersion.V401.toString()));
    double other = Double.parseDouble(extractDataServiceVersionString(value));
    
    return (Double.compare(other, version4) == 0) || (Double.compare(other, version401) == 0);
  }
  
  public static boolean isValidMaxODataVersion(String value) {
    double version4 = Double.parseDouble(extractDataServiceVersionString(ODataServiceVersion.V40.toString()));
    double other = Double.parseDouble(extractDataServiceVersionString(value));
    
    return other >= version4;
  }
  
  /**
   * Extract data service version and return it.
   *
   * @param rawDataServiceVersion raw data service version from which the service version gets extracted
   * @return the extracted data service version
   */
  private static String extractDataServiceVersionString(String rawDataServiceVersion) {
    if (rawDataServiceVersion != null) {
      String[] pattern = rawDataServiceVersion.split(";");
      return pattern[0];
    }

    return null;
  }

  private final String version;

  ODataServiceVersion(String version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return version;
  }

}