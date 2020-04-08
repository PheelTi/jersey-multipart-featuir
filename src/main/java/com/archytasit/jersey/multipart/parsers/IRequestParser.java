package com.archytasit.jersey.multipart.parsers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.InputStream;

/**
 * The interface Request parser.
 */
public interface IRequestParser {

    /**
     * Gets part iterator.
     *
     * @param mediaType the media type
     * @param headers   the headers
     * @param stream    the stream
     * @return the part iterator
     */
    StreamingPartIterator getPartIterator(MediaType mediaType, MultivaluedMap headers, InputStream stream);
}
