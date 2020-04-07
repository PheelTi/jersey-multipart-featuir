package com.archytasit.jersey.multipart;

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Providers;

import org.glassfish.jersey.server.ContainerRequest;

import com.archytasit.jersey.multipart.model.databags.IDataBagProvider;
import com.archytasit.jersey.multipart.model.databags.IDataBag;
import com.archytasit.jersey.multipart.model.bodyparts.IBodyPart;
import com.archytasit.jersey.multipart.model.bodyparts.IBodyPartProvider;
import com.archytasit.jersey.multipart.utils.InputStreamLimitCounter;
import com.archytasit.jersey.multipart.model.MultiPart;


/**
 * The type File upload message body worker.
 */
@Consumes("multipart/*")
@Singleton
@ConstrainedTo(RuntimeType.SERVER)
public class FileUploadMessageBodyWorker implements MessageBodyReader<MultiPart> {

    @Inject
    private ResourceCleaner resourceCleaner;

    @Context
    private Provider<ContainerRequest> requestProvider;

    private MultiPartConfig config;

    @Inject
    private IBodyPartProvider bodyPartProvider;

    @Inject
    private IDataBagProvider dataBagProvider;

    /**
     * Instantiates a new File upload message body worker.
     *
     * @param providers the providers
     */
    @Inject
    public FileUploadMessageBodyWorker(@Context final Providers providers) {
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

        InputStream baseInputStream = entityStream;
        if (config.getRequestSizeLimit() >= 0) {
            baseInputStream = new InputStreamLimitCounter(config.getRequestSizeLimit(), entityStream, config.getRequestSizeLimitAction());
        }



        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(mediaType);
        List<IBodyPart> bodyParts = bodyPartProvider.parseRequest(config, dataBagProvider, mediaType, httpHeaders, baseInputStream);
        bodyParts.stream().forEach(b -> {
            if (b.getDataBag() instanceof IDataBag && ((IDataBag) b.getDataBag()).isCleanable()) {
                resourceCleaner.trackResourceToClean((IDataBag)b.getDataBag(), requestProvider.get());
            }
        });
        multiPart.setBodyParts(bodyParts == null ? new ArrayList<>() : bodyParts.stream().filter(Objects::nonNull).collect(Collectors.toList()));

        return multiPart;
    }

}

