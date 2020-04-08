package com.archytasit.jersey.multipart.model;

import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.model.internal.StreamingPart;
import com.archytasit.jersey.multipart.utils.StreamUtils;

import java.io.File;
import java.io.IOException;

/**
 * The type File body part provider.
 */
public class FileBodyPartProvider implements IBodyPartProvider<FileBodyPart> {

    public FileBodyPart provideBodyPart(MultiPartConfig config, StreamingPart streamingPart) throws IOException {
        File f = File.createTempFile(config.getTempFilePrefix(), config.getTempFileSuffix(), new File(config.getTempFileDirectory()));
        StreamUtils.toTempFileNoClose(streamingPart.getInputStream(), f);
        return new FileBodyPart(streamingPart, f);
    }
}
