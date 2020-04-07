package com.archytasit.jersey.multipart.internal.valueproviders;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.model.Parameter;
import org.glassfish.jersey.server.ContainerRequest;

import com.archytasit.jersey.multipart.LoggingWrapper;
import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.annotations.Map;
import com.archytasit.jersey.multipart.model.MultiPart;
import com.archytasit.jersey.multipart.model.bodyparts.IBodyPart;
import com.archytasit.jersey.multipart.utils.HeadersUtils;

public abstract class AbstractEnhancedBodyPartsValueProvider<T> extends AbstractBodyPartsValueProvider<T> {

    private Predicate<EnhancedBodyPart> mediaTypeFilter;
    private Function<IBodyPart, MediaType> mediaTypeConverter;
    private BiFunction<IBodyPart, MediaType, EnhancedBodyPart.ValueExtractionMode> extractionModeSupplier;

    public AbstractEnhancedBodyPartsValueProvider(Parameter parameter) {
        super(parameter);
        this.mediaTypeConverter = prepareMediaTypeConverter(marker.mapContentTypeAs());
        this.mediaTypeFilter = prepareMediaTypeFilter(marker.filterContentType());
        this.extractionModeSupplier = prepareModeSupplier();
    }

    @Override
    protected final T applyToBodyParts(ContainerRequest request, List<IBodyPart> bodyParts) {
        List<EnhancedBodyPart> enhancedBodyParts = bodyParts.stream()
                .filter(Objects::nonNull)
                .map(b -> {
                    MediaType targetMediaType = mediaTypeConverter.apply(b);
                    return new EnhancedBodyPart(b, targetMediaType, extractionModeSupplier.apply(b, targetMediaType));
                })
                .filter(mediaTypeFilter)
                .collect(Collectors.toList());
        return applyToEnhancedBodyParts(request, enhancedBodyParts);
    }

    protected abstract T applyToEnhancedBodyParts(ContainerRequest request, List<EnhancedBodyPart> enhancedBodyParts);


    private Function<IBodyPart, MediaType> prepareMediaTypeConverter(Map[] mappingsStr) {

        List<Mapping> mappings = Stream.of(mappingsStr).map( m -> {
                    MediaType from = HeadersUtils.getMediaType(m.from(), null);
                    MediaType to = HeadersUtils.getMediaType(m.to(), null);
                    if (from == null|| to == null) {
                        LoggingWrapper.warn(AbstractBodyPartsValueProvider.class, String.format("ignored invalid media type in FormDataParam.Map : %s -> %s", m.from(), m.to()), null);
                        return null;
                    }
                    return new Mapping(from, to);
                }
        ).filter(Objects::nonNull).collect(Collectors.toList());

        return (b) -> {
            MediaType mediaType = b.getMediaType() != null ? b.getMediaType() : MediaType.TEXT_PLAIN_TYPE;
            for (Mapping mapping : mappings) {
                if (mediaType.isCompatible(mapping.from)) {
                    return mapping.to;
                }
            }
            return mediaType;
        };
    }

    private Predicate<EnhancedBodyPart> prepareMediaTypeFilter(String[] filterContentType) {
        Set<MediaType> allowedMediaTypes =  Arrays.stream(filterContentType)
                .filter(Objects::nonNull)
                .map(s -> HeadersUtils.getMediaType(s, null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (allowedMediaTypes.isEmpty()) {
            return (b) -> true;
        } else {
            return (b) -> allowedMediaTypes.contains(b.getMappedMediaType());
        }
    }

    private static class Mapping {
        MediaType from;
        MediaType to;

        public Mapping(MediaType from, MediaType to) {
            this.from = from;
            this.to = to;
        }
    }


    private BiFunction<IBodyPart, MediaType, EnhancedBodyPart.ValueExtractionMode> prepareModeSupplier() {
        return (b, m) -> {
            if (b.isFormField() && m.isCompatible(MediaType.TEXT_PLAIN_TYPE)) {
                return EnhancedBodyPart.ValueExtractionMode.EXTRACTOR;
            }
            return EnhancedBodyPart.ValueExtractionMode.MESSAGE_BODY_WORKER;
        };
    }

}

