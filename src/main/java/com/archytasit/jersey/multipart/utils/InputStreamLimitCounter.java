package com.archytasit.jersey.multipart.utils;

import org.apache.commons.io.output.NullOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Input stream limit counter.
 */
public class InputStreamLimitCounter extends InputStreamCounter {

    private final Consumer<InputStream> onLimitReached;
    private long limit = -1L;


    /**
     * Instantiates a new Input stream limit counter.
     *
     * @param limit          the limit
     * @param is             the is
     * @param onLimitReached the on limit reached
     */
    public InputStreamLimitCounter(long limit, InputStream is, Consumer<InputStream> onLimitReached) {
        super(is);
        this.limit = limit;
        this.onLimitReached = onLimitReached;
    }

    @Override
    protected long addToCounter(long num) {
        super.addToCounter(num);

        if (this.counter > this.limit) {
            onLimitReached.accept(this.is);
        }
        return num;
    }
}

