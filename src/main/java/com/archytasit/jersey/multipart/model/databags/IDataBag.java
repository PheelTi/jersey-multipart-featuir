package com.archytasit.jersey.multipart.model.databags;

import java.io.IOException;
import java.io.InputStream;

/**
 * The interface Data bag.
 */
public interface IDataBag {

    /**
     * Gets input stream.
     *
     * @return the input stream
     * @throws IOException the io exception
     */
    public InputStream getInputStream() throws IOException;

    /**
     * Gets content length.
     *
     * @return the content length
     */
    public long getContentLength();

    /**
     * Clean resource.
     */
    public void cleanResource();

    /**
     * Is cleanable boolean.
     *
     * @return the boolean
     */
    public default boolean isCleanable() { return true ;}
}
