package com.archytasit.jersey.multipart.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.core.MediaType;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FormDataParam {

    String value();

    Filter filter() default Filter.BOTH;

    Map[] mapContentTypeAs() default {};

    String[] filterContentType() default {};

    public enum Filter {
        BOTH, FORMFIELD, ATTACHMENT
    }



}
