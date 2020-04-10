package com.archytasit.jersey.multipart.common;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

public class LocalDateParamConverter implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> aClass, Type type, Annotation[] annotations) {
        if (aClass != null && aClass.isAssignableFrom(LocalDate.class)) {
            return (ParamConverter<T>) new ParamConverter<LocalDate>() {
                @Override
                public LocalDate fromString(String s) {
                    return s == null || s.trim().isEmpty() ? null : LocalDate.parse(s);
                }

                @Override
                public String toString(LocalDate localDate) {
                    return localDate.toString();
                }
            };

        }
        return null;
    }


}