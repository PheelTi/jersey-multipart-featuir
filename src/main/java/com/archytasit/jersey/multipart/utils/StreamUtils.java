package com.archytasit.jersey.multipart.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The type Stream utils.
 */
public class StreamUtils {

    private static final int BUFFER_SIZE = 8 * 1024;

    /**
     * To out stream.
     *
     * @param openStream the open stream
     * @param outStream  the out stream
     * @throws IOException the io exception
     */
    public static void toOutStream(InputStream openStream, OutputStream outStream)  throws IOException {

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = openStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();

    }

    /**
     * To null stream.
     *
     * @param openStream the open stream
     * @param outStream  the out stream
     * @throws IOException the io exception
     */
    public static void toNullStream(InputStream openStream, OutputStream outStream)  throws IOException {

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = openStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();

    }

    /**
     * To temp file no close file.
     *
     * @param inStream the in stream
     * @param tempFile the temp file
     * @return the file
     * @throws IOException the io exception
     */
    public static File toTempFileNoClose(InputStream inStream, File tempFile)  throws IOException {
        try (OutputStream outStream = new FileOutputStream(tempFile)) {
            toOutStream(inStream, outStream);
        };
        return tempFile;
    }

    /**
     * To bytes no close byte [ ].
     *
     * @param inStream the in stream
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public static byte[] toBytesNoClose(InputStream inStream) throws IOException {
        try(ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            toOutStream(inStream, outStream);
            return outStream.toByteArray();
        }


    }
}

