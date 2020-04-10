package com.archytasit.jersey.multipart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiPart extends BodyPart {

    private List<BodyPart> bodyParts = new ArrayList<>();

    public List<BodyPart> getBodyParts() {
        return Collections.unmodifiableList(bodyParts);
    }

    public void add(BodyPart bodyPart) {
        if (bodyPart != null) {
            bodyPart.setParent(this);
            bodyParts.add(bodyPart);
        }
    }

    public void addAll(List<BodyPart> bodyPart) {
       if (bodyPart != null) {
           bodyPart.stream().forEach(this::add);
       }
    }

    public MultiPart() {
        this(null, MediaType.MULTIPART_FORM_DATA_TYPE, new MultivaluedHashMap<>(), null);
    }

    public MultiPart(String name, MediaType contentType, MultivaluedMap<String, String> headers, ContentDisposition contentDisposition) {
        super(name, contentType, headers, contentDisposition);
        if (!"multipart".equals(contentType.getType())) {
            throw new IllegalArgumentException("contentType for Multipart wust be 'multipart/*' or compatible subtypes");
        }
    }

    @Override
    public void close() {
        for (BodyPart bp : bodyParts) {
            bp.close();
        }
    }
}
