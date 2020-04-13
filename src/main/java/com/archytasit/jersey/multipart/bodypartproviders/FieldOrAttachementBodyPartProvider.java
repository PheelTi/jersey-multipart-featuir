package com.archytasit.jersey.multipart.bodypartproviders;

import com.archytasit.jersey.multipart.FormDataBodyPart;
import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.parsers.StreamingPart;

import java.io.IOException;

/**
 * The type Form field or attachement body part provider.
 */
public class FieldOrAttachementBodyPartProvider implements IFormDataBodyPartProvider {

    private IFormDataBodyPartProvider formFieldBodyPartProvider;
    private IFormDataBodyPartProvider attachmentBodyPartProvider;

    /**
     * Instantiates a new Form field or attachement body part provider.
     *
     * @param formFieldBodyPartProvider  the form field body part provider
     * @param attachmentBodyPartProvider the attachment body part provider
     */
    public FieldOrAttachementBodyPartProvider(IFormDataBodyPartProvider formFieldBodyPartProvider, IFormDataBodyPartProvider attachmentBodyPartProvider) {
        if (formFieldBodyPartProvider == null || attachmentBodyPartProvider == null) {
            throw new IllegalArgumentException();
        }
        this.formFieldBodyPartProvider = formFieldBodyPartProvider;
        this.attachmentBodyPartProvider = attachmentBodyPartProvider;
    }

    @Override
    public final FormDataBodyPart provideBodyPart(MultiPartConfig config, StreamingPart streamingPart) throws IOException {
        if (streamingPart.isFormField()) {
            return formFieldBodyPartProvider.provideBodyPart(config, streamingPart);
        } else {
            return attachmentBodyPartProvider.provideBodyPart(config, streamingPart);
        }
    }
}
