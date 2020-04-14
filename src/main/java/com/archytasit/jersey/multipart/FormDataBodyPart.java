package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.parsers.StreamingPart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * The type Form data body part.
 *
 * @param <T> the type parameter
 */
public abstract class FormDataBodyPart<T> extends BodyPart implements Closeable {

    private final boolean isFormField;

    private String filename;

    private T storeEntity;

    private long contentLength = -1;

    /**
     * Instantiates a new Form data body part.
     *
     * @param part          the part
     * @param contentLength the content length
     * @param storeEntity   the store entity
     */
    protected FormDataBodyPart(StreamingPart part, long contentLength, T storeEntity) {
        super(part.getFieldName(), part.getContentType(), part.getHeaders(), part.getContentDisposition());
        this.storeEntity = storeEntity;
        this.contentLength = contentLength;
        this.isFormField = part.isFormField();
        this.filename = part.getFileName();
    }

    /**
     * Instantiates a new Form data body part.
     *
     * @param name               the name
     * @param contentType        the content type
     * @param headers            the headers
     * @param isFormField        the is form field
     * @param filename           the filename
     * @param storeEntity        the store entity
     * @param contentLength      the content length
     * @param contentDisposition the content disposition
     */
    protected FormDataBodyPart(String name, MediaType contentType, MultivaluedMap<String, String> headers, boolean isFormField, String filename, T storeEntity, long contentLength, ContentDisposition contentDisposition) {
        super(name, contentType, headers, contentDisposition);
        this.isFormField = isFormField;
        this.filename = filename;
        this.storeEntity = storeEntity;
        this.contentLength = contentLength;

    }

    /**
     * Gets content length.
     *
     * @return the content length
     */
    public long getContentLength() {
        return contentLength;
    }


    /**
     * Gets entity.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the entity
     * @throws IOException the io exception
     */
    public <T> T getEntity(Class<T> type) throws IOException {
        if (InputStream.class.isAssignableFrom(type)) {
            return (T) getInputStream();
        } else if (storeEntity.getClass().isAssignableFrom(type)) {
            return (T) storeEntity;
        }
        return null;
    }

    /**
     * Gets input stream.
     *
     * @return the input stream
     * @throws IOException the io exception
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * Is form field boolean.
     *
     * @return the boolean
     */
    public boolean isFormField() {
        return isFormField;
    }

    /**
     * Gets filename.
     *
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Gets store entity.
     *
     * @return the store entity
     */
    public T getStoreEntity() {
        return storeEntity;
    }


}

