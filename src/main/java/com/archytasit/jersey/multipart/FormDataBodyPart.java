package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.parsers.StreamingPart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public abstract class FormDataBodyPart<T> extends BodyPart implements Closeable {

    private final boolean isFormField;

    private String filename;

    private T storeEntity;

    private long contentLength = -1;

    protected FormDataBodyPart(StreamingPart part, long contentLength, T storeEntity) {
        super(part.getFieldName(), part.getContentType(), part.getHeaders(), part.getContentDisposition());
        this.storeEntity = storeEntity;
        this.contentLength = contentLength;
        this.isFormField = part.isFormField();
        this.filename = part.getFileName();
    }

    protected FormDataBodyPart(String name, MediaType contentType, MultivaluedMap<String, String> headers, boolean isFormField, String filename, T storeEntity, long contentLength, ContentDisposition contentDisposition) {
        super(name, contentType, headers, contentDisposition);
        this.isFormField = isFormField;
        this.filename = filename;
        this.storeEntity = storeEntity;
        this.contentLength = contentLength;

    }

    public long getContentLength() {
        return contentLength;
    }


    public <T> T getEntity(Class<T> type) throws IOException {
        if (InputStream.class.isAssignableFrom(type)) {
            return (T) getInputStream();
        } else if (storeEntity.getClass().isAssignableFrom(type)) {
            return (T) storeEntity;
        }
        return null;
    }

    public abstract InputStream getInputStream() throws IOException;

    public boolean isFormField() {
        return isFormField;
    }

    public String getFilename() {
        return filename;
    }

    public T getStoreEntity() {
        return storeEntity;
    }


}

