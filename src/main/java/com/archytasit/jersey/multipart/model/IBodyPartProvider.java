package com.archytasit.jersey.multipart.model;

import java.io.IOException;


import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.model.internal.StreamingPart;

/**
 * The interface Body part provider.
 *
 * @param <T> the type parameter
 */
public interface IBodyPartProvider<T extends IBodyPart> {

    /**
     * Provide body part t.
     *
     * @param config        the config
     * @param streamingPart the streaming part
     * @return the t
     * @throws IOException the io exception
     */
    T provideBodyPart(MultiPartConfig config, StreamingPart streamingPart) throws IOException;

}
