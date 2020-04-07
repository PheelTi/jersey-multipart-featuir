package com.archytasit.jersey.multipart.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import org.apache.commons.io.output.NullOutputStream;

import com.archytasit.jersey.multipart.LoggingWrapper;

/**
 * The type Input stream counter.
 */
public class InputStreamCounter extends InputStream {

    /**
     * The Counter.
     */
    protected long counter = 0L;

    /**
     * The Is.
     */
    protected InputStream is;

    /**
     * Instantiates a new Input stream counter.
     *
     * @param is the is
     */
    public InputStreamCounter(InputStream is) {
        this.is = is;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return addToCounterInt(is.read(b));
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return (int) addToCounterInt(is.read(b, off, len));
    }

    @Override
    public long skip(long n) throws IOException {
        return addToCounter(is.skip(n));
    }

    /**
     * Add to counter long.
     *
     * @param num the num
     * @return the long
     */
    protected long addToCounter(long num) {
        this.counter+=num;
        return num;
    }
    private int addToCounterInt(int num) {
        addToCounter(num);
        return num;
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
        this.counter = 0L;
    }

    @Override
    public int read() throws IOException {
        int result = is.read();
        if (result > -1) {
            addToCounter(1L);
        }
        return result;
    }

    @Override
    public int available() throws IOException {
        return super.available();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        super.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return super.markSupported();
    }

    /**
     * Gets content read.
     *
     * @return the content read
     */
    public long getContentRead() {
        return counter;
    }

}

