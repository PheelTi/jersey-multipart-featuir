package com.archytasit.jersey.multipart.exception;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ParamException;

import com.archytasit.jersey.multipart.annotations.FormDataParam;

public class FormDataParamException extends ParamException {

    public FormDataParamException(final Throwable cause, final String name, final String defaultStringValue) {
        super(cause, Response.Status.BAD_REQUEST, FormDataParam.class, name, defaultStringValue);
    }
}
