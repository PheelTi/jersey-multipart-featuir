package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.parsers.StreamingPart;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;


/**
 * The type String body part.
 */
public final class FormDataStringBodyPart extends FormDataBodyPart<String> {

    private Charset charset;

    /**
     * Instantiates a new Form data string body part.
     *
     * @param part        the part
     * @param charset     the charset
     * @param stringValue the string value
     */
    public FormDataStringBodyPart(StreamingPart part, Charset charset, String stringValue) {
        super(part, stringValue.length(), stringValue);
        this.charset = charset;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(getStoreEntity().getBytes(charset));
    }


}
