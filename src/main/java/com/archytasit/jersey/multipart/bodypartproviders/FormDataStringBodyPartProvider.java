package com.archytasit.jersey.multipart.bodypartproviders;

import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.FormDataStringBodyPart;
import com.archytasit.jersey.multipart.parsers.StreamingPart;
import com.archytasit.jersey.multipart.utils.HeadersUtils;
import com.archytasit.jersey.multipart.utils.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * The type String body part provider.
 */
public class FormDataStringBodyPartProvider implements IFormDataBodyPartProvider {

    public FormDataStringBodyPart provideBodyPart(MultiPartConfig config, StreamingPart streamingPart) throws IOException {

        Charset charset = HeadersUtils.getCharset(streamingPart.getContentType());
        if (charset == null) {
            charset = config.getDefaultCharset();
        }
        return new FormDataStringBodyPart(streamingPart, charset, new String(StreamUtils.toBytesNoClose(streamingPart.getInputStream()), charset));
    }
}
