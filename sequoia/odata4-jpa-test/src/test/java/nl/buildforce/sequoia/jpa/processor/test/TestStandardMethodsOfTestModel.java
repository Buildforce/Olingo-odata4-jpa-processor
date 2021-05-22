package nl.buildforce.sequoia.jpa.processor.test;

import nl.buildforce.sequoia.processor.core.testmodel.AdministrativeDivision;
import nl.buildforce.sequoia.processor.core.testmodel.AdministrativeDivisionDescription;
import nl.buildforce.sequoia.processor.core.testmodel.AdministrativeDivisionDescriptionKey;
import nl.buildforce.sequoia.processor.core.testmodel.AdministrativeDivisionKey;
import nl.buildforce.sequoia.processor.core.testmodel.AdministrativeInformation;
import nl.buildforce.sequoia.processor.core.testmodel.BusinessPartnerProtected;
import nl.buildforce.sequoia.processor.core.testmodel.BusinessPartnerRole;
import nl.buildforce.sequoia.processor.core.testmodel.BusinessPartnerRoleKey;
import nl.buildforce.sequoia.processor.core.testmodel.BusinessPartnerRoleProtected;
import nl.buildforce.sequoia.processor.core.testmodel.BusinessPartnerRoleWithGroup;
import nl.buildforce.sequoia.processor.core.testmodel.BusinessPartnerWithGroups;
import nl.buildforce.sequoia.processor.core.testmodel.ChangeInformation;
import nl.buildforce.sequoia.processor.core.testmodel.CollectionInnerComplex;
import nl.buildforce.sequoia.processor.core.testmodel.CollectionNestedComplex;
import nl.buildforce.sequoia.processor.core.testmodel.Collection;
import nl.buildforce.sequoia.processor.core.testmodel.CollectionDeep;
import nl.buildforce.sequoia.processor.core.testmodel.CollectionFirstLevelComplex;
import nl.buildforce.sequoia.processor.core.testmodel.CollectionPartOfComplex;
import nl.buildforce.sequoia.processor.core.testmodel.CollectionSecondLevelComplex;
import nl.buildforce.sequoia.processor.core.testmodel.Comment;
import nl.buildforce.sequoia.processor.core.testmodel.CommunicationData;
import nl.buildforce.sequoia.processor.core.testmodel.CountryKey;
import nl.buildforce.sequoia.processor.core.testmodel.CountryRestriction;
import nl.buildforce.sequoia.processor.core.testmodel.InhouseAddress;
import nl.buildforce.sequoia.processor.core.testmodel.InhouseAddressTable;
import nl.buildforce.sequoia.processor.core.testmodel.InhouseAddressWithGroup;
import nl.buildforce.sequoia.processor.core.testmodel.InhouseAddressWithProtection;
import nl.buildforce.sequoia.processor.core.testmodel.InhouseAddressWithThreeProtections;
import nl.buildforce.sequoia.processor.core.testmodel.InstanceRestrictionKey;
import nl.buildforce.sequoia.processor.core.testmodel.JoinRelationKey;
import nl.buildforce.sequoia.processor.core.testmodel.MembershipKey;
import nl.buildforce.sequoia.processor.core.testmodel.Organization;
import nl.buildforce.sequoia.processor.core.testmodel.OrganizationImage;
import nl.buildforce.sequoia.processor.core.testmodel.Person;
import nl.buildforce.sequoia.processor.core.testmodel.PersonDeepProtected;
import nl.buildforce.sequoia.processor.core.testmodel.PersonDeepProtectedHidden;
import nl.buildforce.sequoia.processor.core.testmodel.PersonImage;
import nl.buildforce.sequoia.processor.core.testmodel.PostalAddressData;
import nl.buildforce.sequoia.processor.core.testmodel.PostalAddressDataWithGroup;
import nl.buildforce.sequoia.processor.core.testmodel.User;

import nl.buildforce.sequoia.processor.core.errormodel.CollectionAttributeProtected;
import nl.buildforce.sequoia.processor.core.errormodel.ComplexProtectedNoPath;
import nl.buildforce.sequoia.processor.core.errormodel.ComplexProtectedWrongPath;
import nl.buildforce.sequoia.processor.core.errormodel.EmbeddedKeyPartOfGroup;
import nl.buildforce.sequoia.processor.core.errormodel.KeyPartOfGroup;
import nl.buildforce.sequoia.processor.core.errormodel.MandatoryPartOfGroup;
import nl.buildforce.sequoia.processor.core.errormodel.NavigationAttributeProtected;
import nl.buildforce.sequoia.processor.core.errormodel.NavigationPropertyPartOfGroup;
import nl.buildforce.sequoia.processor.core.errormodel.PersonDeepCollectionProtected;
import nl.buildforce.sequoia.processor.core.errormodel.Team;

import org.hsqldb.jdbc.JDBCClob;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * The following set of test methods checks a number of standard methods of model pojos.
 *
 */
public class TestStandardMethodsOfTestModel {
  private final Boolean expBoolean = Boolean.TRUE;
  private final BigInteger expBigInt = new BigInteger("10");
  private final BigDecimal expDecimal = new BigDecimal("1.10");
  private final LocalDate expLocalDate = LocalDate.now();
  private final byte[] expByteArray = new byte[] { 1, 1, 1, 1 };
  private final Date expDate = Date.valueOf(expLocalDate);
  @SuppressWarnings("deprecation")
  private final java.util.Date expUtilDate = new java.util.Date(119, 10, 1);
  private final Timestamp expTimestamp = Timestamp.valueOf(LocalDateTime.now());
  private final Short expShort = Short.valueOf("10");
  private Clob expClob;

