package com.archytasit.jersey.multipart.parsers;

import com.archytasit.jersey.multipart.model.internal.StreamingPart;

import javax.ws.rs.InternalServerErrorException;
import java.util.Iterator;

/**
 * The type Streaming part iterator.
 */
public class StreamingPartIterator implements Iterator<StreamingPart> {

    /**
     * The interface Throwing supplier.
     *
     * @param <T> the type parameter
     */
    @FunctionalInterface
    public static interface ThrowingSupplier<T> {
        /**
         * Get t.
         *
         * @return the t
         * @throws Exception the exception
         */
        T get() throws Exception;
    }

    private ThrowingSupplier<Boolean> hasNextSupplier;
    private ThrowingSupplier<StreamingPart> nextValueSupplier;


    /**
     * Instantiates a new Streaming part iterator.
     *
     * @param hasNextSupplier   the has next supplier
     * @param nextValueSupplier the next value supplier
     */
    public StreamingPartIterator(ThrowingSupplier<Boolean> hasNextSupplier, ThrowingSupplier<StreamingPart> nextValueSupplier) {
        if (hasNextSupplier == null || nextValueSupplier == null) {
            throw new IllegalArgumentException();
        }
        this.hasNextSupplier = hasNextSupplier;
        this.nextValueSupplier = nextValueSupplier;
    }

    @Override
    public boolean hasNext() {
        try {
            return hasNextSupplier.get();
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public StreamingPart next() {
        try {
            return nextValueSupplier.get();
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }
    }
}
