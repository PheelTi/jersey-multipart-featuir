package com.archytasit.jersey.multipart.model.bodyparts;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.archytasit.jersey.multipart.model.bodyparts.AbstractBodyPart;


/**
 * The type Form body part.
 */
public final class FormBodyPart extends AbstractBodyPart<String> {

    private Charset charset;

    /**
     * Instantiates a new Form body part.
     *
     * @param fieldName the field name
     * @param mediaType the media type
     * @param headers   the headers
     * @param charset   the charset
     * @param data      the data
     */
    public FormBodyPart(String fieldName, MediaType mediaType, MultivaluedMap<String, String> headers, Charset charset, byte[] data) {
        super(fieldName, mediaType, headers, data.length, new String(data, charset));
        this.charset = charset;
    }

    @Override
    protected InputStream getInputStreamFromDataBag(String content) {
        return new ByteArrayInputStream(content.getBytes(charset));
    }

    /**
     * Gets charset.
     *
     * @return the charset
     */
    public Charset getCharset() {
        return charset;
    }

    @Override
    public String getDataBag() {
        return null;
    }

    @Override
    public boolean isFormField() {
        return true;
    }
}
