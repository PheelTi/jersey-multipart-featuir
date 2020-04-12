package com.archytasit.jersey.multipart.common;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

public class ExceptionMappers {

    @Priority(Integer.MIN_VALUE)
    @Provider
    public static class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {
        @Override
        public Response toResponse(Throwable exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return Response.status(500).entity(exception.getMessage()).build();
        }
    }

    @Priority(Integer.MIN_VALUE-1)
    @Provider
    public static class WebApplicationExceptionExceptionMapper implements ExceptionMapper<WebApplicationException> {
        @Override
        public Response toResponse(WebApplicationException exception) {
            exception.printStackTrace();
            return Response.status(exception.getResponse().getStatus()).entity(exception.getMessage()).build();
        }
    }



}
