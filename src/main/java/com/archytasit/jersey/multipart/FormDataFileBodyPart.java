package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.parsers.StreamingPart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * The type File body part.
 */
public final class FormDataFileBodyPart extends FormDataBodyPart<File> {


    /**
     * Instantiates a new Form data file body part.
     *
     * @param name        the name
     * @param storeEntity the store entity
     * @param fileName    the file name
     * @param mediaType   the media type
     */
    public FormDataFileBodyPart(String name, File storeEntity, String fileName, MediaType mediaType) {
        super(name, mediaType, new MultivaluedHashMap<>(), false, fileName, storeEntity, storeEntity.length(), ContentDisposition.withFileName(name, fileName));
    }

    /**
     * Instantiates a new Form data file body part.
     *
     * @param name        the name
     * @param storeEntity the store entity
     * @param fileName    the file name
     */
    public FormDataFileBodyPart(String name, File storeEntity, String fileName) {
        this(name, storeEntity, fileName, MediaType.APPLICATION_OCTET_STREAM_TYPE);
    }

    /**
     * Instantiates a new Form data file body part.
     *
     * @param name        the name
     * @param storeEntity the store entity
     * @param mediaType   the media type
     */
    public FormDataFileBodyPart(String name, File storeEntity, MediaType mediaType) {
        this(name, storeEntity, storeEntity.getName(), mediaType);
    }

    /**
     * Instantiates a new Form data file body part.
     *
     * @param name        the name
     * @param storeEntity the store entity
     */
    public FormDataFileBodyPart(String name, File storeEntity) {
        this(name, storeEntity, storeEntity.getName(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
    }

    /**
     * Instantiates a new Form data file body part.
     *
     * @param part the part
     * @param f    the f
     */
    public FormDataFileBodyPart(StreamingPart part, File f) {
        super(part, f.length(), f);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(getStoreEntity());
    }


    @Override
    public void close() {
        getStoreEntity().delete();
    }
}
