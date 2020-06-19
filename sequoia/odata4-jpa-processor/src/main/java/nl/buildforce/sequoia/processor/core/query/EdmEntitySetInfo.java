package nl.buildforce.sequoia.processor.core.query;

import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.server.api.uri.UriParameter;

import java.util.List;

public interface EdmEntitySetInfo {

  EdmEntitySet getEdmEntitySet();

  List<UriParameter> getKeyPredicates();

  String getName();

  String getNavigationPath();

  EdmEntitySet getTargetEdmEntitySet();
}