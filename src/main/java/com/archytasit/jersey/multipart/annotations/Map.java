package com.archytasit.jersey.multipart.annotations;

import javax.ws.rs.core.MediaType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


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