package com.archytasit.jersey.multipart.internal.valueproviders;

import com.archytasit.jersey.multipart.FormDataBodyPart;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Parameter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The type Part single value provider.
 *
 * @param <T> the type parameter
 */
public class PartSingleValueProvider<T extends FormDataBodyPart> extends AbstractBodyPartsValueProvider<T> {


    /**
     * Instantiates a new Part single value provider.
     *
     * @param param the param
     */
    public PartSingleValueProvider(Parameter param) {
        super(param);
    }

    // Add filter on generics

    @Override
    protected T applyToBodyParts(ContainerRequest request, List<FormDataBodyPart> enhancedBodyParts) {
        Optional<T> part = (Optional<T>) enhancedBodyParts.stream()
                .filter(Objects::nonNull)
                .filter(b -> parameter.getRawType().isAssignableFrom(b.getClass())).findFirst();
        if (part.isPresent()) {
            return part.get();
        }
        return null;
    }
}
