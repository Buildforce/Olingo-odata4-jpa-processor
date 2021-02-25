/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import nl.buildforce.olingo.commons.api.Constants;
import nl.buildforce.olingo.commons.api.edm.EdmEntityContainer;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmFunctionImport;
import nl.buildforce.olingo.commons.api.edm.EdmSingleton;
import nl.buildforce.olingo.commons.api.edmx.EdmxReference;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;
import nl.buildforce.olingo.server.api.serializer.SerializerException;

public class ServiceDocumentXmlSerializer {
  private static final String APP = "app";
  private static final String NS_APP = "https://www.w3.org/2007/app";
  private static final String ATOM = "atom";
  private static final String NS_ATOM = Constants.NS_ATOM;
  private static final String METADATA = "metadata";
  private static final String NS_METADATA = Constants.NS_METADATA;

  private final ServiceMetadata metadata;
  private final String serviceRoot;

  public ServiceDocumentXmlSerializer(ServiceMetadata metadata, String serviceRoot)
      throws SerializerException {
    if (metadata == null || metadata.getEdm() == null) {
      throw new SerializerException("Service Metadata and EDM must not be null for a service.",
          SerializerException.MessageKeys.NULL_METADATA_OR_EDM);
    }
    this.metadata = metadata;
    this.serviceRoot = serviceRoot;
  }

  public void writeServiceDocument(XMLStreamWriter writer) throws XMLStreamException {
    String metadataUri =
        (serviceRoot == null ? "" : serviceRoot.endsWith("/") ? serviceRoot : (serviceRoot + "/"))
            + Constants.METADATA;

    writer.writeStartDocument(ODataSerializer.DEFAULT_CHARSET, "1.0");
    writer.writeStartElement(APP, "service", NS_APP);
    writer.writeNamespace(ATOM, NS_ATOM);
    writer.writeNamespace(APP, NS_APP);
    writer.writeNamespace(METADATA, NS_METADATA);
    writer.writeAttribute(METADATA, NS_METADATA, Constants.CONTEXT, metadataUri);

    if (metadata != null
        && metadata.getServiceMetadataETagSupport() != null
        && metadata.getServiceMetadataETagSupport().getMetadataETag() != null) {
      writer.writeAttribute(METADATA, NS_METADATA, Constants.ATOM_ATTR_METADATAETAG,
          metadata.getServiceMetadataETagSupport().getMetadataETag());
    }

    writer.writeStartElement(APP, "workspace", NS_APP);

    EdmEntityContainer container = metadata.getEdm().getEntityContainer();
    if (container != null) {
      writer.writeStartElement(ATOM, Constants.ATOM_ELEM_TITLE, NS_ATOM);
      writer.writeCharacters(container.getFullQualifiedName().getFullQualifiedNameAsString());
      writer.writeEndElement();

      writeEntitySets(writer, container);
      writeFunctionImports(writer, container);
      writeSingletons(writer, container);
      writeServiceDocuments(writer);
    }
    writer.writeEndElement(); // end workspace
    writer.writeEndElement(); // end service
  }

  private void writeServiceDocuments(XMLStreamWriter writer) throws XMLStreamException {
    for (EdmxReference reference : metadata.getReferences()) {
      String referenceString = reference.getUri().toASCIIString();
      writeElement(writer, false, "service-document", referenceString, referenceString);
    }
  }

  private void writeEntitySets(XMLStreamWriter writer, EdmEntityContainer container)
      throws XMLStreamException {
    for (EdmEntitySet edmEntitySet : container.getEntitySets()) {
      if (edmEntitySet.isIncludeInServiceDocument()) {
        writeElement(writer, true, "collection", edmEntitySet.getName(), edmEntitySet.getTitle());
      }
    }
  }

  private void writeFunctionImports(XMLStreamWriter writer, EdmEntityContainer container)
      throws XMLStreamException {
    for (EdmFunctionImport edmFunctionImport : container.getFunctionImports()) {
      if (edmFunctionImport.isIncludeInServiceDocument()) {
        writeElement(writer, false, "function-import", edmFunctionImport.getName(), edmFunctionImport.getTitle());
      }
    }
  }

  private void writeSingletons(XMLStreamWriter writer, EdmEntityContainer container)
      throws XMLStreamException {
    for (EdmSingleton edmSingleton : container.getSingletons()) {
      writeElement(writer, false, "singleton", edmSingleton.getName(), edmSingleton.getTitle());
    }
  }

  private void writeElement(XMLStreamWriter writer, boolean isApp, String kind, String name,
                            String title) throws XMLStreamException {
    if (isApp) {
      writer.writeStartElement(APP, kind, NS_APP);
    } else {
      writer.writeStartElement(METADATA, kind, NS_METADATA);
    }
    writer.writeAttribute(Constants.ATTR_HREF, name);
    writer.writeAttribute(METADATA, NS_METADATA, Constants.ATTR_NAME, name);
    writer.writeStartElement(ATOM, Constants.ATOM_ELEM_TITLE, NS_ATOM);
    if (title != null) {
      writer.writeCharacters(title);
    } else {
      writer.writeCharacters(name);
    }
    writer.writeEndElement();
    writer.writeEndElement();
  }
}
