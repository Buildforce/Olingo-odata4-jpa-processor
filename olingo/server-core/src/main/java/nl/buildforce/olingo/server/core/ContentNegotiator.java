/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import nl.buildforce.olingo.commons.api.format.AcceptCharset;
import nl.buildforce.olingo.commons.api.format.AcceptType;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.serializer.CustomContentTypeSupport;
import nl.buildforce.olingo.server.api.serializer.RepresentationType;
import nl.buildforce.olingo.server.api.uri.queryoption.FormatOption;

import static com.google.common.net.HttpHeaders.ACCEPT;
import static com.google.common.net.HttpHeaders.ACCEPT_CHARSET;

public final class ContentNegotiator {

    private static final String APPLICATION_JSON = "application/json";
    private static final String ATOM = "atom";
    private static final Pattern CHARSET_PATTERN = Pattern.compile("([^,][\\w!#$%&'*+-._`|~;^]*)");
    private static final String COLON = ":";
    private static final String JSON = ContentType.JSON;
    private static final String METADATA = "METADATA";
    private static final String XML = "xml";

    private static final List<ContentType> DEFAULT_SUPPORTED_CONTENT_TYPES =
            List.of(ContentType.CT_JSON, ContentType.JSON_NO_METADATA, ContentType.APPLICATION_JSON, ContentType.JSON_FULL_METADATA, ContentType.APPLICATION_ATOM_XML, ContentType.APPLICATION_XML);

    private static List<ContentType> getDefaultSupportedContentTypes(RepresentationType type) {
        return switch (type) {
            case METADATA -> List.of(ContentType.APPLICATION_XML, ContentType.APPLICATION_JSON);
            case MEDIA, BINARY -> Collections.singletonList(ContentType.APPLICATION_OCTET_STREAM);
            case VALUE, COUNT -> Collections.singletonList(ContentType.TEXT_PLAIN);
            case BATCH -> Collections.singletonList(ContentType.MULTIPART_MIXED);
            default -> DEFAULT_SUPPORTED_CONTENT_TYPES;
        };
    }

    private static List<ContentType> getSupportedContentTypes(
            CustomContentTypeSupport customContentTypeSupport, RepresentationType representationType)
            throws ContentNegotiatorException {
        List<ContentType> defaultSupportedContentTypes = getDefaultSupportedContentTypes(representationType);
        List<ContentType> result = customContentTypeSupport == null ? defaultSupportedContentTypes :
                customContentTypeSupport.modifySupportedContentTypes(defaultSupportedContentTypes, representationType);
        if (result == null || result.isEmpty()) {
            throw new ContentNegotiatorException("No content type has been specified as supported.",
                    ContentNegotiatorException.MessageKeys.NO_CONTENT_TYPE_SUPPORTED);
        } else {
            return result;
        }
    }

