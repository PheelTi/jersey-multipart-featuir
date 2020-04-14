package com.archytasit.jersey.multipart.bodypartproviders;

import com.archytasit.jersey.multipart.FormDataBodyPart;
import com.archytasit.jersey.multipart.MultiPartConfig;
import com.archytasit.jersey.multipart.parsers.StreamingPart;
import com.archytasit.jersey.multipart.utils.HeadersUtils;
import com.archytasit.jersey.multipart.utils.InputStreamCounter;
import com.archytasit.jersey.multipart.utils.StreamUtils;


import java.io.*;
import java.nio.charset.Charset;

/**
 * The type Memory limit body part provider.
 */
public class MemoryLimitBodyPartProvider implements IFormDataBodyPartProvider {

    private Integer limitOverride = null;



    private IFormDataBodyPartProvider memoryProvider;

    private IFormDataBodyPartProvider largeObjectProvider;

    /**
     * Instantiates a new Memory limit body part provider.
     *
     * @param limitOverride       the limit override, if null, will get the configured one in MultiPartConfig
     * @param memoryProvider      the memory provider, provider used if below limit
     * @param largeObjectProvider the large object provider, provider used if over limit
     */
    public MemoryLimitBodyPartProvider(Integer limitOverride, IFormDataBodyPartProvider memoryProvider, IFormDataBodyPartProvider largeObjectProvider) {
        this.limitOverride = limitOverride;
        if (memoryProvider == null || largeObjectProvider == null) {
            throw new IllegalArgumentException("memoryProvider and largeObjectProvider must not be null");
        }
        this.memoryProvider = memoryProvider;
        this.largeObjectProvider = largeObjectProvider;
    }

    @Override
    public FormDataBodyPart provideBodyPart(MultiPartConfig config, StreamingPart streamingPart) throws IOException {

        int limit = limitOverride != null ? limitOverride : config.getMemorySizeLimit();

        Charset charset = HeadersUtils.getCharset(streamingPart.getContentType());
        if (charset == null) {
            charset = config.getDefaultCharset();
        }
        byte[] memoryBytes;
        boolean swithToFile = false;
        InputStreamCounter inputStream = new InputStreamCounter(streamingPart.getInputStream());

        try(ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            int currentBufferSite = Math.min(StreamUtils.BUFFER_SIZE, limit >= 0 ? (limit + 1) : StreamUtils.BUFFER_SIZE);
            byte[] buffer;
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer = new byte[currentBufferSite])) != -1) {
                outStream.write(buffer, 0, bytesRead);
                int alreadyRead = (int) inputStream.getContentRead();
                if (limit > 0 && limit < alreadyRead) {
                    swithToFile = true;
                    break;
                } else {
                    currentBufferSite = Math.min(StreamUtils.BUFFER_SIZE, (limit - alreadyRead) >= 0 ? (limit - alreadyRead + 1) : StreamUtils.BUFFER_SIZE);
                }
            }
            outStream.flush();
            memoryBytes = outStream.toByteArray();
        }


        try(ByteArrayInputStream stream = new ByteArrayInputStream(memoryBytes)) {
            if (!swithToFile) {
                return memoryProvider.provideBodyPart(config, overridePartStream(streamingPart, stream));
            } else {
                return largeObjectProvider.provideBodyPart(config, overridePartStream(streamingPart, new SequenceInputStream(stream, streamingPart.getInputStream())));
            }
        }
    }

    private StreamingPart overridePartStream(StreamingPart part, InputStream stream) {
        return new StreamingPart(part.getFieldName(), part.getContentType(), part.getContentDisposition(), part.getHeaders(), part.isFormField(), part.getFileName(), stream);
    }


}
