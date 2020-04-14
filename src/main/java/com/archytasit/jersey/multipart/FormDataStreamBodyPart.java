package com.archytasit.jersey.multipart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.IOException;
import java.io.InputStream;

/**
 * The type Form data stream body part.
 *
 * @param <T> the type parameter
 */
public class FormDataStreamBodyPart<T> extends FormDataBodyPart<InputStream> {

    /**
     * Instantiates a new Form data stream body part.
     *
     * @param name        the name
     * @param contentType the content type
     * @param filename    the filename
     * @param storeEntity the store entity
     */
    public FormDataStreamBodyPart(String name, MediaType contentType, String filename, InputStream storeEntity) {
        super(name, contentType, new MultivaluedHashMap<>(), false, filename, storeEntity, -1, ContentDisposition.withFileName(name, filename));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return getStoreEntity();
    }
}