    public static ContentType doContentNegotiation(FormatOption formatOption, ODataRequest request,
                                                   CustomContentTypeSupport customContentTypeSupport, RepresentationType representationType)
            throws ContentNegotiatorException {
        List<ContentType> supportedContentTypes =
                getSupportedContentTypes(customContentTypeSupport, representationType);
        String acceptHeaderValue = request.getHeader(ACCEPT);
        String acceptCharset = request.getHeader(ACCEPT_CHARSET);
        List<AcceptCharset> charsets = null;

        ContentType result;

        if (formatOption != null && formatOption.getFormat() != null) {
            String formatString = formatOption.getFormat().trim();
            ContentType contentType = mapContentType(formatString, representationType);
            boolean isCharsetInFormat;
            List<AcceptType> formatTypes;
            try {
                formatTypes = AcceptType.fromContentType(contentType == null ? new ContentType(formatOption.getFormat()) : contentType);
            } catch (IllegalArgumentException e) {
                throw new AcceptHeaderContentNegotiatorException(
                        "Unsupported $format=" + formatString, e,
                        AcceptHeaderContentNegotiatorException.MessageKeys.UNSUPPORTED_FORMAT_OPTION, formatString);
            }
            Map<String, String> formatParameters = formatTypes.get(0).getParameters();
            if (!formatParameters.isEmpty() && null != formatParameters.get(ContentType.PARAMETER_CHARSET)) {
                isCharsetInFormat = true;
            } else {
                isCharsetInFormat = false;
                charsets = getAcceptCharset(acceptCharset);
            }
            try {
                if (isCharsetInFormat) {
                    charsets = getAcceptCharset(formatParameters.get(ContentType.PARAMETER_CHARSET));
                }
                result = getAcceptedType(formatTypes, supportedContentTypes, charsets);
            } catch (IllegalArgumentException | AcceptHeaderContentNegotiatorException e) {
                throw new AcceptHeaderContentNegotiatorException(
                        "Unsupported $format=" + formatString, e,
                        AcceptHeaderContentNegotiatorException.MessageKeys.UNSUPPORTED_FORMAT_OPTION, formatString);
            } catch (ContentNegotiatorException e) {
                throw new ContentNegotiatorException(
                        "Unsupported $format=" + formatString, e,
                        ContentNegotiatorException.MessageKeys.UNSUPPORTED_FORMAT_OPTION, formatString);
            }
            if (result == null) {
                throw new ContentNegotiatorException("Unsupported $format = " + formatString,
                        ContentNegotiatorException.MessageKeys.UNSUPPORTED_FORMAT_OPTION, formatString);
            }
        } else if (acceptHeaderValue != null) {
            charsets = getAcceptCharset(acceptCharset);
            try {
                result = getAcceptedType(AcceptType.create(acceptHeaderValue),
                        supportedContentTypes, charsets);
            } catch (IllegalArgumentException e) {
                throw new AcceptHeaderContentNegotiatorException(e.getMessage(), e,
                        AcceptHeaderContentNegotiatorException.MessageKeys.UNSUPPORTED_ACCEPT_TYPES,
                        e.getMessage().substring(e.getMessage().lastIndexOf(COLON) + 1));
            }
            if (result == null) {
                List<AcceptType> types = AcceptType.create(acceptHeaderValue);
                throw new ContentNegotiatorException(
                        "The combination of type and subtype " + types.get(0) +
                                " != " + supportedContentTypes,
                        ContentNegotiatorException.MessageKeys.UNSUPPORTED_ACCEPT_TYPES, acceptHeaderValue);
            }
        } else {
            charsets = getAcceptCharset(acceptCharset);
            ContentType requestedContentType = getDefaultSupportedContentTypes(representationType).get(0);
            result = getAcceptedType(AcceptType.fromContentType(requestedContentType),
                    supportedContentTypes, charsets);

            if (result == null) {
                throw new ContentNegotiatorException(
                        "unsupported accept content type: " + requestedContentType + " != " + supportedContentTypes,
                        ContentNegotiatorException.MessageKeys.UNSUPPORTED_CONTENT_TYPE,
                        requestedContentType.toString());
            }
        }
        return result;
    }

    /**
     * @param acceptCharset
     * @return
     * @throws ContentNegotiatorException
     * @throws AcceptHeaderContentNegotiatorException
     */
    private static List<AcceptCharset> getAcceptCharset(String acceptCharset)
            throws ContentNegotiatorException, AcceptHeaderContentNegotiatorException {
        List<AcceptCharset> charsets = null;
        if (acceptCharset != null) {
            try {
                charsets = AcceptCharset.create(acceptCharset);
            } catch (UnsupportedCharsetException e) {
                throw new ContentNegotiatorException(e.getMessage(), e,
                        ContentNegotiatorException.MessageKeys.UNSUPPORTED_ACCEPT_CHARSET,
                        e.getMessage().substring(e.getMessage().lastIndexOf(COLON) + 1));
            } catch (IllegalArgumentException e) {
                throw new AcceptHeaderContentNegotiatorException(e.getMessage(), e,
                        AcceptHeaderContentNegotiatorException.MessageKeys.UNSUPPORTED_ACCEPT_CHARSET_HEADER_OPTIONS,
                        e.getMessage().substring(e.getMessage().lastIndexOf(COLON) + 1));
            }
        }
        return charsets;
    }

    private static ContentType mapContentType(String formatString,
                                              RepresentationType representationType) {
        if (representationType.name().equals(METADATA)) {
            return JSON.equalsIgnoreCase(formatString) ||
                    APPLICATION_JSON.equalsIgnoreCase(formatString) ? ContentType.APPLICATION_JSON :
                    XML.equalsIgnoreCase(formatString) ? ContentType.APPLICATION_XML :
                            ATOM.equalsIgnoreCase(formatString) ? ContentType.APPLICATION_ATOM_XML : null;
        } else {
            return JSON.equalsIgnoreCase(formatString) ? ContentType.CT_JSON :
                    XML.equalsIgnoreCase(formatString) ? ContentType.APPLICATION_XML :
                            ATOM.equalsIgnoreCase(formatString) ? ContentType.APPLICATION_ATOM_XML :
                                    APPLICATION_JSON.equalsIgnoreCase(formatString) ? ContentType.APPLICATION_JSON : null;
        }
    }

