package com.archytasit.jersey.multipart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

public abstract class BodyPart implements Closeable {

    private MultiPart parent;
    private String name;
    private MediaType contentType;
    private MultivaluedMap<String, String> headers;
    private ContentDisposition contentDisposition;

    public BodyPart(String name, MediaType contentType, MultivaluedMap<String, String> headers, ContentDisposition contentDisposition) {
        this.name = name;
        this.contentType = contentType;
        this.headers = headers;
        this.contentDisposition = contentDisposition;
    }

    protected void setParent(MultiPart parent) {
        this.parent = parent;
    }

    public void addContentTypeParameter(String paramName, String paramValue) {
        Map<String, String> newParams = new HashMap<>(contentType.getParameters());
        newParams.put(paramName, paramValue);
        contentType = new MediaType(contentType.getType(), contentType.getSubtype(), newParams);
    }

    public MultiPart getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public MediaType getContentType() {
        return contentType;
    }

    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }

    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }

    @Override
    public void close() {

    }
}
