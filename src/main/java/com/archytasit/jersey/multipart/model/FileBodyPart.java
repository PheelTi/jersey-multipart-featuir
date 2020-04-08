package com.archytasit.jersey.multipart.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.archytasit.jersey.multipart.model.internal.StreamingPart;


/**
 * The type File body part.
 */
public class FileBodyPart extends AbstractBodyPart {


    private File tempFile;

    /**
     * Instantiates a new File body part.
     *
     * @param streamingPart the streaming part
     * @param tempFile      the temp file
     */
    public FileBodyPart(StreamingPart streamingPart, File tempFile) {
        super(streamingPart, tempFile.length());
        this.tempFile = tempFile;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(tempFile);
    }

    @Override
    public void cleanResource() {
        tempFile.delete();
    }

    /**
     * Gets temp file.
     *
     * @return the temp file
     */
    public File getTempFile() {
        return tempFile;
    }
}
