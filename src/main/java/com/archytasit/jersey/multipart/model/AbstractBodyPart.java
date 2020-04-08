package com.archytasit.jersey.multipart.model;

import com.archytasit.jersey.multipart.model.internal.StreamingPart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * The type Abstract body part.
 */
public abstract class AbstractBodyPart implements IBodyPart {

    private final boolean isFormField;

    private String fieldName;

    private String fileName;

    private MediaType contentType;

    private long contentLength;

    private MultivaluedMap<String, String> headers;


    /**
     * Instantiates a new Abstract body part.
     *
     * @param streamingPart the streaming part
     * @param contentLength the content length
     */
    public AbstractBodyPart(StreamingPart streamingPart, long contentLength) {
        this.isFormField = streamingPart.isFormField();
        this.fieldName = streamingPart.getFieldName();
        this.fileName = streamingPart.getFileName();
        this.contentType = streamingPart.getContentType();
        this.headers = streamingPart.getHeaders();
        this.contentLength = contentLength;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }


    public boolean isFormField() {
        return isFormField;
    }
}
