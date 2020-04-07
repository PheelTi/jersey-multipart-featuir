package com.archytasit.jersey.multipart.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class EntityTooLargeException extends WebApplicationException {

    public EntityTooLargeException() {
        super(Response.Status.REQUEST_ENTITY_TOO_LARGE);
    }
}
