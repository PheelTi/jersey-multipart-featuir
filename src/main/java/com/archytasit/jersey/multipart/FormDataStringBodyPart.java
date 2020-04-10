package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.parsers.StreamingPart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * The type String body part.
 */
public final class FormDataStringBodyPart extends FormDataBodyPart<String> {

    private Charset charset;

    public FormDataStringBodyPart(StreamingPart part, Charset charset, String stringValue) {
        super(part, stringValue.length(), stringValue);
        this.charset = charset;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(getStoreEntity().getBytes(charset));
    }


}
