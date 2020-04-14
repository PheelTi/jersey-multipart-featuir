package com.archytasit.jersey.multipart.bodypartproviders;

import com.archytasit.jersey.multipart.FormDataBodyPart;
import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.parsers.StreamingPart;

import java.io.IOException;

/**
 * The interface Form data body part provider.
 */
public interface IFormDataBodyPartProvider {

    /**
     * Provide body part t.
     *
     * @param <T>           the type parameter
     * @param config        the config
     * @param streamingPart the streaming part
     * @return the t
     * @throws IOException the io exception
     */
    <T extends FormDataBodyPart> T provideBodyPart(MultiPartConfig config, StreamingPart streamingPart) throws IOException;


}
