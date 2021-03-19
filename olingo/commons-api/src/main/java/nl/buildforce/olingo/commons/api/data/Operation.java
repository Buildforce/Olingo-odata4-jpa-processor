/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
 */
package nl.buildforce.olingo.commons.api.data;

import java.net.URI;

/**
 * Data representation for an operation.
 */
public class Operation {
    public enum Type {ACTION, FUNCTION}

    private String metadataAnchor;

    private String title;

    private URI target;

    private Type type;

    /**
     * Gets metadata anchor.
     *
     * @return metadata anchor.
     */
    public String getMetadataAnchor() {
        return metadataAnchor;
    }

/*
 * Sets metadata anchor.
 *
 * @param metadataAnchor metadata anchor.
 */
/*public void setMetadataAnchor(String metadataAnchor) {
  this.metadataAnchor = metadataAnchor;
}*/

    /**
     * Gets title.
     *
     * @return title.
     */
    public String getTitle() {
        return title;
    }

    /*
     * Sets title.
     *
     * @param title title.
     */
//  public void setTitle(String title) {
//    this.title = title;
//  }
//

    /**
     * Gets target.
     *
     * @return target.
     */
    public URI getTarget() {
        return target;
    }

    /**
     * Sets target.
     *
     * @param target target.
     */
/*
  public void setTarget(URI target) {
    this.target = target;
  }
*/

    /**
     * Gets the Operation Type
     *
     * @return
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the Operation type
     * @param type
     */
/*  public void setType(Type type) {
    this.type = type;
  }*/

}