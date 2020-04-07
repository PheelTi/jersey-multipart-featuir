package com.archytasit.jersey.multipart.model.bodyparts;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.archytasit.jersey.multipart.model.databags.IDataBag;

public class AttachmentBodyPart<T extends IDataBag> extends AbstractBodyPart<T> {


    private String filename;

    public AttachmentBodyPart(String fieldName, MediaType mediaType, MultivaluedMap<String, String> headers, String filename, T dataBag) {
        super(fieldName, mediaType, headers, dataBag.getContentLength(), dataBag);
        this.filename = filename;
    }

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
