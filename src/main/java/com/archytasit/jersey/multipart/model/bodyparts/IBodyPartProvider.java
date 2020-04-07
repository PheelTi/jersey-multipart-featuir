package com.archytasit.jersey.multipart.model.bodyparts;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;


import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.model.databags.IDataBagProvider;

/**
 * The interface Body part provider.
 */
public interface IBodyPartProvider {

    /**
     * Parse request list.
     *
     * @param config    the config
     * @param provider  the provider
     * @param mediaType the media type
     * @param headers   the headers
     * @param stream    the stream
     * @return the list
     */
    List<IBodyPart> parseRequest(MultiPartConfig config, IDataBagProvider provider, MediaType mediaType, MultivaluedMap headers, InputStream stream);


}
