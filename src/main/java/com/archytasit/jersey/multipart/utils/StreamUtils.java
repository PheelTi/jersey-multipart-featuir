package com.archytasit.jersey.multipart.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

    private static final int BUFFER_SIZE = 8 * 1024;

    public static void toOutStream(InputStream openStream, OutputStream outStream)  throws IOException {

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = openStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();

    }

    public static void toNullStream(InputStream openStream, OutputStream outStream)  throws IOException {

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = openStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();

    }

    public static File toTempFileNoClose(InputStream inStream, File tempFile)  throws IOException {
        try (OutputStream outStream = new FileOutputStream(tempFile)) {
            toOutStream(inStream, outStream);
        };
        return tempFile;
    }

    public static byte[] toBytesNoClose(InputStream inStream) throws IOException {
        try(ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            toOutStream(inStream, outStream);
            return outStream.toByteArray();
        }


    }
}

