package com.archytasit.jersey.multipart.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * The type Entity too large exception.
 */
public class EntityTooLargeException extends WebApplicationException {

    /**
     * Instantiates a new Entity too large exception.
     */
    public EntityTooLargeException() {
        super(Response.Status.REQUEST_ENTITY_TOO_LARGE);
    }
}
