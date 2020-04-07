package com.archytasit.jersey.multipart.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.core.MediaType;


@Retention(RetentionPolicy.RUNTIME)
public @interface Map {
    String from() default MediaType.WILDCARD;
    String to();
}