package com.archytasit.jersey.multipart.utils;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.fileupload.FileItemHeaders;

/**
 * The type Headers utils.
 */
public class HeadersUtils {


    /**
     * Gets media type.
     *
     * @param contentType  the content type
     * @param defaultValue the default value
     * @return the media type
     */
    public static MediaType getMediaType(String contentType, MediaType defaultValue) {
        if (contentType != null && contentType.trim().length() > 0) {
            try {
                return MediaType.valueOf(contentType);
            } catch (IllegalArgumentException e) {
                Logger.getLogger(HeadersUtils.class.getName()).log(Level.WARNING,  "unknown media type");
            }
        }
        return defaultValue;
    }

    /**
     * Gets charset.
     *
     * @param defaultCharset the default charset
     * @param mt             the mt
     * @param headers        the headers
     * @return the charset
     */
    public static Charset getCharset(Charset defaultCharset, MediaType mt, MultivaluedMap headers) {
        // TODO
        return defaultCharset;
    }

    /**
     * To multi valued map multivalued map.
     *
     * @param headers the headers
     * @return the multivalued map
     */
    public static MultivaluedMap<String, String> toMultiValuedMap(FileItemHeaders headers) {
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        if (headers != null) {
            Iterable<String> headerNamesIterable = () -> headers.getHeaderNames();
            List<String> headerNames = StreamSupport
                    .stream(headerNamesIterable.spliterator(), false)
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            for (String headerName : headerNames) {
                Iterable<String> headerValuesIterable = () -> headers.getHeaders(headerName);
                List<String> headerValues = StreamSupport
                        .stream(headerValuesIterable.spliterator(), false)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                result.put(headerName, headerValues);
            }
        }
        return result;
    }
}
