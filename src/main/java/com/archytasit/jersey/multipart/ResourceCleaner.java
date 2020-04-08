package com.archytasit.jersey.multipart;

import java.util.*;
import java.util.function.Supplier;

import com.archytasit.jersey.multipart.model.IBodyPart;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * The type Resource cleaner.
 */
public class ResourceCleaner {

    private static String REQUEST_FILES_LIST_PROPERTY_NAME = ResourceCleaner.class.getName();


    /**
     * Track resource to clean.
     *
     * @param resourceToClean the resource to clean
     * @param cleanMode       the clean mode
     * @param request         the request
     */
    public void trackResourceToClean(IBodyPart resourceToClean, MultiPartConfig.CleanResourceMode cleanMode, ContainerRequest request) {
        if (!cleanMode.equals(MultiPartConfig.CleanResourceMode.NEVER)) {
            Map<MultiPartConfig.CleanResourceMode, Set<IBodyPart>> filesToClean = getOrCreateRequestProperty(request, REQUEST_FILES_LIST_PROPERTY_NAME, HashMap::new);
            if (filesToClean.get(cleanMode) == null) {
                filesToClean.put(cleanMode, new HashSet<>());
            }
            filesToClean.get(cleanMode).add(resourceToClean);
        }
    }


    /**
     * Clean request.
     *
     * @param request   the request
     * @param isSuccess the is success
     */
    public void cleanRequest(ContainerRequest request, boolean isSuccess) {

        Map<MultiPartConfig.CleanResourceMode, Set<IBodyPart>> filesToClean = (Map<MultiPartConfig.CleanResourceMode, Set<IBodyPart>>) request.getProperty(ResourceCleaner.class.getName());
        if (filesToClean != null) {
            filesToClean.getOrDefault(MultiPartConfig.CleanResourceMode.ALWAYS, new HashSet<>()).forEach(IBodyPart::cleanResource);
            if (!isSuccess) {
                filesToClean.getOrDefault(MultiPartConfig.CleanResourceMode.ON_ERROR, new HashSet<>()).forEach(IBodyPart::cleanResource);
            }
        }
    }



    private <T> T getOrCreateRequestProperty(ContainerRequest request, String propName, Supplier<T> newItem) {
        T item = (T) request.getProperty(propName);
        if (item == null) {
            request.setProperty(propName, item = newItem.get());
        }
        return item;
    }

}
