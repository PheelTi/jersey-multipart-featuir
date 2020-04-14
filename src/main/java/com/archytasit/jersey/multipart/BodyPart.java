package com.archytasit.jersey.multipart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Body part.
 */
public abstract class BodyPart implements Closeable {

    private MultiPart parent;
    private String name;
    private MediaType contentType;
    private MultivaluedMap<String, String> headers;
    private ContentDisposition contentDisposition;

    /**
     * Instantiates a new Body part.
     *
     * @param name               the name
     * @param contentType        the content type
     * @param headers            the headers
     * @param contentDisposition the content disposition
     */
    public BodyPart(String name, MediaType contentType, MultivaluedMap<String, String> headers, ContentDisposition contentDisposition) {
        this.name = name;
        this.contentType = contentType;
        this.headers = headers;
        this.contentDisposition = contentDisposition;
    }

    /**
     * Sets parent.
     *
     * @param parent the parent
     */
    protected void setParent(MultiPart parent) {
        this.parent = parent;
    }

    /**
     * Add content type parameter.
     *
     * @param paramName  the param name
     * @param paramValue the param value
     */
    public void addContentTypeParameter(String paramName, String paramValue) {
        Map<String, String> newParams = new HashMap<>(contentType.getParameters());
        newParams.put(paramName, paramValue);
        contentType = new MediaType(contentType.getType(), contentType.getSubtype(), newParams);
    }

    /**
     * Gets parent.
     *
     * @return the parent
     */
    public MultiPart getParent() {
        return parent;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
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
     * Gets content disposition.
     *
     * @return the content disposition
     */
    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }

    @Override
    public void close() {

    }
}
