package com.archytasit.jersey.multipart.model.databags;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.archytasit.jersey.multipart.MultiPartConfig;

/**
 * The interface Data bag provider.
 *
 * @param <T> the type parameter
 */
public interface IDataBagProvider<T extends IDataBag> {

    /**
     * Gets data bag.
     *
     * @param config      the config
     * @param mediaType   the media type
     * @param headers     the headers
     * @param inputStream the input stream
     * @param fileName    the file name
     * @return the data bag
     * @throws IOException the io exception
     */
    T getDataBag(MultiPartConfig config, MediaType mediaType, MultivaluedMap headers, InputStream inputStream, String fileName) throws IOException;

}
