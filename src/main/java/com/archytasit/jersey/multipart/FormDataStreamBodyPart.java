package com.archytasit.jersey.multipart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.IOException;
import java.io.InputStream;

public class FormDataStreamBodyPart<T> extends FormDataBodyPart<InputStream> {

    public FormDataStreamBodyPart(String name, MediaType contentType, String filename, InputStream storeEntity) {
        super(name, contentType, new MultivaluedHashMap<>(), false, filename, storeEntity, -1, ContentDisposition.withFileName(name, filename));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return getStoreEntity();
    }
}

