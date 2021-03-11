package nl.buildforce.olingo.commons.api.http;

/**
 * Supplemental HTTP header constants.
 */
public interface HttpHeader {
    /**
     * See <a href="http://www.rfc-editor.org/rfc/rfc2392.txt">RFC 2392</a>.
     */
    String CONTENT_ID = "Content-ID";

    /**
     * Custom Header defined in the OData standard.
     */
    String ODATA_ENTITY_ID = "OData-EntityID";

    /**
     * Custom Header defined in the OData standard.
     */
    String ODATA_MAX_VERSION = "OData-MaxVersion";

    /**
     * Custom Header defined in the OData standard.
     */
    String ODATA_VERSION = "OData-Version";

    /**
     * OData Prefer Header.
     * See <a href="http://docs.oasis-open.org/odata/odata/v4.0/odata-v4.0-part1-protocol.html#_Toc406398233">
     * OData Version 4.0 Part 1: Protocol</a> and <a href="https://www.ietf.org/rfc/rfc7240.txt">RFC 7240</a>.
     */
    String PREFER = "Prefer";

    /**
     * OData Preference-Applied Header.
     * See <a href="http://docs.oasis-open.org/odata/odata/v4.0/odata-v4.0-part1-protocol.html#_Toc406398247">
     * OData Version 4.0 Part 1: Protocol</a> and <a href="https://www.ietf.org/rfc/rfc7240.txt">RFC 7240</a>.
     */
    String PREFERENCE_APPLIED = "Preference-Applied";

    /**
     * See <a href="http://www.rfc-editor.org/rfc/rfc7230.txt">RFC 7230</a>.
     */
    String TE = "TE";

    /**
     * Non standard header.
     */
    String X_HTTP_METHOD = "X-HTTP-Method";

    /**
     * Non standard header.
     */
    String X_HTTP_METHOD_OVERRIDE = "X-HTTP-Method-Override";

}