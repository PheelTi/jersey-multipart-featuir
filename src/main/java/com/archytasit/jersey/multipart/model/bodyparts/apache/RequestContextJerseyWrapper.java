package com.archytasit.jersey.multipart.model.bodyparts.apache;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.fileupload.RequestContext;

public class RequestContextJerseyWrapper implements RequestContext {

    private String charset = StandardCharsets.UTF_8.name();
    private String contentType;
    private int contentLength = -1;
    private InputStream inputStream;


    public RequestContextJerseyWrapper(MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream inputStream) {

        this.charset = httpHeaders.getFirst(HttpHeaders.CONTENT_ENCODING);
        contentType = httpHeaders.getFirst(HttpHeaders.CONTENT_TYPE);
        String contentLengthStr = httpHeaders.getFirst(HttpHeaders.CONTENT_LENGTH);
        if (contentLengthStr != null && contentLengthStr.matches("[0-9]+")) {
            contentLength = Integer.parseInt(contentLengthStr);
        }
        if (mediaType != null) {
            this.charset = mediaType != null ? mediaType.getParameters().get(MediaType.CHARSET_PARAMETER) : null;
            if (this.charset == null) {

            }
        }
        this.inputStream = inputStream;
    }

    public String getCharacterEncoding() {
        return charset;
    }

    public String getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public InputStream getInputStream() throws IOException {
        return inputStream;
    }
}