    private static ContentType getAcceptedType(List<AcceptType> acceptedContentTypes,
                                               List<ContentType> supportedContentTypes, List<AcceptCharset> charsets) throws ContentNegotiatorException {
        if (charsets != null) {
            for (AcceptCharset charset : charsets) {
                return getContentType(acceptedContentTypes, supportedContentTypes, charset);
            }
        } else {
            return getContentType(acceptedContentTypes, supportedContentTypes, null);
        }
        return null;
    }

    private static ContentType getContentType(List<AcceptType> acceptedContentTypes,
                                              List<ContentType> supportedContentTypes, AcceptCharset charset) throws ContentNegotiatorException {
        for (AcceptType acceptedType : acceptedContentTypes) {
            for (ContentType supportedContentType : supportedContentTypes) {
                ContentType contentType = supportedContentType;
                String charSetValue = acceptedType.getParameter(ContentType.PARAMETER_CHARSET);
                if (charset != null) {
                        contentType = new ContentType(contentType, ContentType.PARAMETER_CHARSET,
                                ("*".equals(charset.toString())) ? StandardCharsets.UTF_8.name() : charset.toString());
                } else if (charSetValue != null) {
                    if ("utf8".equalsIgnoreCase(charSetValue) || StandardCharsets.UTF_8.name().equalsIgnoreCase(charSetValue)) {
                        contentType = new ContentType(contentType, ContentType.PARAMETER_CHARSET, StandardCharsets.UTF_8.name());
                    } else {
                        if (CHARSET_PATTERN.matcher(charSetValue).matches()) {
                            throw new ContentNegotiatorException("Unsupported accept-header-charset = " + charSetValue,
                                    ContentNegotiatorException.MessageKeys.UNSUPPORTED_ACCEPT_HEADER_CHARSET, acceptedType.toString());
                        } else {
                            throw new AcceptHeaderContentNegotiatorException(
                                    "Illegal charset in Accept header: " + charSetValue,
                                    AcceptHeaderContentNegotiatorException.MessageKeys.UNSUPPORTED_ACCEPT_HEADER_CHARSET,
                                    acceptedType.toString());
                        }
                    }
                }

                String ieee754compatibleValue = acceptedType.getParameter(ContentType.PARAMETER_IEEE754_COMPATIBLE);
                if ("true".equalsIgnoreCase(ieee754compatibleValue)) {
                    contentType = new ContentType(contentType, ContentType.PARAMETER_IEEE754_COMPATIBLE, "true");
                } else if ("false".equalsIgnoreCase(ieee754compatibleValue)) {
                    contentType = new ContentType(contentType, ContentType.PARAMETER_IEEE754_COMPATIBLE, "false");
                } else if (ieee754compatibleValue != null) {
                    throw new IllegalArgumentException("Invalid IEEE754Compatible value in accept header:" + acceptedType);
                }

                if (acceptedType.matches(contentType)) {
                    return contentType;
                }
            }
        }
        return null;
    }

    public static void checkSupport(ContentType contentType,
                                    CustomContentTypeSupport customContentTypeSupport, RepresentationType representationType)
            throws ContentNegotiatorException {
        for (ContentType supportedContentType : getSupportedContentTypes(customContentTypeSupport, representationType)) {
            if (AcceptType.fromContentType(supportedContentType).get(0).matches(contentType)) {
                return;
            }
        }
        throw new ContentNegotiatorException("unsupported content type: " + contentType,
                ContentNegotiatorException.MessageKeys.UNSUPPORTED_CONTENT_TYPE, contentType.toString());
    }

    public static boolean isSupported(ContentType contentType,
                                      CustomContentTypeSupport customContentTypeSupport,
                                      RepresentationType representationType) throws ContentNegotiatorException {

        for (ContentType supportedContentType : getSupportedContentTypes(customContentTypeSupport, representationType)) {
            if (AcceptType.fromContentType(supportedContentType).get(0).matches(contentType)) {
                return true;
            }
        }
        return false;
    }

}