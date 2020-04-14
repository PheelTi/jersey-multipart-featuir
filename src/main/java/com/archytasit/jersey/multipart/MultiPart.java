package com.archytasit.jersey.multipart;

import com.archytasit.jersey.multipart.utils.HeadersUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Multi part.
 */
public class MultiPart extends BodyPart {

    private List<BodyPart> bodyParts = new ArrayList<>();

    /**
     * Gets body parts.
     *
     * @return the body parts
     */
    public List<BodyPart> getBodyParts() {
        return Collections.unmodifiableList(bodyParts);
    }

    /**
     * Add multi part.
     *
     * @param bodyPart the body part
     * @return the multi part
     */
    public MultiPart add(BodyPart bodyPart) {
        if (bodyPart != null) {
            if (bodyPart instanceof MultiPart
                    && (!HeadersUtils.MULTIPART_WILDCARD_MEDIATYPE.isCompatible(bodyPart.getContentType())
                    || MediaType.MULTIPART_FORM_DATA_TYPE.isCompatible(bodyPart.getContentType()))) {
                throw new IllegalArgumentException("nested multipart must be of type 'multipart/*' and not 'multipart/form-data'");
            }
            bodyPart.setParent(this);
            bodyParts.add(bodyPart);
        }
        return this;
    }

    /**
     * Add all.
     *
     * @param bodyPart the body part
     */
    public void addAll(List<BodyPart> bodyPart) {
       if (bodyPart != null) {
           bodyPart.stream().forEach(this::add);
       }
    }


    /**
     * Mixed multi part multi part.
     *
     * @param name the name
     * @return the multi part
     */
    public static MultiPart mixedMultiPart(String name) {
        return new MultiPart(name, new MediaType("multipart", "mixed"), new MultivaluedHashMap<>(), ContentDisposition.defaultValue(name));
    }

    /**
     * Form data multi part multi part.
     *
     * @return the multi part
     */
    public static MultiPart formDataMultiPart() {
        return new MultiPart(null, MediaType.MULTIPART_FORM_DATA_TYPE, new MultivaluedHashMap<>(), null);
    }

    /**
     * Instantiates a new Multi part.
     *
     * @param name               the name
     * @param contentType        the content type
     * @param headers            the headers
     * @param contentDisposition the content disposition
     */
    public MultiPart(String name, MediaType contentType, MultivaluedMap<String, String> headers, ContentDisposition contentDisposition) {
        super(name, contentType, headers, contentDisposition);
        if (!HeadersUtils.MULTIPART_WILDCARD_MEDIATYPE.isCompatible(contentType)) {
            throw new IllegalArgumentException("contentType for Multipart wust be 'multipart/*' or compatible subtypes");
        }
    }

    @Override
    public void close() {
        for (BodyPart bp : bodyParts) {
            bp.close();
        }
    }

    /**
     * Entity entity.
     *
     * @return the entity
     */
    public Entity<MultiPart> entity() {
        return Entity.entity(this, getContentType());
    }
}
