package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.bodypartproviders.IFormDataBodyPartProvider;
import com.archytasit.jersey.multipart.parsers.IRequestParser;
import com.archytasit.jersey.multipart.parsers.StreamingPart;
import com.archytasit.jersey.multipart.parsers.StreamingPartIterator;
import com.archytasit.jersey.multipart.utils.InputStreamLimitCounter;
import com.archytasit.jersey.multipart.utils.StreamUtils;
import org.apache.commons.io.output.NullOutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.Consumes;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Providers;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The type File upload message body worker.
 */
@Consumes("multipart/*")
@Singleton
@ConstrainedTo(RuntimeType.CLIENT)
public class MultiPartMessageBodyReaderClient implements MessageBodyReader<MultiPart> {


    private static final MediaType MULTIPART_WILDCARD = new MediaType("multipart", null);


    /**
     * The Config.
     */
    protected MultiPartConfig config;

    @Inject
    private IRequestParser requestParser;

    @Inject
    private IFormDataBodyPartProvider bodyPartProvider;

    /**
     * Instantiates a new File upload message body worker.
     *
     * @param providers the providers
     */
    @Inject
    public MultiPartMessageBodyReaderClient(@Context final Providers providers) {
        // finding MultiPartConfig in context
        final ContextResolver<MultiPartConfig> contextResolver =
                providers.getContextResolver(MultiPartConfig.class, MediaType.WILDCARD_TYPE);
        if (contextResolver != null) {
            this.config = contextResolver.getContext(this.getClass());
        }
        if (this.config == null) {
            // otherwire using default implementation
            this.config = new MultiPartConfig();
        }
        checkMultiPartConfig();
    }

    private void checkMultiPartConfig() {
        File tempDir = new File(this.config.getTempFileDirectory());
        if (!tempDir.exists() || !tempDir.isDirectory()) {
            throw new IllegalArgumentException("The specified tempDirectory in MultiPartConfig must exists and be a directory.");
        }
        File temp = null;
        try {
            temp = File.createTempFile(config.getTempFilePrefix(), config.getTempFileSuffix(), tempDir);
        } catch (IOException e) {
            throw new IllegalArgumentException("The specified tempDirectory in MultiPartConfig is not writable.", e);
        } finally {
            if (temp != null) {
                temp.delete();
            }
        }
    }


    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return mediaType.isCompatible(MediaType.MULTIPART_FORM_DATA_TYPE) && MultiPart.class.isAssignableFrom(type);
    }

    public MultiPart readFrom(Class<MultiPart> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        return readFromParent(null, mediaType, httpHeaders, entityStream);

    }

    /**
     * Read from parent multi part.
     *
     * @param name         the name
     * @param mediaType    the media type
     * @param httpHeaders  the http headers
     * @param entityStream the entity stream
     * @return the multi part
     * @throws IOException             the io exception
     * @throws WebApplicationException the web application exception
     */
    public MultiPart readFromParent(String name, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {

        try {
            // preparing main object
            MultiPart multiPart = new MultiPart(name, mediaType, httpHeaders, ContentDisposition.fromHeaderValues(name, httpHeaders));

            // getting the "iterator" through the parts
            StreamingPartIterator partIterator = requestParser.getPartIterator(mediaType, httpHeaders, entityStream);
            if (partIterator != null) {
                while (partIterator.hasNext()) {
                    StreamingPart part = partIterator.next();
                    // if the part is a multipart, calling this recursively
                    if (MULTIPART_WILDCARD.isCompatible(part.getContentType())) {
                        // nested multipart may have the same name of original multipart.
                        String fieldName = name;
                        multiPart.add(readFromParent(fieldName, part.getContentType(), part.getHeaders(), part.getInputStream()));
                    } else {
                        // else proving the part bay the configured provider.
                        FormDataBodyPart storedPart = bodyPartProvider.provideBodyPart(config, part);
                        trackResourceForClean(storedPart);
                        multiPart.add(storedPart);
                    }
                }
            }
            return multiPart;

        } finally {
            // redind the remaining of input for the client tp process the response correctly
            StreamUtils.toOutStream(entityStream, new NullOutputStream());
        }

    }

    protected void trackResourceForClean(FormDataBodyPart storedPart) {
        // nothing to do in client, the cleaning of resources is for server side
    }
}

