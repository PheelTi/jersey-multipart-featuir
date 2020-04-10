package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.bodypartproviders.IFormDataBodyPartProvider;
import com.archytasit.jersey.multipart.parsers.StreamingPart;
import com.archytasit.jersey.multipart.parsers.IRequestParser;
import com.archytasit.jersey.multipart.parsers.StreamingPartIterator;

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
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.function.Supplier;


/**
 * The type File upload message body worker.
 */
@Consumes("multipart/*")
@Singleton
@ConstrainedTo(RuntimeType.CLIENT)
public class MultiPartMessageBodyReaderClient implements MessageBodyReader<MultiPart> {


    private static final MediaType MULTIPART_WILDCARD = new MediaType("multipart", null);


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
        this.config = getInstanceFromContext(providers, MultiPartConfig.class, MultiPartConfig::new);
    }

    private <T> T getInstanceFromContext(Providers providers, Class<T> clazz, Supplier<T> defaultValueSupplier) {
        final ContextResolver<T> contextResolver =
                providers.getContextResolver(clazz, MediaType.WILDCARD_TYPE);
        T instance = null;
        if (contextResolver != null) {
            instance = contextResolver.getContext(this.getClass());
        }
        return instance != null ? instance : defaultValueSupplier.get();
    }

    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return mediaType.isCompatible(MediaType.MULTIPART_FORM_DATA_TYPE) && MultiPart.class.isAssignableFrom(type);
    }

    public MultiPart readFrom(Class<MultiPart> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {

//        final String userAgent = httpHeaders.getFirst(HttpHeaders.USER_AGENT);
//        boolean fileNameFix = userAgent != null && userAgent.contains(" MSIE ");

        return readFromParent(null, mediaType, httpHeaders, entityStream);

    }

    public MultiPart readFromParent(String name, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {

        MultiPart multiPart = new MultiPart(name, mediaType, httpHeaders, ContentDisposition.fromHeaderValues(name, httpHeaders));

        StreamingPartIterator partIterator = requestParser.getPartIterator(mediaType, httpHeaders, entityStream);
        if (partIterator != null) {
            while (partIterator.hasNext()) {
                StreamingPart part = partIterator.next();

                if (MULTIPART_WILDCARD.isCompatible(part.getContentType())) {

                    // nested multipart may have the same name of original multipart.
                    String fieldName = name;
                    if (config.isKeepNesterFieldNamesHierarchy() || name == null) {
                        fieldName = part.getFieldName();
                    }

                    multiPart.add(readFromParent(fieldName, part.getContentType(), part.getHeaders(), part.getInputStream()));
                } else {
                    multiPart.add(bodyPartProvider.provideBodyPart(config, part));
                }
            }
        }

        return multiPart;
    }

}

