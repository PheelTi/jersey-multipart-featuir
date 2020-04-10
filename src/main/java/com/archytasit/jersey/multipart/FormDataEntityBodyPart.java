package com.archytasit.jersey.multipart;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.IOException;
import java.io.InputStream;

public class FormDataEntityBodyPart<T> extends FormDataBodyPart<T> {

    public FormDataEntityBodyPart(String name, Entity<T> entity) {
        super(name, entity.getMediaType(), new MultivaluedHashMap<>(), true, null, entity.getEntity(), -1, ContentDisposition.defaultValue(name));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new IllegalStateException("Cannot get inputstream on FormDataEntity");
    }

}
