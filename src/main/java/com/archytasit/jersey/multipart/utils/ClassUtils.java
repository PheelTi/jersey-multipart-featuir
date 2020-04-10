package com.archytasit.jersey.multipart.utils;

import javax.ws.rs.InternalServerErrorException;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Class utils.
 */
public class ClassUtils {
    private static final Set<Class<?>> TYPES = Stream.of(
                Byte.class,
                byte.class,
                Short.class,
                short.class,
                Integer.class,
                int.class,
                Long.class,
                long.class,
                Float.class,
                float.class,
                Double.class,
                double.class,
                Boolean.class,
                boolean.class,
                Character.class,
                char.class).collect(Collectors.toSet());


    /**
     * Is primitive boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isPrimitive(final Class<?> type) {
        return TYPES.contains(type);
    }

    /**
     * Gets stream collector.
     *
     * @param collectionClass the collection class
     * @return the stream collector
     */
    public static Collector getStreamCollector(Class<?> collectionClass) {
        if (List.class.isAssignableFrom(collectionClass)) {
            return Collectors.toList();
        } else if (SortedSet.class.isAssignableFrom(collectionClass)) {
            return Collectors.toCollection(TreeSet::new);
        } else if (Set.class.isAssignableFrom(collectionClass)) {
            return Collectors.toSet();
        }
        throw new InternalServerErrorException("kind of collection not managed");
    }

    
}
