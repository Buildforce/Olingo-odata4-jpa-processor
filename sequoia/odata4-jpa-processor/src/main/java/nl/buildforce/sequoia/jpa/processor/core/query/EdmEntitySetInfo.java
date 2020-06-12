package nl.buildforce.sequoia.jpa.processor.core.query;

import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.server.api.uri.UriParameter;

import java.util.List;

public interface EdmEntitySetInfo {

  EdmEntitySet getEdmEntitySet();

  List<UriParameter> getKeyPredicates();

  String getName();

  String getNavigationPath();

  EdmEntitySet getTargetEdmEntitySet();
}