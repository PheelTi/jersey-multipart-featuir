package com.archytasit.jersey.multipart.model.bodyparts;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * The interface Body part.
 *
 * @param <T> the type parameter
 */
public interface IBodyPart<T> {

    /**
     * Gets data bag.
     *
     * @return the data bag
     */
    T getDataBag();

    /**
     * Gets field name.
     *
     * @return the field name
     */
    String getFieldName();

    /**
     * Gets media type.
     *
     * @return the media type
     */
    MediaType getMediaType();

    /**
     * Gets content length.
     *
     * @return the content length
     */
    long getContentLength();

    /**
     * Gets headers.
     *
     * @return the headers
     */
    MultivaluedMap<String, String> getHeaders();

    /**
     * Gets input stream.
     *
     * @return the input stream
     * @throws IOException the io exception
     */
    InputStream getInputStream() throws IOException;

    /**
     * Is form field boolean.
     *
     * @return the boolean
     */
    boolean isFormField();
}
