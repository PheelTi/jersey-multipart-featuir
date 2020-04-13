package com.archytasit.jersey.multipart.internal;

import org.glassfish.jersey.internal.inject.ParamConverterFactory;
import org.glassfish.jersey.internal.util.collection.LazyValue;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class MultiPartParameterConverterProvider implements ParamConverterProvider {

    private final LazyValue<ParamConverterFactory> paramConverterFactory;

    public MultiPartParameterConverterProvider(LazyValue<ParamConverterFactory> paramConverterFactory) {
        this.paramConverterFactory = paramConverterFactory;
    }

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
            return paramConverterFactory.get().getConverter(rawType, genericType, annotations);
    }
}
