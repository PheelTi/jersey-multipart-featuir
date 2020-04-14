package com.archytasit.jersey.multipart;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.IOException;
import java.io.InputStream;

/**
 * The type Form data entity body part.
 *
 * @param <T> the type parameter
 */
public class FormDataEntityBodyPart<T> extends FormDataBodyPart<T> {

    /**
     * Instantiates a new Form data entity body part.
     *
     * @param name   the name
     * @param entity the entity
     */
    public FormDataEntityBodyPart(String name, Entity<T> entity) {
        super(name, entity.getMediaType(), new MultivaluedHashMap<>(), true, null, entity.getEntity(), -1, ContentDisposition.defaultValue(name));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new IllegalStateException("Cannot get inputstream on FormDataEntity");
    }

}
