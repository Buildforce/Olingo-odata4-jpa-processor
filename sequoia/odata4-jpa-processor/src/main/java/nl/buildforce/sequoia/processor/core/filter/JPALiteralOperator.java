package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAttribute;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAOperationResultParameter;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAParameter;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAFilterException;
import nl.buildforce.sequoia.processor.core.query.ExpressionUtil;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmDateTimeOffset;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Literal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;

public class JPALiteralOperator implements JPAPrimitiveTypeOperator {
  private final Literal literal;
  private final OData odata;
  private final String literalText;

  public JPALiteralOperator(final OData odata, final Literal literal) {
    this(odata, literal, literal.getText());

  }

  private JPALiteralOperator(OData odata, Literal literal, String literalText) {
    this.literal = literal;
    this.odata = odata;
    this.literalText = literalText;
  }

  /*
   * (non-Javadoc)
   *
   * @see nl.buildforce.sequoia.processor.core.filter.JPAPrimitiveTypeOperator#get()
   */
  @Override
  public Object get() throws ODataApplicationException {
    final EdmPrimitiveType edmType = ((EdmPrimitiveType) literal.getType());

    try {
      if (edmType instanceof EdmDateTimeOffset) {
        return OffsetDateTime.parse(literal.getText());
      } else {
        final Class<?> defaultType = edmType.getDefaultType();
        final Constructor<?> c = defaultType.getConstructor(String.class);
        return c.newInstance(edmType.fromUriLiteral(literalText));
      }
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
            | EdmPrimitiveTypeException | InstantiationException | NoSuchMethodException | SecurityException e) {
      throw new ODataJPAFilterException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Converts a literal value into system type of attribute
   */
  public Object get(final JPAAttribute attribute) throws ODataApplicationException {
    return ExpressionUtil.convertValueOnAttribute(odata, attribute, literalText);
  }

  public Object get(JPAOperationResultParameter returnType) throws ODataApplicationException {
    return ExpressionUtil.convertValueOnFacet(odata, returnType, literalText);
  }

  public Object get(JPAParameter jpaParameter) throws ODataApplicationException {

    return ExpressionUtil.convertValueOnFacet(odata, jpaParameter, literalText);
  }

  @Override
  public boolean isNull() {
    return literal.getText().equals("null");
  }

  JPALiteralOperator clone(String prefix, String postfix) {
    return new JPALiteralOperator(odata, literal, "'" + prefix + literal.getText().replaceAll("'", "") + postfix + "'");
  }

  Literal getLiteral() {
    return literal;
  }

  @Override
  public String getName() {
    return literal.getText();
  }

}