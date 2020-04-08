package com.archytasit.jersey.multipart.model.internal;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.InputStream;

/**
 * The type Streaming part.
 */
public class StreamingPart {

    private String fieldName;

    private MediaType contentType;

    private MultivaluedMap<String, String> headers;

    private boolean isFormField;

    private String fileName;

    private InputStream inputStream;

    /**
     * Instantiates a new Streaming part.
     *
     * @param fieldName   the field name
     * @param contentType the content type
     * @param headers     the headers
     * @param isFormField the is form field
     * @param fileName    the file name
     * @param inputStream the input stream
     */
    public StreamingPart(String fieldName, MediaType contentType, MultivaluedMap<String, String> headers, boolean isFormField, String fileName, InputStream inputStream) {
        this.fieldName = fieldName;
        this.contentType = contentType;
        this.headers = headers;
        this.isFormField = isFormField;
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    /**
     * Gets field name.
     *
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Gets content type.
     *
     * @return the content type
     */
    public MediaType getContentType() {
        return contentType;
    }

    /**
     * Gets headers.
     *
     * @return the headers
     */
    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }

    /**
     * Is form field boolean.
     *
     * @return the boolean
     */
    public boolean isFormField() {
        return isFormField;
    }

    /**
     * Gets file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets input stream.
     *
     * @return the input stream
     */
    public InputStream getInputStream() {
        return inputStream;
    }
}

