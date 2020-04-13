package com.archytasit.jersey.multipart.internal.valueproviders;

import com.archytasit.jersey.multipart.FormDataBodyPart;
import com.archytasit.jersey.multipart.annotations.Map;
import com.archytasit.jersey.multipart.utils.HeadersUtils;
import org.glassfish.jersey.model.Parameter;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Abstract enhanced body parts value provider.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractEnhancedBodyPartsValueProvider<T> extends AbstractBodyPartsValueProvider<T> {

    private Predicate<EnhancedBodyPart> mediaTypeFilter;
    private Function<FormDataBodyPart, MediaType> mediaTypeConverter;
    private BiFunction<FormDataBodyPart, MediaType, EnhancedBodyPart.ValueExtractionMode> extractionModeSupplier;

    /**
     * Instantiates a new Abstract enhanced body parts value provider.
     *
     * @param parameter the parameter
     */
    public AbstractEnhancedBodyPartsValueProvider(Parameter parameter) {
        super(parameter);
        this.mediaTypeConverter = prepareMediaTypeConverter(marker.mapContentTypeAs());
        this.mediaTypeFilter = prepareMediaTypeFilter(marker.filterContentType());
        this.extractionModeSupplier = prepareModeSupplier();
    }

    @Override
    protected final T applyToBodyParts(ContainerRequest request, List<FormDataBodyPart> bodyParts) {
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

    /**
     * Apply to enhanced body parts t.
     *
     * @param request           the request
     * @param enhancedBodyParts the enhanced body parts
     * @return the t
     */
    protected abstract T applyToEnhancedBodyParts(ContainerRequest request, List<EnhancedBodyPart> enhancedBodyParts);


    private Function<FormDataBodyPart, MediaType> prepareMediaTypeConverter(Map[] mappingsStr) {

        List<Mapping> mappings = Stream.of(mappingsStr).map( m -> {
                    MediaType from = HeadersUtils.getMediaType(m.from());
                    MediaType to = HeadersUtils.getMediaType(m.to());
                    if (from == null|| to == null) {
                        Logger.getLogger(AbstractEnhancedBodyPartsValueProvider.class.getName()).log(Level.WARNING, String.format("ignored invalid media type in FormDataParam.Map : %s -> %s", m.from(), m.to()));
                        return null;
                    }
                    return new Mapping(from, to);
                }
        ).filter(Objects::nonNull).collect(Collectors.toList());

        return (b) -> {
            MediaType mediaType = b.getContentType() != null ? b.getContentType() : MediaType.TEXT_PLAIN_TYPE;
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
                .map(s -> HeadersUtils.getMediaType(s))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (allowedMediaTypes.isEmpty()) {
            return (b) -> true;
        } else {
            return (b) -> allowedMediaTypes.contains(b.getMappedMediaType());
        }
    }

    private static class Mapping {
        /**
         * The From.
         */
        MediaType from;
        /**
         * The To.
         */
        MediaType to;

        /**
         * Instantiates a new Mapping.
         *
         * @param from the from
         * @param to   the to
         */
        public Mapping(MediaType from, MediaType to) {
            this.from = from;
            this.to = to;
        }
    }


    private BiFunction<FormDataBodyPart, MediaType, EnhancedBodyPart.ValueExtractionMode> prepareModeSupplier() {
        return (b, m) -> {
            if (b.isFormField() && m.isCompatible(MediaType.TEXT_PLAIN_TYPE)) {
                return EnhancedBodyPart.ValueExtractionMode.EXTRACTOR;
            }
            return EnhancedBodyPart.ValueExtractionMode.MESSAGE_BODY_WORKER;
        };
    }

}

