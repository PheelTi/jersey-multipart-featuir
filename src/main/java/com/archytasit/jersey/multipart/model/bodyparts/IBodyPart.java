package com.archytasit.jersey.multipart.model.bodyparts;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

public interface IBodyPart<T> {

    T getDataBag();

    String getFieldName();

    MediaType getMediaType();

    long getContentLength();

    MultivaluedMap<String, String> getHeaders();

    InputStream getInputStream() throws IOException;

    boolean isFormField();
}
