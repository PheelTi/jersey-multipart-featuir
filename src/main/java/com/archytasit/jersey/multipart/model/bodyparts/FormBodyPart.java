package com.archytasit.jersey.multipart.model.bodyparts;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.archytasit.jersey.multipart.model.bodyparts.AbstractBodyPart;


public final class FormBodyPart extends AbstractBodyPart<String> {

    private Charset charset;

    public FormBodyPart(String fieldName, MediaType mediaType, MultivaluedMap<String, String> headers, Charset charset, byte[] data) {
        super(fieldName, mediaType, headers, data.length, new String(data, charset));
        this.charset = charset;
    }

    @Override
    protected InputStream getInputStreamFromDataBag(String content) {
        return new ByteArrayInputStream(content.getBytes(charset));
    }

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
