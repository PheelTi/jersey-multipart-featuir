package com.archytasit.jersey.multipart.internal.valueproviders;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Parameter;

import com.archytasit.jersey.multipart.model.MultiPart;
import com.archytasit.jersey.multipart.model.bodyparts.IBodyPart;

/**
 * The type Part single value provider.
 *
 * @param <T> the type parameter
 */
public class PartSingleValueProvider<T extends IBodyPart> extends AbstractBodyPartsValueProvider<T> {


    /**
     * Instantiates a new Part single value provider.
     *
     * @param param the param
     */
    public PartSingleValueProvider(Parameter param) {
        super(param);
    }


    @Override
    protected T applyToBodyParts(ContainerRequest request, List<IBodyPart> enhancedBodyParts) {
        Optional<T> part = (Optional<T>) enhancedBodyParts.stream()
                .filter(Objects::nonNull)
                .filter(b -> parameter.getRawType().isAssignableFrom(b.getClass())).findFirst();
        if (part.isPresent()) {
            return part.get();
        }
        return null;
    }
}