  static Stream<Arguments> testModelEntities() {
    return Stream.of(
        arguments(AdministrativeDivision.class),
        arguments(AdministrativeDivisionDescription.class),
        arguments(AdministrativeDivisionDescriptionKey.class),
        arguments(AdministrativeDivisionKey.class),
        arguments(AdministrativeInformation.class),
        arguments(BusinessPartnerProtected.class),
        arguments(BusinessPartnerRole.class),
        arguments(BusinessPartnerRoleKey.class),
        arguments(BusinessPartnerRoleProtected.class),
        arguments(BusinessPartnerRoleWithGroup.class),
        arguments(BusinessPartnerWithGroups.class),
        arguments(ChangeInformation.class),
        arguments(Collection.class),
        arguments(CollectionDeep.class),
        arguments(CollectionFirstLevelComplex.class),
        arguments(CollectionInnerComplex.class),
        arguments(CollectionNestedComplex.class),
        arguments(CollectionPartOfComplex.class),
        arguments(CollectionSecondLevelComplex.class),
        arguments(Comment.class),
        arguments(CommunicationData.class),
        arguments(CountryKey.class),
        arguments(CountryRestriction.class),
        arguments(InhouseAddress.class),
        arguments(InhouseAddressTable.class),
        arguments(InhouseAddressWithGroup.class),
        arguments(InhouseAddressWithProtection.class),
        arguments(InhouseAddressWithThreeProtections.class),
        arguments(InstanceRestrictionKey.class),
        arguments(JoinRelationKey.class),
        arguments(MembershipKey.class),
        arguments(Organization.class),
        arguments(OrganizationImage.class),
        arguments(Person.class),
        arguments(PersonDeepProtected.class),
        arguments(PersonDeepProtectedHidden.class),
        arguments(PersonImage.class),
        arguments(PostalAddressData.class),
        arguments(PostalAddressDataWithGroup.class),
        arguments(User.class));
  }

  static Stream<Arguments> testErrorEntities() {
    return Stream.of(
        arguments(AdministrativeInformation.class),
        arguments(ChangeInformation.class),
        arguments(CollectionAttributeProtected.class),
        arguments(ComplexProtectedNoPath.class),
        arguments(ComplexProtectedWrongPath.class),
        arguments(EmbeddedKeyPartOfGroup.class),
        arguments(KeyPartOfGroup.class),
        arguments(MandatoryPartOfGroup.class),
        arguments(NavigationAttributeProtected.class),
        arguments(NavigationPropertyPartOfGroup.class),
        arguments(PersonDeepCollectionProtected.class),
        arguments(Team.class));
  }

  @BeforeEach
  public void setup() throws SQLException {
    expClob = new JDBCClob("Test");
  }

  @ParameterizedTest
  @MethodSource({ "testModelEntities", "testErrorEntities" })
  public void testGetterReturnsSetPrimitiveValue(final Class<?> clazz) throws NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    final Method[] methods = clazz.getMethods();
    final Constructor<?> constructor = clazz.getConstructor();
    assertNotNull(constructor);
    final Object instance = constructor.newInstance();

    for (final Method setter : methods) {
      if ("set".equals(setter.getName().substring(0, 3))
          && setter.getParameterCount() == 1) {

        final String getterName = "g" + setter.getName().substring(1);
        assertNotNull(clazz.getMethod(getterName));
        final Method getter = clazz.getMethod(getterName);
        final Class<?> paramType = setter.getParameterTypes()[0];
        final Object exp = getExpected(paramType);
        if (exp != null) {
          setter.invoke(instance, exp);
          assertEquals(exp, getter.invoke(instance));
        }
      }
    }
  }

  @ParameterizedTest
  @MethodSource({ "testModelEntities", "testErrorEntities" })
  public void testHasValueReturnsValue(final Class<?> clazz) throws NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    final Method[] methods = clazz.getMethods();
    final Constructor<?> constructor = clazz.getConstructor();
    final Object instance = constructor.newInstance();

    for (final Method hashcode : methods) {
      if ("hashCode".equals(hashcode.getName()) && hashcode.getParameterCount() == 0) {
        assertNotEquals(0, hashcode.invoke(instance));
      }
    }
  }

  private Object getExpected(final Class<?> paramType) {
    if (paramType == String.class) return "TestString";
    else if (paramType == BigDecimal.class) return expDecimal;
    else if (paramType == BigInteger.class) return expBigInt;
    else if (paramType == Boolean.class) return expBoolean;
    else if (paramType == Clob.class) return expClob;
    else if (paramType == Date.class) return expDate;
    else if (paramType == expByteArray.getClass()) return expByteArray;
    else if (paramType == expUtilDate.getClass()) return expUtilDate;
    else if (paramType == int.class) return 10;
    else if (paramType == Integer.class) return Integer.valueOf(20);
    else if (paramType == LocalDate.class) return expLocalDate;
    else if (paramType == Long.class) return Long.valueOf(15L);
    else if (paramType == long.class) return 15L;
    else if (paramType == Short.class) return expShort;
    else if (paramType == short.class) return expShort;
    else if (paramType == Timestamp.class) return expTimestamp;
    else return null;
  }

}