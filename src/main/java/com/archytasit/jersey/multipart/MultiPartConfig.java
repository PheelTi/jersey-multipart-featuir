package com.archytasit.jersey.multipart;

import java.io.InputStream;
import java.util.function.Consumer;

import javax.ws.rs.ext.Provider;

import com.archytasit.jersey.multipart.exception.EntityTooLargeException;

@Provider
public class MultiPartConfig {

    private String tempFileDirectory = System.getProperty("java.io.tmpdir");
    private String tempFilePrefix = "MULTIPART_";
    private String tempFileSuffix = null;
    private long requestSizeLimit = -1;
    private Consumer<InputStream> requestSizeLimitAction = (is) -> {
        throw new EntityTooLargeException();
    };

    public MultiPartConfig() {

    }

    public String getTempFileDirectory() {
        return tempFileDirectory;
    }

    public String getTempFilePrefix() {
        return tempFilePrefix;
    }

    public String getTempFileSuffix() {
        return tempFileSuffix;
    }

    public long getRequestSizeLimit() {
        return requestSizeLimit;
    }

    public Consumer<InputStream> getRequestSizeLimitAction() {
        return requestSizeLimitAction;
    }


    public MultiPartConfig requestSizeLimit(long requestSizeLimit) {
        this.requestSizeLimit = requestSizeLimit;
        return this;
    }

    public MultiPartConfig requestSizeLimitAction(Consumer<InputStream> requestSizeLimitAction) {
        this.requestSizeLimitAction = requestSizeLimitAction;
        return this;
    }

    public MultiPartConfig tempFileDirectory(String tempFileDirectory) {
        this.tempFileDirectory = tempFileDirectory;
        return this;
    }

    public MultiPartConfig tempFilePrefix(String tempFilePrefix) {
        this.tempFilePrefix = tempFilePrefix;
        return this;
    }

    public MultiPartConfig tempFileSuffix(String tempFileSuffix) {
        this.tempFileSuffix = tempFileSuffix;
        return this;
    }
}
