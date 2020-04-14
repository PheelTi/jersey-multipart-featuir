package com.archytasit.jersey.multipart.utils;

/**
 * The type Http header parameters.
 */
public final class HttpHeaderParameters {

    /**
     * The type Content type.
     */
    public final class ContentType {
        /**
         * The constant CHARSET.
         */
        public static final String CHARSET = "charset";
        /**
         * The constant BOUNDARY.
         */
        public static final String BOUNDARY = "boundary";
    }

    /**
     * The type Content disposition.
     */
    public final class ContentDisposition {
        /**
         * The constant FILENAME.
         */
        public static final String FILENAME = "filename";
        /**
         * The constant CREATION_DATE.
         */
        public static final String CREATION_DATE = "creation-date";
        /**
         * The constant MODIFICATION_DATE.
         */
        public static final String MODIFICATION_DATE = "modification-date";
        /**
         * The constant READ_DATE.
         */
        public static final String READ_DATE = "read-date";
        /**
         * The constant SIZE.
         */
        public static final String SIZE = "size";
        /**
         * The constant NAME.
         */
        public static final String NAME = "name";
    }

}
