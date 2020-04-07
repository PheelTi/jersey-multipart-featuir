package com.archytasit.jersey.multipart.model.databags;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.archytasit.jersey.multipart.MultiPartConfig;

public interface IDataBagProvider<T extends IDataBag> {

    T getDataBag(MultiPartConfig config, MediaType mediaType, MultivaluedMap headers, InputStream inputStream, String fileName) throws IOException;

}
