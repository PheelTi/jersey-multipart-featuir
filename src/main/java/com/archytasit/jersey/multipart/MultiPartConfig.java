package com.archytasit.jersey.multipart;

import java.io.InputStream;
import java.util.function.Consumer;

import javax.ws.rs.ext.Provider;

import com.archytasit.jersey.multipart.exception.EntityTooLargeException;

/**
 * The type Multi part config.
 */
@Provider
public class MultiPartConfig {

    /**
     * The enum Clean resource mode.
     */
    public enum CleanResourceMode {
        /**
         * Always clean resource mode.
         */
        ALWAYS,
        /**
         * On error clean resource mode.
         */
        ON_ERROR,
        /**
         * Never clean resource mode.
         */
        NEVER;
    }

    private String tempFileDirectory = System.getProperty("java.io.tmpdir");
    private String tempFilePrefix = "MULTIPART_";
    private String tempFileSuffix = null;
    private long requestSizeLimit = -1;
    private CleanResourceMode cleanResourceMode = CleanResourceMode.ALWAYS;
    private Consumer<InputStream> requestSizeLimitAction = (is) -> {
        throw new EntityTooLargeException();
    };

    /**
     * Instantiates a new Multi part config.
     */
    public MultiPartConfig() {

    }

    /**
     * Gets temp file directory.
     *
     * @return the temp file directory
     */
    public String getTempFileDirectory() {
        return tempFileDirectory;
    }

    /**
     * Gets temp file prefix.
     *
     * @return the temp file prefix
     */
    public String getTempFilePrefix() {
        return tempFilePrefix;
    }

    /**
     * Gets temp file suffix.
     *
     * @return the temp file suffix
     */
    public String getTempFileSuffix() {
        return tempFileSuffix;
    }

    /**
     * Gets request size limit.
     *
     * @return the request size limit
     */
    public long getRequestSizeLimit() {
        return requestSizeLimit;
    }

    /**
     * Gets request size limit action.
     *
     * @return the request size limit action
     */
    public Consumer<InputStream> getRequestSizeLimitAction() {
        return requestSizeLimitAction;
    }


    /**
     * Gets clean resource mode.
     *
     * @return the clean resource mode
     */
    public CleanResourceMode getCleanResourceMode() {
        return cleanResourceMode;
    }


    /**
     * Clean resource mode multi part config.
     *
     * @param cleanResourceMode the clean resource mode
     * @return the multi part config
     */
    public MultiPartConfig cleanResourceMode(CleanResourceMode cleanResourceMode) {
        this.cleanResourceMode = cleanResourceMode;
        return this;
    }

    /**
     * Request size limit multi part config.
     *
     * @param requestSizeLimit the request size limit
     * @return the multi part config
     */
    public MultiPartConfig requestSizeLimit(long requestSizeLimit) {
        this.requestSizeLimit = requestSizeLimit;
        return this;
    }

    /**
     * Request size limit action multi part config.
     *
     * @param requestSizeLimitAction the request size limit action
     * @return the multi part config
     */
    public MultiPartConfig requestSizeLimitAction(Consumer<InputStream> requestSizeLimitAction) {
        this.requestSizeLimitAction = requestSizeLimitAction;
        return this;
    }

    /**
     * Temp file directory multi part config.
     *
     * @param tempFileDirectory the temp file directory
     * @return the multi part config
     */
    public MultiPartConfig tempFileDirectory(String tempFileDirectory) {
        this.tempFileDirectory = tempFileDirectory;
        return this;
    }

    /**
     * Temp file prefix multi part config.
     *
     * @param tempFilePrefix the temp file prefix
     * @return the multi part config
     */
    public MultiPartConfig tempFilePrefix(String tempFilePrefix) {
        this.tempFilePrefix = tempFilePrefix;
        return this;
    }

    /**
     * Temp file suffix multi part config.
     *
     * @param tempFileSuffix the temp file suffix
     * @return the multi part config
     */
    public MultiPartConfig tempFileSuffix(String tempFileSuffix) {
        this.tempFileSuffix = tempFileSuffix;
        return this;
    }
}
