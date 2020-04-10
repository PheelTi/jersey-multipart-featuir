package com.archytasit.jersey.multipart.internal.valueproviders;

import com.archytasit.jersey.multipart.MultiPart;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * The type Multi part entity value provider.
 */
public class MultiPartEntityValueProvider extends AbstractMultiPartValueProvider<MultiPart> {

    @Override
    protected MultiPart apply(ContainerRequest request, MultiPart entity) {
        return entity;
    }
}
