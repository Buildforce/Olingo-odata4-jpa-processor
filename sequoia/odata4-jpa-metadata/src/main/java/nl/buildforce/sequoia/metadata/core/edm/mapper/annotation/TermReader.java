package nl.buildforce.sequoia.metadata.core.edm.mapper.annotation;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.buildforce.olingo.commons.api.edm.provider.CsdlTerm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TermReader {
  final private XmlMapper xmlMapper;

  public TermReader() {
    JacksonXmlModule module = new JacksonXmlModule();
    module.setDefaultUseWrapper(false);
    xmlMapper = new XmlMapper(module);
  }

  public Map<String, Map<String, CsdlTerm>> getTerms(String path) throws IOException {
    return convertEDMX(readFromResource(path));
  }

  public Map<String, Map<String, CsdlTerm>> getTerms(URI uri) throws IOException {
    return convertEDMX(readFromURI(uri));
  }

  public Edmx readFromResource(final String path) throws IOException {
    byte[] b = loadXML(path);
    return xmlMapper.readValue(new String(b), Edmx.class);
  }

  public Edmx readFromURI(final URI uri) throws IOException {
    return xmlMapper.readValue(uri.toURL(), Edmx.class);
  }

  private byte[] loadXML(String path) {
    byte[] image = null;

    URL u = this.getClass().getClassLoader().getResource(path);
    try {
      InputStream i  = Objects.requireNonNull(u).openStream();
      image = new byte[i.available()];
      i.read(image);
    } catch (IOException | NullPointerException e1) {
      e1.printStackTrace();
    } /*finally {
      try {
        i.close();
        return image;
      } catch (IOException | NullPointerException e) {
        e.printStackTrace();
      }
    }*/
    return image;
  }

  private Map<String, Map<String, CsdlTerm>> convertEDMX(Edmx edmx) {
    if (edmx != null && edmx.getDataService() != null) {
      Schema[] schemas = edmx.getDataService().getSchemas();
      Map<String, Map<String, CsdlTerm>> edmSchemas = new HashMap<>(schemas.length);

      for (Schema schema : schemas) {
        String namespace = schema.getNamespace();
        Map<String, CsdlTerm> terms = new HashMap<>();
        for (CsdlTerm t : schema.getTerms()) {
          terms.put(t.getName(), t);
        }
        edmSchemas.put(namespace, terms);
      }
      return edmSchemas;
    }
    return null;
  }

}