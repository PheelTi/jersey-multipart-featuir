package com.archytasit.jersey.multipart.internal;

import org.glassfish.jersey.internal.inject.ParamConverterFactory;
import org.glassfish.jersey.internal.util.collection.LazyValue;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * The type Multi part parameter converter provider.
 */
public class MultiPartParameterConverterProvider implements ParamConverterProvider {

    private final LazyValue<ParamConverterFactory> paramConverterFactory;

    /**
     * Instantiates a new Multi part parameter converter provider.
     *
     * @param paramConverterFactory the param converter factory
     */
    public MultiPartParameterConverterProvider(LazyValue<ParamConverterFactory> paramConverterFactory) {
        this.paramConverterFactory = paramConverterFactory;
    }

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
            return paramConverterFactory.get().getConverter(rawType, genericType, annotations);
    }
}
