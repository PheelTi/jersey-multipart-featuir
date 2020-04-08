package com.archytasit.jersey.multipart.model;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * The interface Body part.
 */
public interface IBodyPart {

    /**
     * Gets field name.
     *
     * @return the field name
     */
    String getFieldName();


    /**
     * Gets file name.
     *
     * @return the file name
     */
    String getFileName();

    /**
     * Gets content type.
     *
     * @return the content type
     */
    MediaType getContentType();

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

    /**
     * Clean resource.
     */
    void cleanResource();
}
