package com.archytasit.jersey.multipart.utils;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.fileupload.FileItemHeaders;

import com.archytasit.jersey.multipart.LoggingWrapper;

public class HeadersUtils {



    public static MediaType getMediaType(String contentType, MediaType defaultValue) {
        if (contentType != null && contentType.trim().length() > 0) {
            try {
                return MediaType.valueOf(contentType);
            } catch (IllegalArgumentException e) {
                LoggingWrapper.warn(HeadersUtils.class, "unknown media type", null);
            }
        }
        return defaultValue;
    }

    public static Charset getCharset(Charset defaultCharset, MediaType mt, MultivaluedMap headers) {
        // TODO
        return defaultCharset;
    }

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
