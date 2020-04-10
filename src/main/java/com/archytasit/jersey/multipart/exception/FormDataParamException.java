package com.archytasit.jersey.multipart.exception;

import com.archytasit.jersey.multipart.annotations.FormDataParam;
import org.glassfish.jersey.server.ParamException;

import javax.ws.rs.core.Response;

/**
 * The type Form data param exception.
 */
public class FormDataParamException extends ParamException {

    /**
     * Instantiates a new Form data param exception.
     *
     * @param cause              the cause
     * @param name               the name
     * @param defaultStringValue the default string value
     */
    public FormDataParamException(final Throwable cause, final String name, final String defaultStringValue) {
        super(cause, Response.Status.BAD_REQUEST, FormDataParam.class, name, defaultStringValue);
    }
}
