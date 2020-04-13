package com.archytasit.jersey.multipart.bodypartproviders;

import com.archytasit.jersey.multipart.FormDataFileBodyPart;
import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.parsers.StreamingPart;
import com.archytasit.jersey.multipart.utils.StreamUtils;

import java.io.File;
import java.io.IOException;

/**
 * The type File body part provider.
 */
public class FormDataFileBodyPartProvider implements IFormDataBodyPartProvider {

    public FormDataFileBodyPart provideBodyPart(MultiPartConfig config, StreamingPart streamingPart) throws IOException {
        File f = File.createTempFile(config.getTempFilePrefix(), config.getTempFileSuffix(), new File(config.getTempFileDirectory()));
        StreamUtils.toTempFileNoClose(streamingPart.getInputStream(), f);
        return new FormDataFileBodyPart(streamingPart, f);
    }
}
