package com.archytasit.jersey.multipart.model.databags.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.archytasit.jersey.multipart.model.databags.IDataBag;

/**
 * The type File data bag.
 */
public class FileDataBag implements IDataBag {

    private File tempFile;

    /**
     * Instantiates a new File data bag.
     *
     * @param tempFile the temp file
     */
    public FileDataBag(File tempFile) {
        this.tempFile = tempFile;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(tempFile);
    }

    @Override
    public long getContentLength() {
        return tempFile.length();
    }

    @Override
    public void cleanResource() {
        tempFile.delete();
    }
}
