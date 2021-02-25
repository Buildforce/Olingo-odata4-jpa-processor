/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.edm.provider.annotation;

public class CsdlConstantExpression extends CsdlExpression {

  private final ConstantExpressionType type;
  private String value;

  /**
   * Type of the constant expression
   */
  public enum ConstantExpressionType {
    /**
     * Type Edm.binary
     */
    Binary,
    /**
     * Type Edm.Bool
     */
    Bool,
    /**
     * Type Edm.Date
     */
    Date,
    /**
     * Type Edm.DateTimeOffset
     */
    DateTimeOffset,
    /**
     * Type Edm.Decimal
     */
    Decimal,
    /**
     * Type Edm.Duration
     */
    Duration,
    /**
     * Type Edm.EnumMeber
     */
    EnumMember,
    /**
     * Type Edm.Float
     */
    Float,
    /**
     * Type Edm.GUID
     */
    Guid,
    /**
     * Type Integer
     */
    Int,
    /**
     * Type Edm.String
     */
    String,
    /**
     * Type Edm.TimeOfDay
     */
    TimeOfDay;

    /**
     * Creates a new type by a given string e.g. "TimeOfDay". 
     * Will NOT throw an IlligalArgumentException for invalid types. If needed use the valueOf method.
     * @param value Type as string
     * @return Type type
     */
    public static ConstantExpressionType fromString(String value) {
      ConstantExpressionType result = null;
      try {
        result = valueOf(value);
      } catch (IllegalArgumentException e) {
        // ignore
      }
      return result;
    }
  }

  public CsdlConstantExpression(ConstantExpressionType type) {
    this.type = type;
  }

  public CsdlConstantExpression(ConstantExpressionType type, String value) {
    this.type = type;
    this.value = value;
  }

  /**
   * Returns the type of the constant expression
   * @return type of the constant expression
   */
  public ConstantExpressionType getType() {
    return type;
  }

  /**
   * Value of the constant expression
   * @return value of the constant expression as String
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value of the constant expression
   * @param value value of the constant expression
   * @return this for method chaining
   */
  public CsdlConstantExpression setValue(String value) {
    this.value = value;
    return this;
  }

  @Override
  public boolean equals (Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CsdlConstantExpression)) {
      return false;
    }
    CsdlConstantExpression csdlConstExp = (CsdlConstantExpression) obj;
    
    return (getValue() == null ? csdlConstExp.getValue() == null :
            getValue().equals(csdlConstExp.getValue()))
        && (getType() == null ? csdlConstExp.getType() == null :
            getType().equals(csdlConstExp.getType()));
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

}