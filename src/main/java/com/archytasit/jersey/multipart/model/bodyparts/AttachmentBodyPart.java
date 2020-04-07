package com.archytasit.jersey.multipart.model.bodyparts;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.archytasit.jersey.multipart.model.databags.IDataBag;

/**
 * The type Attachment body part.
 *
 * @param <T> the type parameter
 */
public class AttachmentBodyPart<T extends IDataBag> extends AbstractBodyPart<T> {


    private String filename;

    /**
     * Instantiates a new Attachment body part.
     *
     * @param fieldName the field name
     * @param mediaType the media type
     * @param headers   the headers
     * @param filename  the filename
     * @param dataBag   the data bag
     */
    public AttachmentBodyPart(String fieldName, MediaType mediaType, MultivaluedMap<String, String> headers, String filename, T dataBag) {
        super(fieldName, mediaType, headers, dataBag.getContentLength(), dataBag);
        this.filename = filename;
    }

    /**
     * Gets filename.
     *
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    @Override
    protected InputStream getInputStreamFromDataBag(T dataContainer) throws IOException {
        return null;
    }

    @Override
    public final boolean isFormField() {
        return false;
    }
}
