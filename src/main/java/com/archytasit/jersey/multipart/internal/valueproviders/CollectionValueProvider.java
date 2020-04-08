package com.archytasit.jersey.multipart.internal.valueproviders;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.ws.rs.InternalServerErrorException;

import org.glassfish.jersey.internal.util.ReflectionHelper;
import org.glassfish.jersey.message.MessageBodyWorkers;
import org.glassfish.jersey.model.Parameter;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractor;

import com.archytasit.jersey.multipart.exception.FormDataParamException;
import com.archytasit.jersey.multipart.utils.ClassUtils;

/**
 * The type Collection value provider.
 */
public class CollectionValueProvider extends SingleValueProvider {

    private Collector streamCollector;

    private Class<?> genericTypeArgument;

    /**
     * Instantiates a new Collection value provider.
     *
     * @param parameter the parameter
     * @param extractor the extractor
     */
    public CollectionValueProvider(Parameter parameter, MultivaluedParameterExtractor<?> extractor) {
        super(parameter, extractor);
        this.streamCollector = ClassUtils.getStreamCollector(parameter.getRawType());
        this.genericTypeArgument = ReflectionHelper.getGenericTypeArgumentClasses(parameter.getType()).get(0);
    }


    @Override
    protected Object applyToEnhancedBodyParts(ContainerRequest request, List<EnhancedBodyPart> enhancedBodyParts) {
        Set<EnhancedBodyPart.ValueExtractionMode> modes = enhancedBodyParts.stream().map(EnhancedBodyPart::getMode).collect(Collectors.toSet());

        if (modes.isEmpty()) {
            return null;
        } else if (modes.size() == 1) {
            MessageBodyWorkers mbw = request.getWorkers();
            EnhancedBodyPart.ValueExtractionMode mode = modes.iterator().next();
            if (extractor == null || EnhancedBodyPart.ValueExtractionMode.MESSAGE_BODY_WORKER.equals(mode)) {
                return enhancedBodyParts.stream().map((b) -> applyMessageBodyWorker(b, genericTypeArgument, genericTypeArgument, b.getMappedMediaType(), mbw)).collect(streamCollector);
            } else if (extractor != null) {
                return applyExtractor(enhancedBodyParts.stream(), mbw);
            } else {
                return enhancedBodyParts.stream().map(b -> null).collect(streamCollector);
            }
        } else {
            throw new FormDataParamException(new IllegalArgumentException("Mix of attachements and form fields could not be extracted"), getParameterName(), parameter.getDefaultValue());
        }

    }



}
