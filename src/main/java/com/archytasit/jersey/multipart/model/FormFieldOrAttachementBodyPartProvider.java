package com.archytasit.jersey.multipart.model;

import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.model.internal.StreamingPart;

import java.io.IOException;

/**
 * The type Form field or attachement body part provider.
 */
public class FormFieldOrAttachementBodyPartProvider implements IBodyPartProvider {

    private IBodyPartProvider formFieldBodyPartProvider;
    private IBodyPartProvider attachmentBodyPartProvider;

    /**
     * Instantiates a new Form field or attachement body part provider.
     *
     * @param formFieldBodyPartProvider  the form field body part provider
     * @param attachmentBodyPartProvider the attachment body part provider
     */
    public FormFieldOrAttachementBodyPartProvider(IBodyPartProvider formFieldBodyPartProvider, IBodyPartProvider attachmentBodyPartProvider) {
        if (formFieldBodyPartProvider == null || attachmentBodyPartProvider == null) {
            throw new IllegalArgumentException();
        }
        this.formFieldBodyPartProvider = formFieldBodyPartProvider;
        this.attachmentBodyPartProvider = attachmentBodyPartProvider;
    }

    @Override
    public final IBodyPart provideBodyPart(MultiPartConfig config, StreamingPart streamingPart) throws IOException {
        if (streamingPart.isFormField()) {
            return formFieldBodyPartProvider.provideBodyPart(config, streamingPart);
        } else {
            return attachmentBodyPartProvider.provideBodyPart(config, streamingPart);
        }
    }
}
