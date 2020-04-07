package com.archytasit.jersey.multipart.internal.valueproviders;

import org.glassfish.jersey.server.ContainerRequest;

import com.archytasit.jersey.multipart.model.MultiPart;

/**
 * The type Multi part entity value provider.
 */
public class MultiPartEntityValueProvider extends AbstractMultiPartValueProvider<MultiPart> {

    @Override
    protected MultiPart apply(ContainerRequest request, MultiPart entity) {
        return entity;
    }
}
