package com.archytasit.jersey.multipart.model.bodyparts;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * The type Abstract body part.
 *
 * @param <T> the type parameter
 */
abstract class AbstractBodyPart<T> implements IBodyPart<T> {

    private String fieldName;

    private MediaType mediaType;

    private long contentLength;

    private T dataBag;

    private MultivaluedMap<String, String> headers;

    /**
     * Instantiates a new Abstract body part.
     *
     * @param fieldName     the field name
     * @param mediaType     the media type
     * @param headers       the headers
     * @param contentLength the content length
     * @param dataBag       the data bag
     */
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

    /**
     * Gets input stream from data bag.
     *
     * @param dataContainer the data container
     * @return the input stream from data bag
     * @throws IOException the io exception
     */
    protected abstract InputStream getInputStreamFromDataBag(T dataContainer) throws IOException ;

    public T getDataBag() {
        return dataBag;
    }

    public abstract boolean isFormField();
}
