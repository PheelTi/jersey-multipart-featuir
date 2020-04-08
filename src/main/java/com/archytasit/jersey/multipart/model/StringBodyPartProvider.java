package com.archytasit.jersey.multipart.model;

import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.model.internal.StreamingPart;
import com.archytasit.jersey.multipart.utils.HeadersUtils;
import com.archytasit.jersey.multipart.utils.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * The type String body part provider.
 */
public class StringBodyPartProvider implements IBodyPartProvider<StringBodyPart> {

    public StringBodyPart provideBodyPart(MultiPartConfig config, StreamingPart streamingPart) throws IOException {
        return new StringBodyPart(streamingPart, HeadersUtils.getCharset(StandardCharsets.UTF_8, streamingPart.getContentType(), streamingPart.getHeaders()), StreamUtils.toBytesNoClose(streamingPart.getInputStream()));
    }
}
