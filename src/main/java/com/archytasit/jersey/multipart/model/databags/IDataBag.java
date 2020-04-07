package com.archytasit.jersey.multipart.model.databags;

import java.io.IOException;
import java.io.InputStream;

public interface IDataBag {

    public InputStream getInputStream() throws IOException;

    public long getContentLength();

    public void cleanResource();

    public default boolean isCleanable() { return true ;}
}
