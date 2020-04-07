package com.archytasit.jersey.multipart.model.bodyparts;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

abstract class AbstractBodyPart<T> implements IBodyPart<T> {

    private String fieldName;

    private MediaType mediaType;

    private long contentLength;

    private T dataBag;

    private MultivaluedMap<String, String> headers;

    public AbstractBodyPart(String fieldName, MediaType mediaType, MultivaluedMap<String, String> headers, long contentLength, T dataBag) {
        this.fieldName = fieldName;
        this.mediaType = mediaType;
        this.headers = headers;
        this.contentLength = contentLength;
        this.dataBag = dataBag;
    }

    public String getFieldName() {
        return fieldName;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }

    public final InputStream getInputStream() throws IOException {
        return getInputStreamFromDataBag(dataBag);
    }

    protected abstract InputStream getInputStreamFromDataBag(T dataContainer) throws IOException ;

    public T getDataBag() {
        return dataBag;
    }

    public abstract boolean isFormField();
}
