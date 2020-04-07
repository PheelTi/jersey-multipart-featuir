package com.archytasit.jersey.multipart.model.databags.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.archytasit.jersey.multipart.model.databags.IDataBag;

public class FileDataBag implements IDataBag {

    private File tempFile;

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
