package com.archytasit.jersey.multipart.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.archytasit.jersey.multipart.model.bodyparts.IBodyPart;

public class MultiPart {

    private MediaType mediaType;

    private List<IBodyPart> bodyParts = new ArrayList<>();

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public List<IBodyPart> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(List<IBodyPart> bodyParts) {
        this.bodyParts = bodyParts;
    }
}
