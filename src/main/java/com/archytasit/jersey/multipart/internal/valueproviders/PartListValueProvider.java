package com.archytasit.jersey.multipart.internal.valueproviders;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.glassfish.jersey.model.Parameter;
import org.glassfish.jersey.server.ContainerRequest;


import com.archytasit.jersey.multipart.model.IBodyPart;
import com.archytasit.jersey.multipart.utils.ClassUtils;

/**
 * The type Part list value provider.
 *
 * @param <T> the type parameter
 */
public class PartListValueProvider<T extends IBodyPart> extends AbstractBodyPartsValueProvider<Collection<T>> {


    private final Class<?> genericClass;

    /**
     * Instantiates a new Part list value provider.
     *
     * @param parameter    the parameter
     * @param genericClass the generic class
     */
    public PartListValueProvider(Parameter parameter, Class<?> genericClass) {
        super(parameter);
        this.genericClass = genericClass;
    }

    @Override
    protected Collection<T> applyToBodyParts(ContainerRequest request, List<IBodyPart> enhancedBodyParts) {
        return (Collection<T>) enhancedBodyParts.stream()
                    .filter(Objects::nonNull)
                    .filter(b -> this.genericClass.isAssignableFrom(b.getClass()))
                    .collect(ClassUtils.getStreamCollector(parameter.getRawType()));
    }
}
