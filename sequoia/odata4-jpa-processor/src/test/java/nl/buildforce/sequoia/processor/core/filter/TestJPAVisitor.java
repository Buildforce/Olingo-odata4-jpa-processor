package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPADataBaseFunction;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAServiceDocument;
// import nl.buildforce.sequoia.processor.core.api.JPAServiceDebugger;
import nl.buildforce.sequoia.processor.core.database.JPAODataDatabaseOperations;
import nl.buildforce.sequoia.processor.core.query.JPAAbstractQuery;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceFunction;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestJPAVisitor {

  private JPAFilterCompilerAccess compiler;
  private JPAExpressionVisitor cut;

  @BeforeEach
  public void setUp() {
    JPAODataDatabaseOperations extension = mock(JPAODataDatabaseOperations.class);
    JPAOperationConverter converter = new JPAOperationConverter(mock(CriteriaBuilder.class), extension);
    compiler = mock(JPAFilterCompilerAccess.class);
    JPAAbstractQuery query = mock(JPAAbstractQuery.class);

    when(compiler.getConverter()).thenReturn(converter);
    when(compiler.getParent()).thenReturn(query);
//    when(compiler.getDebugger()).thenReturn(mock(JPAServiceDebugger.class));
//    when(query.getDebugger()).thenReturn(mock(JPAServiceDebugger.class));

    cut = new JPAVisitor(compiler);
  }

//return new JPAFunctionOperator(jpaFunction, odataParams, this.jpaCompiler.getParent().getRoot(), jpaCompiler.getConverter().cb);

  @Test
  public void createFunctionOperation() throws ODataApplicationException {

//  final UriResource resource = member.getResourcePath().getUriResourceParts().get(0);
    Member member = mock(Member.class);
    UriInfoResource info = mock(UriInfoResource.class);
    UriResourceFunction uriFunction = mock(UriResourceFunction.class);

    List<UriResource> resources = new ArrayList<>();
    resources.add(uriFunction);

    when(member.getResourcePath()).thenReturn(info);
    when(info.getUriResourceParts()).thenReturn(resources);
//  final JPAFunction jpaFunction = this.jpaCompiler.getSd().getFunction(((UriResourceFunction) resource).getFunction());
    JPAServiceDocument sd = mock(JPAServiceDocument.class);
    JPADataBaseFunction jpaFunction = mock(JPADataBaseFunction.class);
    EdmFunction edmFunction = mock(EdmFunction.class);

    when(uriFunction.getFunction()).thenReturn(edmFunction);
    when(compiler.getSd()).thenReturn(sd);
    when(sd.getFunction(edmFunction)).thenReturn(jpaFunction);
    when(uriFunction.getParameters()).thenReturn(new ArrayList<>());

    if (!(cut.visitMember(member) instanceof JPAFunctionOperator)) {
      fail();
    }
  }

}