package com.archytasit.jersey.multipart.internal.valueproviders;

import com.archytasit.jersey.multipart.FormDataBodyPart;

import javax.ws.rs.core.MediaType;

/**
 * The type Enhanced body part.
 */
public class EnhancedBodyPart {

    /**
     * The enum Value extraction mode.
     */
    public enum ValueExtractionMode{
        /**
         * Message body worker value extraction mode.
         */
        MESSAGE_BODY_WORKER,
        /**
         * Extractor value extraction mode.
         */
        EXTRACTOR;
    }

    private final FormDataBodyPart bodyPart;

    private final MediaType mappedMediaType;

    private final ValueExtractionMode mode;

    /**
     * Instantiates a new Enhanced body part.
     *
     * @param bodyPart        the body part
     * @param mappedMediaType the mapped media type
     * @param mode            the mode
     */
    public EnhancedBodyPart(FormDataBodyPart bodyPart, MediaType mappedMediaType, ValueExtractionMode mode) {
        this.bodyPart = bodyPart;
        this.mappedMediaType = mappedMediaType;
        this.mode = mode;
    }


    /**
     * Gets body part.
     *
     * @return the body part
     */
    public FormDataBodyPart getBodyPart() {
        return bodyPart;
    }

    /**
     * Gets mapped media type.
     *
     * @return the mapped media type
     */
    public MediaType getMappedMediaType() {
        return mappedMediaType;
    }

    /**
     * Gets mode.
     *
     * @return the mode
     */
    public ValueExtractionMode getMode() {
        return mode;
    }
}
