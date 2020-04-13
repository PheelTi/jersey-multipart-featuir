package com.archytasit.jersey.multipart.internal.valueproviders;

import com.archytasit.jersey.multipart.exception.FormDataParamException;
import org.glassfish.jersey.internal.util.collection.NullableMultivaluedHashMap;
import org.glassfish.jersey.message.MessageBodyWorkers;
import org.glassfish.jersey.model.Parameter;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractor;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Single value provider.
 */
public class SingleValueProvider extends AbstractEnhancedBodyPartsValueProvider<Object> {

    /**
     * The Extractor.
     */
    protected MultivaluedParameterExtractor<?> extractor;

    /**
     * Instantiates a new Single value provider.
     *
     * @param parameter the parameter
     * @param extractor the extractor
     */
    public SingleValueProvider(Parameter parameter, MultivaluedParameterExtractor<?> extractor) {
        super(parameter);
        this.extractor = extractor;
    }

    @Override
    protected Object applyToEnhancedBodyParts(ContainerRequest request, List<EnhancedBodyPart> enhancedBodyParts) {
        if (enhancedBodyParts.isEmpty()) {
            return null;
        }
        EnhancedBodyPart bp = enhancedBodyParts.get(0);
        if (extractor == null || EnhancedBodyPart.ValueExtractionMode.MESSAGE_BODY_WORKER.equals(bp.getMode())) {
            return applyMessageBodyWorker(bp, request.getWorkers());
        } else if (extractor != null) {
            return applyExtractor(Stream.of(bp), request.getWorkers());
        }

        return null;

    }


    /**
     * Apply extractor object.
     *
     * @param bp      the bp
     * @param workers the workers
     * @return the object
     */
    protected Object applyExtractor(Stream<EnhancedBodyPart> bp, MessageBodyWorkers workers) {
        Predicate<String> filterNullOrNot = (s) -> true;
        // if sorted set we exclude null (and empty) param values
        if ((SortedSet.class.isAssignableFrom(parameter.getRawType()))) {
            filterNullOrNot = s -> s != null && !s.isEmpty();
        }
        MultivaluedMap paramValuesMap = new NullableMultivaluedHashMap();
        paramValuesMap.addAll(getParameterName(),
            bp.map(b -> asString(b, workers)).filter(filterNullOrNot).collect(Collectors.toList()));
        return extractor.extract(paramValuesMap);

    }

    private String asString(EnhancedBodyPart iBodyPart, MessageBodyWorkers workers) {
        return applyMessageBodyWorker(iBodyPart, String.class, String.class, MediaType.TEXT_PLAIN_TYPE, workers);
    }

    /**
     * Apply message body worker object.
     *
     * @param bodyPart the body part
     * @param workers  the workers
     * @return the object
     */
    protected Object applyMessageBodyWorker(EnhancedBodyPart bodyPart, MessageBodyWorkers workers) {
        return applyMessageBodyWorker(bodyPart, parameter.getRawType(), parameter.getType(), bodyPart.getMappedMediaType(), workers);
    }


    /**
     * Apply message body worker t.
     *
     * @param <T>             the type parameter
     * @param bodyPart        the body part
     * @param rawType         the raw type
     * @param type            the type
     * @param customMediaType the custom media type
     * @param workers         the workers
     * @return the t
     */
    protected <T> T applyMessageBodyWorker(EnhancedBodyPart bodyPart, Class<T> rawType, Type type, MediaType customMediaType, MessageBodyWorkers workers) {
        T result = null;
        try {

            result = (T) bodyPart.getBodyPart().getEntity(rawType);

            if (result == null) {
                MessageBodyReader<T> bodyReader = workers.getMessageBodyReader(
                        rawType,
                        type,
                        parameter.getAnnotations(),
                        customMediaType);
                if (bodyReader != null) {
                    result = bodyReader.readFrom(rawType,
                            type,
                            parameter.getAnnotations(),
                            customMediaType,
                            bodyPart.getBodyPart().getHeaders(),
                            bodyPart.getBodyPart().getInputStream());
                }
            }


        } catch (final IOException e) {
            throw new FormDataParamException(e, getParameterName(), parameter.getDefaultValue());
        }
        return result;
    }


}
