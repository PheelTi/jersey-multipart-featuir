package com.archytasit.jersey.multipart.internal;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.inject.Provider;
import javax.ws.rs.InternalServerErrorException;

import org.glassfish.jersey.internal.util.ReflectionHelper;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.AbstractValueParamProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;

import com.archytasit.jersey.multipart.annotations.FormDataParam;
import com.archytasit.jersey.multipart.internal.valueproviders.CollectionValueProvider;
import com.archytasit.jersey.multipart.internal.valueproviders.MultiPartEntityValueProvider;
import com.archytasit.jersey.multipart.internal.valueproviders.PartListValueProvider;
import com.archytasit.jersey.multipart.internal.valueproviders.PartSingleValueProvider;
import com.archytasit.jersey.multipart.internal.valueproviders.SingleValueProvider;
import com.archytasit.jersey.multipart.model.MultiPart;
import com.archytasit.jersey.multipart.model.bodyparts.IBodyPart;

public class FormDataParamValueParamProvider  extends AbstractValueParamProvider {

    public FormDataParamValueParamProvider(Provider<MultivaluedParameterExtractorProvider> extractorProvider) {
        super(extractorProvider, Parameter.Source.ENTITY, Parameter.Source.UNKNOWN);
    }


    @Override
    protected Function<ContainerRequest, ?> createValueProvider(Parameter parameter) {

        final Class<?> rawType = parameter.getRawType();

        if (Parameter.Source.ENTITY == parameter.getSource() || Parameter.Source.UNKNOWN == parameter.getSource()) {
            if (MultiPart.class.isAssignableFrom(rawType)) {
                return new MultiPartEntityValueProvider();
            } else if (parameter.getAnnotation(FormDataParam.class) != null) {
                if (List.class.isAssignableFrom(rawType) || Set.class.isAssignableFrom(rawType)) {
                    final Class genericClazz = ReflectionHelper.getGenericTypeArgumentClasses(parameter.getType()).get(0);
                    if (IBodyPart.class.isAssignableFrom(genericClazz)) {
                        return new PartListValueProvider(parameter, genericClazz);
                    } else {
                        return new CollectionValueProvider(parameter, get(parameter));
                    }
                } else if (IBodyPart.class.isAssignableFrom(rawType)) {
                    return new PartSingleValueProvider(parameter);
                } else {
                    return new SingleValueProvider(parameter, get(parameter));
                }
            }
        }
        return null;
    }

    @Override
    public PriorityType getPriority() {
        return Priority.HIGH;
    }
}
