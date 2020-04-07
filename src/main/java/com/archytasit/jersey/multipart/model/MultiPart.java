package com.archytasit.jersey.multipart.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.archytasit.jersey.multipart.model.bodyparts.IBodyPart;

/**
 * The type Multi part.
 */
public class MultiPart {

    private MediaType mediaType;

    private List<IBodyPart> bodyParts = new ArrayList<>();

    /**
     * Gets media type.
     *
     * @return the media type
     */
    public MediaType getMediaType() {
        return mediaType;
    }

    /**
     * Sets media type.
     *
     * @param mediaType the media type
     */
    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * Gets body parts.
     *
     * @return the body parts
     */
    public List<IBodyPart> getBodyParts() {
        return bodyParts;
    }

    /**
     * Sets body parts.
     *
     * @param bodyParts the body parts
     */
    public void setBodyParts(List<IBodyPart> bodyParts) {
        this.bodyParts = bodyParts;
    }
}
