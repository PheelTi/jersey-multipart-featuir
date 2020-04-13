package com.archytasit.jersey.multipart.utils;

import org.apache.commons.fileupload.FileItemHeaders;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The type Headers utils.
 */
public class HeadersUtils {


    public static final MediaType MULTIPART_WILDCARD_MEDIATYPE = new MediaType("multipart", MediaType.MEDIA_TYPE_WILDCARD);;

    /**
     * Gets media type.
     *
     * @param contentType  the content type
     * @return the media type
     */
    public static MediaType getMediaType(String contentType) {
        if (contentType != null && contentType.trim().length() > 0) {
            try {
                return MediaType.valueOf(contentType);
            } catch (IllegalArgumentException e) {
                Logger.getLogger(HeadersUtils.class.getName()).log(Level.WARNING,  "unknown media type");
            }
        }
        return null;
    }


    public static Charset getCharset(MediaType mediaType) {

        if (mediaType !=null && mediaType.getParameters().get(HttpHeaderParameters.ContentType.CHARSET) != null) {
            return Charset.forName(mediaType.getParameters().get(HttpHeaderParameters.ContentType.CHARSET));
        }
        return null;
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
