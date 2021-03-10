/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.Edm;
import nl.buildforce.olingo.commons.api.edm.constants.ODataServiceVersion;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlEdmProvider;
import nl.buildforce.olingo.commons.api.edmx.EdmxReference;
import nl.buildforce.olingo.commons.core.edm.EdmProviderImpl;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.etag.ServiceMetadataETagSupport;

/**
 */
public class ServiceMetadataImpl implements ServiceMetadata {

  private final Edm edm;
  private final List<EdmxReference> references;
  private final ServiceMetadataETagSupport serviceMetadataETagSupport;

  public ServiceMetadataImpl(CsdlEdmProvider edmProvider, List<EdmxReference> references,
                             ServiceMetadataETagSupport serviceMetadataETagSupport) {
    edm = new EdmProviderImpl(edmProvider);
    this.references = new ArrayList<>();
    this.references.addAll(references);
    this.serviceMetadataETagSupport = serviceMetadataETagSupport;
  }

  @Override
  public Edm getEdm() {
    return edm;
  }

/*
  @Override
  public ODataServiceVersion getDataServiceVersion() {
    return ODataServiceVersion.V40;
  }
*/

  @Override
  public List<EdmxReference> getReferences() {
    return Collections.unmodifiableList(references);
  }

  @Override
  public ServiceMetadataETagSupport getServiceMetadataETagSupport() {
    return serviceMetadataETagSupport;
  }

}