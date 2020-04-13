package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.utils.InputStreamLimitCounter;
import org.glassfish.jersey.server.ContainerRequest;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.Consumes;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;


/**
 * The type File upload message body worker.
 */
@Singleton
@ConstrainedTo(RuntimeType.SERVER)
@Consumes("multipart/*")
public class MultiPartMessageBodyReaderServer extends MultiPartMessageBodyReaderClient {



    @Inject
    private ResourceCleaner resourceCleaner;


    @Context
    private Provider<ContainerRequest> requestProvider;

    /**
     * Instantiates a new File upload message body worker.
     *
     * @param providers the providers
     */
    @Inject
    public MultiPartMessageBodyReaderServer(@Context final Providers providers) {
        super(providers);
    }

    @Override
    public MultiPart readFrom(Class<MultiPart> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {

        InputStream contentStream  = entityStream;
        if (config.getRequestSizeLimit() > 0) {
            contentStream  = new InputStreamLimitCounter(config.getRequestSizeLimit(), entityStream, config.getRequestSizeLimitAction());
        }

        MultiPart multiPart = super.readFrom(type, genericType, annotations, mediaType, httpHeaders, contentStream);

        resourceCleaner.trackResourceToClean(multiPart, config.getCleanResourceMode(), requestProvider.get());

        return multiPart;
    }
}

