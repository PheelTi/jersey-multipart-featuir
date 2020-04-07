package com.archytasit.jersey.multipart.internal.valueproviders;

import java.util.function.Function;

import javax.ws.rs.core.MediaType;

import com.archytasit.jersey.multipart.model.bodyparts.IBodyPart;

public class EnhancedBodyPart {

    public enum ValueExtractionMode{
        MESSAGE_BODY_WORKER,
        EXTRACTOR;
    }

    private final IBodyPart bodyPart;

    private final MediaType mappedMediaType;

    private final ValueExtractionMode mode;

    public EnhancedBodyPart(IBodyPart bodyPart, MediaType mappedMediaType, ValueExtractionMode mode) {
        this.bodyPart = bodyPart;
        this.mappedMediaType = mappedMediaType;
        this.mode = mode;
    }


    public IBodyPart getBodyPart() {
        return bodyPart;
    }

    public MediaType getMappedMediaType() {
        return mappedMediaType;
    }

    public ValueExtractionMode getMode() {
        return mode;
    }
}
