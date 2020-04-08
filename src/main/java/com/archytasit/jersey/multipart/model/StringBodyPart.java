package com.archytasit.jersey.multipart.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.archytasit.jersey.multipart.model.internal.StreamingPart;


/**
 * The type String body part.
 */
public class StringBodyPart extends AbstractBodyPart {

    private String stringValue;

    private Charset charset;

    /**
     * Instantiates a new String body part.
     *
     * @param streamingPart the streaming part
     * @param charset       the charset
     * @param data          the data
     */
    public StringBodyPart(StreamingPart streamingPart, Charset charset, byte[] data) {
        super(streamingPart, data.length);
        this.stringValue = new String(data, charset);
        this.charset = charset;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(stringValue.getBytes(charset));
    }

    @Override
    public void cleanResource() {
        stringValue = null;
    }

    /**
     * Gets string value.
     *
     * @return the string value
     */
    public String getStringValue() {
        return stringValue;
    }
}
