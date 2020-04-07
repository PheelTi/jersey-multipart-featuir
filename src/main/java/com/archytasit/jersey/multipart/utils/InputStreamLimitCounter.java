package com.archytasit.jersey.multipart.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import org.apache.commons.io.output.NullOutputStream;

import com.archytasit.jersey.multipart.LoggingWrapper;

public class InputStreamLimitCounter extends InputStreamCounter {

    private final Consumer<InputStream> onLimitReached;
    private long limit = -1L;


    public InputStreamLimitCounter(long limit, InputStream is, Consumer<InputStream> onLimitReached) {
        super(is);
        this.limit = limit;
        this.onLimitReached = onLimitReached;
    }

    @Override
    protected long addToCounter(long num) {
        super.addToCounter(num);

        if (this.counter > this.limit) {
            try {
                StreamUtils.toOutStream(this.is, new NullOutputStream());
            } catch (IOException e) {
                LoggingWrapper.warn(InputStreamLimitCounter.class, "failed to read the remaining of request", e);
            }
            onLimitReached.accept(this.is);
        }
        return num;
    }
}

