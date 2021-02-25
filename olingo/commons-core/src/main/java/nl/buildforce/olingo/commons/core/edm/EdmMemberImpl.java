/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.EdmMember;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEnumMember;

public class EdmMemberImpl extends AbstractEdmNamed implements EdmMember {

  private final CsdlEnumMember member;

  public EdmMemberImpl(Edm edm, CsdlEnumMember member) {
    super(edm, member.getName(), member);
    this.member = member;
  }

  @Override
  public String getValue() {
    return member.getValue();
  }
}
