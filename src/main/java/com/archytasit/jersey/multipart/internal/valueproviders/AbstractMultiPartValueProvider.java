package com.archytasit.jersey.multipart.internal.valueproviders;

import java.util.function.Function;

import javax.ws.rs.BadRequestException;

import org.glassfish.jersey.internal.LocalizationMessages;
import org.glassfish.jersey.server.ContainerRequest;

import com.archytasit.jersey.multipart.model.MultiPart;

/**
 * The type Abstract multi part value provider.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractMultiPartValueProvider<T> implements Function<ContainerRequest, T> {

    @Override
    public final T apply(ContainerRequest request) {
        Object entity;
        synchronized (request) {
            final String requestPropertyName = MultiPart.class.getName();
            entity = request.getProperty(requestPropertyName);
            if (entity == null) {
                entity = request.readEntity(MultiPart.class);
                if (entity == null) {
                    throw new BadRequestException(LocalizationMessages.ERROR_READING_ENTITY_MISSING());
                }
                request.setProperty(requestPropertyName, entity);
            }
        }
        return apply(request, (MultiPart) entity);

    }

    /**
     * Apply t.
     *
     * @param request the request
     * @param entity  the entity
     * @return the t
     */
    protected abstract T apply(ContainerRequest request, MultiPart entity);
}
