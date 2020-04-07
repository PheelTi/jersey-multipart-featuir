package com.archytasit.jersey.multipart.model.bodyparts;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;


import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.model.databags.IDataBagProvider;

public interface IBodyPartProvider {

    List<IBodyPart> parseRequest(MultiPartConfig config, IDataBagProvider provider, MediaType mediaType, MultivaluedMap headers, InputStream stream);


}
