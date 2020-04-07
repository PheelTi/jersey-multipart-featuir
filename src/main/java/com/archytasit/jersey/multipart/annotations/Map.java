package com.archytasit.jersey.multipart.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.core.MediaType;


/**
 * The interface Map.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Map {
    /**
     * From string.
     *
     * @return the string
     */
    String from() default MediaType.WILDCARD;

    /**
     * To string.
     *
     * @return the string
     */
    String to();
}